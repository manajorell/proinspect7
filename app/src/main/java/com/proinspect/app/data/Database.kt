package com.proinspect.app.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
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
    suspend fun insertSettings(settings: AppSettings)
    
    @Update
    suspend fun updateSettings(settings: AppSettings)
}

@Dao
interface SerialDecodePatternDao {
    @Query("SELECT * FROM serial_decode_patterns WHERE LOWER(manufacturer) = LOWER(:mfg) ORDER BY priority DESC")
    suspend fun getPatternsForManufacturer(mfg: String): List<SerialDecodePattern>
    
    @Query("SELECT * FROM serial_decode_patterns ORDER BY manufacturer, priority DESC")
    suspend fun getAllPatterns(): List<SerialDecodePattern>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatterns(patterns: List<SerialDecodePattern>)
    
    @Query("DELETE FROM serial_decode_patterns")
    suspend fun deleteAll()
}

class Converters {
    @TypeConverter
    fun fromRating(value: Rating): String = value.name

    @TypeConverter
    fun toRating(value: String): Rating = 
        Rating.values().find { it.name == value } ?: Rating.NOT_RATED
}

// Migration from version 7 to 8 - adds payment fields and app_settings table
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add payment fields to reports table
        database.execSQL("ALTER TABLE reports ADD COLUMN inspectionAmount TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE reports ADD COLUMN inspectionService TEXT NOT NULL DEFAULT 'Standard Home Inspection'")
        database.execSQL("ALTER TABLE reports ADD COLUMN ancillaryServices TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE reports ADD COLUMN ancillaryAmount TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE reports ADD COLUMN paymentStatus TEXT NOT NULL DEFAULT 'Amount Due'")
        database.execSQL("ALTER TABLE reports ADD COLUMN paymentMethod TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE reports ADD COLUMN paymentNotes TEXT NOT NULL DEFAULT ''")
        
        // Create app_settings table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS app_settings (
                id INTEGER PRIMARY KEY NOT NULL,
                companyLogoPath TEXT NOT NULL DEFAULT '',
                badge1Path TEXT NOT NULL DEFAULT '',
                badge2Path TEXT NOT NULL DEFAULT '',
                badge3Path TEXT NOT NULL DEFAULT '',
                badge4Path TEXT NOT NULL DEFAULT '',
                anthropicApiKey TEXT NOT NULL DEFAULT '',
                ircState TEXT NOT NULL DEFAULT ''
            )
        """)
        
        // Insert default settings row
        database.execSQL("INSERT OR REPLACE INTO app_settings (id) VALUES (1)")
    }
}

@Database(
    entities = [
        Report::class, 
        InspectionItem::class, 
        InspectionPhoto::class, 
        AppSettings::class,
        SerialDecodePattern::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class) 
abstract class ProInspectDatabase : RoomDatabase() { 
    abstract fun reportDao(): ReportDao
    abstract fun inspectionItemDao(): InspectionItemDao
    abstract fun inspectionPhotoDao(): InspectionPhotoDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun serialDecodePatternDao(): SerialDecodePatternDao

    companion object {
        @Volatile private var INSTANCE: ProInspectDatabase? = null
        
        fun getInstance(context: Context): ProInspectDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ProInspectDatabase::class.java, "proinspect.db")
                    .addMigrations(MIGRATION_7_8)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insert default settings when database is created
                            db.execSQL("INSERT OR REPLACE INTO app_settings (id) VALUES (1)")
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
