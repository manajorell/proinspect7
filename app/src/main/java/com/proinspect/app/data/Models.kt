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

// FIX: This extension property MUST be outside the class and NOT have a trailing {
val InspectionItem.rating: Rating
    get() = Rating.entries.find { it.name == ratingName } ?: Rating.NOT_RATED

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
    val itemId: String? = null,
    val section: String = "",
    val filePath: String = "",
    val caption: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
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
// Data class for the UI checklist items
data class ChecklistItem(
    val id: String, 
    val title: String, 
    val section: String
)

object InspectionSections {
    // renamed from 'all' to 'allItems' to satisfy PdfGenerator and InspectionScreens
    val allItems = listOf(
        ChecklistItem("rf1", "Roof Surface", "Roofing"),
        ChecklistItem("rf2", "Flashings", "Roofing"),
        ChecklistItem("rf3", "Gutters/Downspouts", "Roofing"),
        ChecklistItem("ex1", "Siding/Trim", "Exterior"),
        ChecklistItem("ex2", "Eaves/Soffits/Fascia", "Exterior"),
        ChecklistItem("ex3", "Windows/Doors", "Exterior"),
        ChecklistItem("st1", "Foundation", "Structure"),
        ChecklistItem("st2", "Basement/Crawlspace", "Structure"),
        ChecklistItem("pl1", "Main Water Shutoff", "Plumbing"),
        ChecklistItem("pl2", "Visible Piping", "Plumbing"),
        ChecklistItem("pl3", "Water Heater", "Plumbing"),
        ChecklistItem("el1", "Main Panel", "Electrical"),
        ChecklistItem("el2", "Outlets/Switches", "Electrical"),
        ChecklistItem("hv1", "Heating Equipment", "HVAC"),
        ChecklistItem("hv2", "Cooling Equipment", "HVAC"),
        ChecklistItem("in1", "Walls/Ceilings/Floors", "Interior"),
        ChecklistItem("in2", "Attic/Insulation", "Interior")
    )

    // These lines satisfy the "Unresolved reference: sections/sectionNames" errors
    val sections = allItems.groupBy { it.section }
    val sectionNames = allItems.map { it.section }.distinct()
    
    // This satisfies the "Unresolved reference: sectionIcons" error in PDF
    val sectionIcons = mapOf(
        "Roofing" to "🏠",
        "Exterior" to "🌳",
        "Structure" to "🏗️",
        "Plumbing" to "💧",
        "Electrical" to "⚡",
        "HVAC" to "🌡️",
        "Interior" to "🛋️"
    )
}
