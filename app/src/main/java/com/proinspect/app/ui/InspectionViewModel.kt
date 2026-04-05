package com.proinspect.app.ui

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proinspect.app.data.InspectionSections
import com.proinspect.app.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class InspectionViewModel(application: android.app.Application) : AndroidViewModel(application) {

    private val db = ProInspectDatabase.getInstance(application)
    private val reportDao = db.reportDao()
    private val itemDao = db.inspectionItemDao()
    private val photoDao = db.inspectionPhotoDao()
    private val settingsDao = db.appSettingsDao()

    val allReports: StateFlow<List<Report>> = reportDao.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appSettings: StateFlow<AppSettings> = settingsDao.getSettings()
        .map { it ?: AppSettings(id = 1) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings(id = 1))

    private val _currentReportId = MutableStateFlow<Long?>(null)

    val currentReport: StateFlow<Report?> = _currentReportId.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else reportDao.getAllReports().map { list -> list.find { it.id == id } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val items: StateFlow<Map<String, InspectionItem>> = _currentReportId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else itemDao.getItemsForReport(id)
    }.map { list -> list.associateBy { it.itemId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val photos: StateFlow<List<InspectionPhoto>> = _currentReportId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else photoDao.getPhotosForReport(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _navigateToReport = MutableSharedFlow<Unit>()
    val navigateToReport: SharedFlow<Unit> = _navigateToReport

    private var pendingPhotoPath: String? = null
    private var pendingSection: String = ""
    private var pendingItemId: String? = null

fun createNewReport() {
    viewModelScope.launch {
        val s = appSettings.value
        val report = Report(
            inspectionDate = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date()),
            inspectorName = s.inspectorName,
            inspectorCert = s.inspectorLicense,
            inspectorCompany = s.inspectorCompany,
            inspectorPhone = s.inspectorPhone,
            clientEmail = s.inspectorEmail
        )
        val id = reportDao.insertReport(report)
        _currentReportId.value = id
        _navigateToReport.emit(Unit)
        if (FirebaseSync.isSignedIn) {
            FirebaseSync.syncReport(report.copy(id = id))
        }
    }
}

    fun loadReport(id: Long) {
        _currentReportId.value = id
    }

fun saveReport(report: Report) {
    viewModelScope.launch {
        reportDao.insertReport(report)
        if (FirebaseSync.isSignedIn) {
            FirebaseSync.syncReport(report)
        }
    }
}

fun deleteReport(report: Report) {
    viewModelScope.launch {
        itemDao.deleteItemsForReport(report.id)
        photoDao.deletePhotosForReport(report.id)
        reportDao.deleteReport(report)
        if (FirebaseSync.isSignedIn) {
            FirebaseSync.deleteReport(report.id)
        }
    }
}

    fun setItemRating(itemId: String, section: String, rating: Rating) {
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val existing = items.value[itemId]
            val item = existing?.copy(rating = rating)
    ?: InspectionItem(
        reportId = reportId,
        itemId = itemId,
        section = section,
        rating = rating,
        narrative = ""
    )
            itemDao.insertItem(item)
        }
    }

    fun setItemNarrative(itemId: String, section: String, narrative: String) {
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val existingItem = items.value[itemId]
          val item = existingItem?.copy(narrative = narrative)
    ?: InspectionItem(
        reportId = reportId,
        itemId = itemId,
        section = section,
        rating = Rating.NOT_RATED,
        narrative = narrative
    )
            itemDao.insertItem(item)
        }
    }

    fun prepareCameraUri(context: Context, section: String, itemId: String?): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        
        val photoDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val photoFile = File(photoDir, "photo_${timestamp}.jpg")
        
        pendingPhotoPath = photoFile.absolutePath
        pendingSection = section
        pendingItemId = itemId
        
        return FileProvider.getUriForFile(
            context, 
            "${context.packageName}.fileprovider", 
            photoFile
        )
    }

    fun onPhotoCaptured(success: Boolean) {
        if (!success) return
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val path = pendingPhotoPath ?: return@launch
            photoDao.insertPhoto(
                InspectionPhoto(
                    reportId = reportId,
                    photoPath = path,
                    section = pendingSection,
                    itemId = pendingItemId
                )
            )
        }
    }

    fun addPhotoFromGallery(context: Context, uri: Uri, section: String, itemId: String?) {
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            
            val photoDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val destFile = File(photoDir, "gallery_${timestamp}.jpg")
            
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                photoDao.insertPhoto(
                    InspectionPhoto(
                        reportId = reportId,
                        photoPath = destFile.absolutePath,
                        section = section,
                        itemId = itemId
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deletePhoto(photo: InspectionPhoto) {
        viewModelScope.launch {
            try {
                File(photo.photoPath).delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            photoDao.deletePhoto(photo)
        }
    }

    fun saveSettings(settings: AppSettings) {
        viewModelScope.launch {
            settingsDao.insertSettings(settings)
        }
    }

    fun saveAgreementPath(reportId: Long, path: String, isSigned: Boolean) {
        viewModelScope.launch {
            val report = allReports.value.find { it.id == reportId } ?: return@launch
            val updated = if (isSigned) {
                report.copy(signedAgreementPath = path)
            } else {
                report.copy(agreementPath = path)
            }
            reportDao.insertReport(updated)
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // NEW FUNCTIONS FOR SETTINGS SCREEN
    // ═══════════════════════════════════════════════════════════════════════════

    fun saveCompanyLogo(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val logoDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val logoFile = File(logoDir, "company_logo_${timestamp}.jpg")
                
                context.contentResolver.openInputStream(uri)?.use { input ->
                    logoFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                val currentSettings = appSettings.value
                val updatedSettings = currentSettings.copy(companyLogoPath = logoFile.absolutePath)
                settingsDao.insertSettings(updatedSettings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveBadge(context: Context, uri: Uri, badgeNumber: Int) {
        viewModelScope.launch {
            try {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val badgeDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val badgeFile = File(badgeDir, "badge${badgeNumber}_${timestamp}.jpg")
                
                context.contentResolver.openInputStream(uri)?.use { input ->
                    badgeFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                val currentSettings = appSettings.value
                val updatedSettings = when (badgeNumber) {
                    1 -> currentSettings.copy(badge1Path = badgeFile.absolutePath)
                    2 -> currentSettings.copy(badge2Path = badgeFile.absolutePath)
                    3 -> currentSettings.copy(badge3Path = badgeFile.absolutePath)
                    4 -> currentSettings.copy(badge4Path = badgeFile.absolutePath)
                    else -> currentSettings
                }
                settingsDao.insertSettings(updatedSettings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearBadge(badgeNumber: Int) {
        viewModelScope.launch {
            val currentSettings = appSettings.value
            val updatedSettings = when (badgeNumber) {
                1 -> currentSettings.copy(badge1Path = "")
                2 -> currentSettings.copy(badge2Path = "")
                3 -> currentSettings.copy(badge3Path = "")
                4 -> currentSettings.copy(badge4Path = "")
                else -> currentSettings
            }
            settingsDao.insertSettings(updatedSettings)
        }
    }

    fun updateSettings(settings: AppSettings) {
        viewModelScope.launch {
            settingsDao.insertSettings(settings)
        }
    }

    fun exportBackup(context: Context, onComplete: (Uri?) -> Unit) {
        viewModelScope.launch {
            try {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val backupDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val backupFile = File(backupDir, "proinspect_backup_${timestamp}.db")
                
                // Copy database file
                val dbPath = context.getDatabasePath("proinspect.db")
                dbPath.copyTo(backupFile, overwrite = true)
                
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    backupFile
                )
                onComplete(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(null)
            }
        }
    }

  fun restoreBackup(context: android.content.Context, uri: Uri, onComplete: (Int) -> Unit) {
    viewModelScope.launch {
        try {
            val dbPath = context.getDatabasePath("proinspect.db")
            context.contentResolver.openInputStream(uri)?.use { input ->
                dbPath.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            _currentReportId.value = null
            onComplete(1)
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete(-1)
        }
    }
}
  fun restoreFromCloud(onComplete: (Int) -> Unit) {
    viewModelScope.launch {
        try {
            val cloudReports = FirebaseSync.fetchAllReports()
            var count = 0
            cloudReports.forEach { data ->
                try {
                    val report = Report(
                        id = (data["id"] as? Long) ?: 0L,
                        reportNumber = data["reportNumber"] as? String ?: "",
                        propertyAddress = data["propertyAddress"] as? String ?: "",
                        propertyCity = data["propertyCity"] as? String ?: "",
                        clientName = data["clientName"] as? String ?: "",
                        clientEmail = data["clientEmail"] as? String ?: "",
                        inspectorName = data["inspectorName"] as? String ?: "",
                        inspectorCert = data["inspectorCert"] as? String ?: "",
                        inspectorCompany = data["inspectorCompany"] as? String ?: "",
                        inspectorPhone = data["inspectorPhone"] as? String ?: "",
                        inspectionDate = data["inspectionDate"] as? String ?: "",
                        inspectionTime = data["inspectionTime"] as? String ?: "",
                        weatherConditions = data["weatherConditions"] as? String ?: "",
                        yearBuilt = data["yearBuilt"] as? String ?: "",
                        squareFootage = data["squareFootage"] as? String ?: "",
                        overviewNarrative = data["overviewNarrative"] as? String ?: "",
                        limitations = data["limitations"] as? String ?: "",
                        roofingNarrative = data["roofingNarrative"] as? String ?: "",
                        exteriorNarrative = data["exteriorNarrative"] as? String ?: "",
                        structureNarrative = data["structureNarrative"] as? String ?: "",
                        electricalNarrative = data["electricalNarrative"] as? String ?: "",
                        hvacNarrative = data["hvacNarrative"] as? String ?: "",
                        plumbingNarrative = data["plumbingNarrative"] as? String ?: "",
                        interiorNarrative = data["interiorNarrative"] as? String ?: "",
                        insulationNarrative = data["insulationNarrative"] as? String ?: "",
                        garageNarrative = data["garageNarrative"] as? String ?: "",
                        inspectionService = data["inspectionService"] as? String ?: "",
                        inspectionAmount = data["inspectionAmount"] as? String ?: "",
                        ancillaryServices = data["ancillaryServices"] as? String ?: "",
                        ancillaryAmount = data["ancillaryAmount"] as? String ?: "",
                        paymentStatus = data["paymentStatus"] as? String ?: "Amount Due",
                        paymentMethod = data["paymentMethod"] as? String ?: "",
                        paymentNotes = data["paymentNotes"] as? String ?: "",
                        propertyType = data["propertyType"] as? String ?: "",
                        roofType = data["roofType"] as? String ?: "",
                        roofAge = data["roofAge"] as? String ?: "",
                        heatType = data["heatType"] as? String ?: "",
                        heatBrand = data["heatBrand"] as? String ?: "",
                        heatAge = data["heatAge"] as? String ?: "",
                        acType = data["acType"] as? String ?: "",
                        acBrand = data["acBrand"] as? String ?: "",
                        acAge = data["acAge"] as? String ?: "",
                        panelBrand = data["panelBrand"] as? String ?: "",
                        panelAmps = data["panelAmps"] as? String ?: "",
                        whType = data["whType"] as? String ?: "",
                        whAge = data["whAge"] as? String ?: "",
                        whCapacity = data["whCapacity"] as? String ?: "",
                        createdAt = (data["createdAt"] as? Long) ?: System.currentTimeMillis()
                    )
                    reportDao.insertReport(report)
                    count++
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            onComplete(count)
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete(-1)
        }
    }
}
    // Add these methods to InspectionViewModel class

fun getCodeForItem(itemId: String): String {
    // Return a code based on the item ID
    // You can customize this logic based on your needs
    return when {
        itemId.startsWith("EXT") -> "E-${itemId.takeLast(3)}"
        itemId.startsWith("INT") -> "I-${itemId.takeLast(3)}"
        itemId.startsWith("ROOF") -> "R-${itemId.takeLast(3)}"
        itemId.startsWith("ELEC") -> "EL-${itemId.takeLast(3)}"
        itemId.startsWith("PLUMB") -> "P-${itemId.takeLast(3)}"
        itemId.startsWith("HVAC") -> "H-${itemId.takeLast(3)}"
        else -> itemId
    }
}

fun getCodesForSection(section: String): List<String> {
    // Return a list of codes for a given section
    // Customize based on your inspection standards
    return when (section.uppercase()) {
        "EXTERIOR" -> listOf("E-001", "E-002", "E-003", "E-004", "E-005")
        "INTERIOR" -> listOf("I-001", "I-002", "I-003", "I-004", "I-005")
        "ROOF" -> listOf("R-001", "R-002", "R-003", "R-004")
        "ELECTRICAL" -> listOf("EL-001", "EL-002", "EL-003", "EL-004")
        "PLUMBING" -> listOf("P-001", "P-002", "P-003", "P-004")
        "HVAC" -> listOf("H-001", "H-002", "H-003")
        "STRUCTURE" -> listOf("S-001", "S-002", "S-003", "S-004")
        else -> emptyList()
    }
}
// ═══════════════════════════════════════════════════════════════════════════
    // ALIASES — match what the UI expects
    // ═══════════════════════════════════════════════════════════════════════════

    val currentItems = items
    val currentPhotos = photos
    val settings = appSettings

    private val _currentItemId = MutableStateFlow<String?>(null)
    val currentItemId: StateFlow<String?> = _currentItemId

    fun setCurrentItem(itemId: String) {
        _currentItemId.value = itemId
    }

    fun clearCurrentItem() {
        _currentItemId.value = null
    }

    fun updateRating(itemId: String, rating: Rating) {
        val section = items.value[itemId]?.section
            ?: InspectionSections.allItems.find { it.id == itemId }?.section
            ?: ""
        setItemRating(itemId, section, rating)
    }

    fun updateNarrative(itemId: String, narrative: String) {
        val section = items.value[itemId]?.section
            ?: InspectionSections.allItems.find { it.id == itemId }?.section
            ?: ""
        setItemNarrative(itemId, section, narrative)
    }

    fun addPhoto(itemId: String, uri: android.net.Uri) {
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val section = items.value[itemId]?.section
                ?: InspectionSections.allItems.find { it.id == itemId }?.section
                ?: ""
            photoDao.insertPhoto(
                InspectionPhoto(
                    reportId = reportId,
                    photoPath = uri.toString(),
                    section = section,
                    itemId = itemId
                )
            )
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // REPORT FIELD UPDATERS
    // ═══════════════════════════════════════════════════════════════════════════

    private fun updateReport(transform: (Report) -> Report) {
        viewModelScope.launch {
            val report = currentReport.value ?: return@launch
            reportDao.insertReport(transform(report))
        }
    }

    fun updatePropertyAddress(value: String) = updateReport { it.copy(propertyAddress = value) }
    fun updateClientName(value: String)       = updateReport { it.copy(clientName = value) }
    fun updateInspectorName(value: String)    = updateReport { it.copy(inspectorName = value) }
    fun updateInspectionDate(value: String)   = updateReport { it.copy(inspectionDate = value) }
    fun updateYearBuilt(value: String)        = updateReport { it.copy(yearBuilt = value) }
    fun updateSquareFootage(value: String)    = updateReport { it.copy(squareFootage = value) }
    fun updatePropertyType(value: String)     = updateReport { it.copy(propertyType = value) }

    // ═══════════════════════════════════════════════════════════════════════════
    // PDF + EMAIL
    // ═══════════════════════════════════════════════════════════════════════════

    fun generatePdf(context: android.content.Context, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val report = currentReport.value ?: return@launch
                val itemsList = items.value.values.toList()
                val photosList = photos.value
                val appSettingsValue = appSettings.value
                val file = com.proinspect.app.pdf.PdfGenerator.generate(
                    context, report, itemsList, photosList, appSettingsValue
                )
                onComplete(file.exists())
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    fun emailReport(context: android.content.Context, email: String) {
        viewModelScope.launch {
            try {
                val report = currentReport.value ?: return@launch
                val itemsList = items.value.values.toList()
                val photosList = photos.value
                val appSettingsValue = appSettings.value
                val file = com.proinspect.app.pdf.PdfGenerator.generate(
                    context, report, itemsList, photosList, appSettingsValue
                )
                val uri = androidx.core.content.FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(email))
                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Inspection Report — ${report.propertyAddress}")
                    putExtra(android.content.Intent.EXTRA_STREAM, uri)
                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(android.content.Intent.createChooser(intent, "Email Report"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
