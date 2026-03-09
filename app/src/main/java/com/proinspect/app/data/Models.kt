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
// Data class for the UI checklist items
data class ChecklistItem(
    val id: String, 
    val title: String, 
    val description: String = ""
)

object InspectionSections {
    
    private val roofingItems = listOf(
        ChecklistItem("rf1", "Roof Surface", "Inspect shingles, tiles, or metal roofing"),
        ChecklistItem("rf2", "Flashings", "Check chimney, vent, and skylight flashing"),
        ChecklistItem("rf3", "Gutters/Downspouts", "Check gutters, downspouts, and drainage"),
        ChecklistItem("rf4", "Roof Structure", "Check for sagging or structural issues"),
        ChecklistItem("rf5", "Attic Ventilation", "Verify adequate ventilation")
    )
    
    private val exteriorItems = listOf(
        ChecklistItem("ex1", "Siding/Trim", "Inspect for damage, rot, or deterioration"),
        ChecklistItem("ex2", "Eaves/Soffits/Fascia", "Check for damage or pest entry"),
        ChecklistItem("ex3", "Windows/Doors", "Check operation, seals, and weatherstripping"),
        ChecklistItem("ex4", "Foundation & Grading", "Inspect for cracks or water issues"),
        ChecklistItem("ex5", "Decks & Porches", "Check structural integrity and safety")
    )
    
    private val structureItems = listOf(
        ChecklistItem("st1", "Foundation", "Check for cracks, bowing, or moisture"),
        ChecklistItem("st2", "Basement/Crawlspace", "Inspect for moisture, pests, or damage"),
        ChecklistItem("st3", "Floor Structure", "Check for sagging or rot"),
        ChecklistItem("st4", "Wall Framing", "Check for plumb and structural issues"),
        ChecklistItem("st5", "Stairs & Railings", "Verify code compliance and safety")
    )
    
    private val electricalItems = listOf(
        ChecklistItem("el1", "Main Panel", "Inspect service size, breakers, and labeling"),
        ChecklistItem("el2", "Outlets/Switches", "Test GFCI/AFCI protection"),
        ChecklistItem("el3", "Wiring", "Check for aluminum wiring or hazards"),
        ChecklistItem("el4", "Grounding", "Verify proper grounding system"),
        ChecklistItem("el5", "Smoke/CO Detectors", "Check presence and operation")
    )
    
    private val hvacItems = listOf(
        ChecklistItem("hv1", "Heating Equipment", "Inspect furnace or heat pump operation"),
        ChecklistItem("hv2", "Cooling Equipment", "Check AC unit and operation"),
        ChecklistItem("hv3", "Ductwork", "Inspect for leaks and insulation"),
        ChecklistItem("hv4", "Ventilation", "Check exhaust fans and fresh air"),
        ChecklistItem("hv5", "Thermostat", "Test operation and temperature control")
    )
    
    private val plumbingItems = listOf(
        ChecklistItem("pl1", "Main Water Shutoff", "Locate and test main shutoff valve"),
        ChecklistItem("pl2", "Visible Piping", "Check for leaks or corrosion"),
        ChecklistItem("pl3", "Water Heater", "Inspect age, operation, and TPR valve"),
        ChecklistItem("pl4", "Fixtures", "Test sinks, toilets, showers, and tubs"),
        ChecklistItem("pl5", "Drains & Vents", "Check for proper drainage and venting")
    )
    
    private val interiorItems = listOf(
        ChecklistItem("in1", "Walls/Ceilings/Floors", "Check for cracks or water damage"),
        ChecklistItem("in2", "Attic/Insulation", "Check insulation type and depth"),
        ChecklistItem("in3", "Interior Doors", "Check operation and hardware"),
        ChecklistItem("in4", "Windows", "Test operation and check for condensation"),
        ChecklistItem("in5", "Kitchen", "Inspect cabinets and appliances")
    )
    
    private val insulationItems = listOf(
        ChecklistItem("ins1", "Attic Insulation", "Check type, depth, and coverage"),
        ChecklistItem("ins2", "Wall Insulation", "Verify presence and adequacy"),
        ChecklistItem("ins3", "Crawlspace Insulation", "Inspect insulation and vapor barriers"),
        ChecklistItem("ins4", "Ventilation", "Check for adequate airflow"),
        ChecklistItem("ins5", "Moisture Control", "Look for signs of moisture problems")
    )
    
    private val garageItems = listOf(
        ChecklistItem("gr1", "Garage Door & Opener", "Test operation and safety sensors"),
        ChecklistItem("gr2", "Garage Structure", "Inspect walls, ceiling, and floor"),
        ChecklistItem("gr3", "Garage Electrical", "Check outlets and GFCI protection"),
        ChecklistItem("gr4", "Fire Separation", "Verify fire-rated walls and door"),
        ChecklistItem("gr5", "Garage Ventilation", "Check for adequate ventilation")
    )
    
    // Map with LOWERCASE keys (matching your code)
    val items: Map<String, List<ChecklistItem>> = mapOf(
        "roofing" to roofingItems,
        "exterior" to exteriorItems,
        "structure" to structureItems,
        "electrical" to electricalItems,
        "hvac" to hvacItems,
        "plumbing" to plumbingItems,
        "interior" to interiorItems,
        "insulation" to insulationItems,
        "garage" to garageItems
    )
    
    // List of section keys for iteration
    val sections: List<String> = listOf(
        "roofing",
        "exterior",
        "structure",
        "electrical",
        "hvac",
        "plumbing",
        "interior",
        "insulation",
        "garage"
    )
    
    // All items flattened for summary screen
    val allItems: List<ChecklistItem> = items.values.flatten()
    
    // Section display names
    val sectionNames = mapOf(
        "roofing" to "Roofing",
        "exterior" to "Exterior",
        "structure" to "Structure",
        "electrical" to "Electrical",
        "hvac" to "HVAC",
        "plumbing" to "Plumbing",
        "interior" to "Interior",
        "insulation" to "Insulation & Ventilation",
        "garage" to "Garage & Carport"
    )
    
    // Icons for PDF headers
    val sectionIcons = mapOf(
        "roofing" to "🏠",
        "exterior" to "🌳",
        "structure" to "🏗️",
        "electrical" to "⚡",
        "hvac" to "🌡️",
        "plumbing" to "💧",
        "interior" to "🛋️",
        "insulation" to "🧊",
        "garage" to "🚗"
    )
}
