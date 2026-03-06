package com.proinspect.app.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReport(id: Long): Report?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report): Long

    @Delete
    suspend fun deleteReport(report: Report)
}

@Dao
interface InspectionItemDao {
    @Query("SELECT * FROM inspection_items WHERE reportId = :reportId")
    fun getItemsForReport(reportId: Long): Flow<List<InspectionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InspectionItem)

    @Query("DELETE FROM inspection_items WHERE reportId = :reportId")
    suspend fun deleteItemsForReport(reportId: Long)
}

@Dao
interface InspectionPhotoDao {
    @Query("SELECT * FROM inspection_photos WHERE reportId = :reportId")
    fun getPhotosForReport(reportId: Long): Flow<List<InspectionPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: InspectionPhoto): Long

    @Delete
    suspend fun deletePhoto(photo: InspectionPhoto)

    @Query("DELETE FROM inspection_photos WHERE reportId = :reportId")
    suspend fun deletePhotosForReport(reportId: Long)
}

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettings(): Flow<AppSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: AppSettings)
}

@Database(
    entities = [Report::class, InspectionItem::class, InspectionPhoto::class, AppSettings::class],
    version = 2,
    exportSchema = false
)
abstract class ProInspectDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun inspectionItemDao(): InspectionItemDao
    abstract fun inspectionPhotoDao(): InspectionPhotoDao
    abstract fun appSettingsDao(): AppSettingsDao

    companion object {
        @Volatile private var INSTANCE: ProInspectDatabase? = null
        fun getInstance(context: Context): ProInspectDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ProInspectDatabase::class.java, "proinspect.db")
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
