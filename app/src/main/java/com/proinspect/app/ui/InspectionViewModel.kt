package com.proinspect.app.ui

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
        .map { it ?: AppSettings() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

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
            val report = Report(
                inspectionDate = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date())
            )
            val id = reportDao.insertReport(report)
            _currentReportId.value = id
            _navigateToReport.emit(Unit)
        }
    }

    fun loadReport(id: Long) {
        _currentReportId.value = id
    }

    fun saveReport(report: Report) {
        viewModelScope.launch { reportDao.insertReport(report) }
    }

    fun deleteReport(report: Report) {
        viewModelScope.launch {
            itemDao.deleteItemsForReport(report.id)
            photoDao.deletePhotosForReport(report.id)
            reportDao.deleteReport(report)
        }
    }

    fun setItemRating(itemId: String, section: String, rating: Rating) {
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val existing = items.value[itemId]
            val item = existing?.copy(rating = rating)
                ?: InspectionItem(reportId = reportId, itemId = itemId, section = section, rating = rating)
            itemDao.insertItem(item)
        }
    }

    fun setItemNarrative(itemId: String, section: String, narrative: String) {
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val existing = items.value[itemId]
            val item = existing?.copy(narrative = narrative)
                ?: InspectionItem(reportId = reportId, itemId = itemId, section = section, narrative = narrative)
            itemDao.insertItem(item)
        }
    }

    fun prepareCameraUri(context: Context, section: String, itemId: String?): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val photoFile = File(context.filesDir, "photo_${timestamp}.jpg")
        pendingPhotoPath = photoFile.absolutePath
        pendingSection = section
        pendingItemId = itemId
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    fun onPhotoCaptured(success: Boolean) {
        if (!success) return
        viewModelScope.launch {
            val reportId = _currentReportId.value ?: return@launch
            val path = pendingPhotoPath ?: return@launch
            photoDao.insertPhoto(
                InspectionPhoto(
                    reportId = reportId,
                    filePath = path,
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
            val destFile = File(context.filesDir, "gallery_${timestamp}.jpg")
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    destFile.outputStream().use { output -> input.copyTo(output) }
                }
                photoDao.insertPhoto(
                    InspectionPhoto(
                        reportId = reportId,
                        filePath = destFile.absolutePath,
                        section = section,
                        itemId = itemId
                    )
                )
            } catch (_: Exception) {}
        }
    }

    fun deletePhoto(photo: InspectionPhoto) {
        viewModelScope.launch {
            try { File(photo.filePath).delete() } catch (_: Exception) {}
            photoDao.deletePhoto(photo)
        }
    }

    fun saveSettings(settings: AppSettings) {
        viewModelScope.launch { settingsDao.saveSettings(settings) }
    }

    fun saveCompanyLogo(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val dest = File(context.filesDir, "company_logo.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    dest.outputStream().use { output -> input.copyTo(output) }
                }
                val current = appSettings.value
                settingsDao.saveSettings(current.copy(companyLogoPath = dest.absolutePath))
            } catch (_: Exception) {}
        }
    }

    fun saveBadge(context: Context, uri: Uri, slot: Int) {
        viewModelScope.launch {
            try {
                val dest = File(context.filesDir, "badge_${slot}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    dest.outputStream().use { output -> input.copyTo(output) }
                }
                val current = appSettings.value
                val updated = when (slot) {
                    1 -> current.copy(badge1Path = dest.absolutePath)
                    2 -> current.copy(badge2Path = dest.absolutePath)
                    3 -> current.copy(badge3Path = dest.absolutePath)
                    4 -> current.copy(badge4Path = dest.absolutePath)
                    else -> current
                }
                settingsDao.saveSettings(updated)
            } catch (_: Exception) {}
        }
    }

    fun clearBadge(slot: Int) {
        viewModelScope.launch {
            val current = appSettings.value
            val updated = when (slot) {
                1 -> current.copy(badge1Path = "")
                2 -> current.copy(badge2Path = "")
                3 -> current.copy(badge3Path = "")
                4 -> current.copy(badge4Path = "")
                else -> current
            }
            settingsDao.saveSettings(updated)
        }
    }
}
