package com.proinspect.app.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

class Converters {
    @TypeConverter fun fromRating(r: Rating): String = r.name
    @TypeConverter fun toRating(s: String): Rating = Rating.valueOf(s)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY updatedAt DESC")
    fun getAllReports(): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE id = :id")
    fun getReportFlow(id: Long): Flow<Report?>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): Report? = reportDao.getReportById(id)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report): Long

    @Update
    suspend fun updateReport(report: Report)

    @Delete
    suspend fun deleteReport(report: Report)
}

@Dao
interface InspectionItemDao {
    @Query("SELECT * FROM inspection_items WHERE reportId = :reportId")
    fun getItemsForReport(reportId: Long): Flow<List<InspectionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InspectionItem)
}

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE reportId = :reportId ORDER BY createdAt ASC")
    fun getPhotosForReport(reportId: Long): Flow<List<InspectionPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: InspectionPhoto): Long

    @Query("DELETE FROM photos WHERE id = :photoId")
    suspend fun deletePhotoById(photoId: Long)
}

@Database(
    entities = [Report::class, InspectionItem::class, InspectionPhoto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class InspectionDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun itemDao(): InspectionItemDao
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile private var INSTANCE: InspectionDatabase? = null
        fun getDatabase(context: Context): InspectionDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    InspectionDatabase::class.java,
                    "inspection_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

class InspectionRepository(private val db: InspectionDatabase) {
    val allReports = db.reportDao().getAllReports()

    fun getReportFlow(id: Long) = db.reportDao().getReportFlow(id)
    suspend fun createReport(report: Report): Long = db.reportDao().insertReport(report)
    suspend fun updateReport(report: Report) = db.reportDao().updateReport(report)
    suspend fun deleteReport(report: Report) = db.reportDao().deleteReport(report)

    fun getItemsForReport(reportId: Long) = db.itemDao().getItemsForReport(reportId)
    suspend fun saveItem(item: InspectionItem) = db.itemDao().insertItem(item)

    fun getPhotosForReport(reportId: Long) = db.photoDao().getPhotosForReport(reportId)
    suspend fun addPhoto(photo: InspectionPhoto): Long = db.photoDao().insertPhoto(photo)
    suspend fun deletePhoto(photoId: Long) = db.photoDao().deletePhotoById(photoId)

    companion object {
        @Volatile private var INSTANCE: InspectionRepository? = null
        fun getInstance(context: Context): InspectionRepository {
            return INSTANCE ?: synchronized(this) {
                InspectionRepository(
                    InspectionDatabase.getDatabase(context)
                ).also { INSTANCE = it }
            }
        }
    }
}
