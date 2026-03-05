package com.proinspect.app.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proinspect.app.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class InspectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = InspectionRepository.getInstance(application)

    val allReports: StateFlow<List<Report>> = repo.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentReportId = MutableStateFlow<Long?>(null)

    val currentReport: StateFlow<Report?> = _currentReportId
        .flatMapLatest { id -> if (id == null) flowOf(null) else repo.getReportFlow(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val items: StateFlow<Map<String, InspectionItem>> = _currentReportId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repo.getItemsForReport(id) }
        .map { list -> list.associateBy { it.itemId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val photos: StateFlow<List<InspectionPhoto>> = _currentReportId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repo.getPhotosForReport(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _navigateToReport = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateToReport: SharedFlow<Unit> = _navigateToReport

    private var tempPhotoPath: String? = null
    private var tempPhotoSection: String = ""
    private var tempPhotoItemId: String? = null

    fun loadReport(reportId: Long) {
        _currentReportId.value = reportId
    }

    fun createNewReport() {
        viewModelScope.launch {
            val report = Report(
                reportNumber = "RPT-${SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())}-${(1000..9999).random()}",
                inspectionDate = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date())
            )
            val newId = repo.createReport(report)
            _currentReportId.value = newId
            _navigateToReport.emit(Unit)
        }
    }

    fun saveReport(report: Report) {
        viewModelScope.launch {
            repo.updateReport(report.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteReport(report: Report) {
        viewModelScope.launch { repo.deleteReport(report) }
    }

    fun setItemRating(itemId: String, section: String, rating: Rating) {
        val reportId = _currentReportId.value ?: return
        viewModelScope.launch {
            val existing = items.value[itemId]
            val item = existing?.copy(rating = rating)
                ?: InspectionItem(itemId = itemId, reportId = reportId, rating = rating, section = section)
            repo.saveItem(item)
        }
    }

    fun setItemNarrative(itemId: String, section: String, narrative: String) {
        val reportId = _currentReportId.value ?: return
        viewModelScope.launch {
            val existing = items.value[itemId]
            val item = existing?.copy(narrative = narrative)
                ?: InspectionItem(itemId = itemId, reportId = reportId, narrative = narrative, section = section)
            repo.saveItem(item)
        }
    }

    fun prepareCameraUri(context: Context, section: String, itemId: String?): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
        tempPhotoPath = file.absolutePath
        tempPhotoSection = section
        tempPhotoItemId = itemId
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun onPhotoCaptured(success: Boolean) {
        if (!success) return
        val reportId = _currentReportId.value ?: return
        val path = tempPhotoPath ?: return
        viewModelScope.launch {
            repo.addPhoto(
                InspectionPhoto(
                    reportId = reportId,
                    itemId = tempPhotoItemId,
                    section = tempPhotoSection,
                    filePath = path
                )
            )
        }
    }

    fun addPhotoFromGallery(context: Context, uri: Uri, section: String, itemId: String?) {
        val reportId = _currentReportId.value ?: return
        viewModelScope.launch {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val dest = File(storageDir, "IMG_${timeStamp}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            }
            repo.addPhoto(
                InspectionPhoto(
                    reportId = reportId,
                    itemId = itemId,
                    section = section,
                    filePath = dest.absolutePath
                )
            )
        }
    }

    fun deletePhoto(photoId: Long) {
        viewModelScope.launch { repo.deletePhoto(photoId) }
    }
}
