package com.proinspect.app.data

import androidx.room.*

enum class Rating(val label: String, val short: String) {
    NOT_RATED("Not Rated", "NR"),
    GOOD("Good / Inspected", "✓"),
    MONITOR("Monitor", "MON"),
    MAJOR("Major Concern", "MAJ"),
    SAFETY("Safety Issue", "⚠")
}

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportNumber: String = "",
    val propertyAddress: String = "",
    val propertyCity: String = "",
    val clientName: String = "",
    val clientEmail: String = "",
    val inspectorName: String = "",
    val inspectorCert: String = "",
    val inspectorCompany: String = "",
    val inspectorPhone: String = "",
    val inspectionDate: String = "",
    val inspectionTime: String = "",
    val weatherConditions: String = "",
    val yearBuilt: String = "",
    val squareFootage: String = "",
    val propertyType: String = "Single Family",
    val limitations: String = "",
    val overviewNarrative: String = "",
    val roofType: String = "Asphalt Shingles",
    val roofAge: String = "",
    val roofMethod: String = "Walked",
    val roofingNarrative: String = "",
    val sidingType: String = "Vinyl",
    val drivewayType: String = "Concrete",
    val exteriorNarrative: String = "",
    val foundationType: String = "Poured Concrete",
    val framingType: String = "Wood Frame",
    val structureNarrative: String = "",
    val panelBrand: String = "",
    val panelAmps: String = "200 Amp",
    val panelType: String = "Circuit Breaker",
    val wiringType: String = "Copper",
    val serviceEntrance: String = "Overhead",
    val electricalNarrative: String = "",
    val heatType: String = "Gas Forced Air",
    val heatBrand: String = "",
    val heatAge: String = "",
    val acType: String = "Central AC",
    val acBrand: String = "",
    val acAge: String = "",
    val fuelType: String = "Natural Gas",
    val filterDate: String = "",
    val hvacNarrative: String = "",
    val supplyMaterial: String = "Copper",
    val drainMaterial: String = "PVC",
    val whType: String = "Tank — Gas",
    val whAge: String = "",
    val whCapacity: String = "",
    val plumbingNarrative: String = "",
    val interiorNarrative: String = "",
    val atticInsulation: String = "Fiberglass Batt",
    val atticRValue: String = "",
    val crawlInsulation: String = "None",
    val insulationNarrative: String = "",
    val garageType: String = "Attached",
    val garageCars: String = "2 Car",
    val garageNarrative: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val agreementSentPath: String = "",
    val signedAgreementPath: String = ""
)

@Entity(
    tableName = "inspection_items",
    foreignKeys = [ForeignKey(
        entity = Report::class,
        parentColumns = ["id"],
        childColumns = ["reportId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("reportId")]
)
data class InspectionItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long = 0,
    val itemId: String = "",
    val section: String = "",
    val ratingName: String = Rating.NOT_RATED.name,
    val narrative: String = ""
)

val InspectionItem.rating: Rating
    get() = Rating.entries.find { it.name == ratingName } ?: Rating.NOT_RATED {
    @Ignore
    val rating: Rating get(); = Rating.values().find { it.name == ratingName } ?: Rating.NOT_RATED
}
}

@Entity(
    tableName = "inspection_photos",
    foreignKeys = [ForeignKey(
        entity = Report::class,
        parentColumns = ["id"],
        childColumns = ["reportId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("reportId")]
)
data class InspectionPhoto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long = 0,
    val itemId: String? = null,   // nullable — null means it's a section-level photo
    val section: String = "",     // added: needed for section-level photo filtering
    val filePath: String = "",
    val caption: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class ChecklistItem(val id: String, val title: String, val section: String)

// ... keep existing code (InspectionSections object - unchanged) ...

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val companyLogoPath: String = "",
    val badge1Path: String = "",
    val badge2Path: String = "",
    val badge3Path: String = "",
    val badge4Path: String = "",
    val anthropicApiKey: String = ""
)
