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
    val signedAgreementPath: String = "",
    
        // NEW: Payment fields
    val inspectionAmount: String = "",
    val inspectionService: String = "Standard Home Inspection",
    val ancillaryServices: String = "",
    val ancillaryAmount: String = "",
    val paymentStatus: String = "Amount Due",  // "Paid" or "Amount Due"
    val paymentMethod: String = "",  // "Cash", "Check", "Credit Card", "Zelle", etc.
    val paymentNotes: String = ""
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
    @PrimaryKey val id: Int = 1,  // ← Add this line
    val companyLogoPath: String = "",
    val badge1Path: String = "",
    val badge2Path: String = "",
    val badge3Path: String = "",
    val badge4Path: String = "",
    val anthropicApiKey: String = "",
    val ircState: String = ""
)

// Data class for the UI checklist items
data class ChecklistItem(
    val id: String, 
    val title: String, 
    val description: String = ""
)

object InspectionSections {
    
       val items: Map<String, List<ChecklistItem>> = mapOf(
        "roofing" to listOf(
            ChecklistItem("rf1", "Roof Covering — Shingles/Surface Material"),
            ChecklistItem("rf2", "Flashing (Chimney, Valleys, Walls, Penetrations)"),
            ChecklistItem("rf3", "Ridge Cap"),
            ChecklistItem("rf4", "Gutters & Downspouts"),
            ChecklistItem("rf5", "Chimney (Masonry, Cap, Flashing)"),
            ChecklistItem("rf6", "Skylights & Roof Penetrations"),
            ChecklistItem("rf7", "Fascia & Soffit"),
            ChecklistItem("rf8", "Ventilation (Ridge, Soffit, Attic Vents)"),
            ChecklistItem("rf9", "Evidence of Ponding Water / Ice Dams")
        ),
        "exterior" to listOf(
            ChecklistItem("ex1", "Wall Cladding / Siding"),
            ChecklistItem("ex2", "Trim & Exterior Finishes"),
            ChecklistItem("ex3", "Exterior Windows & Frames"),
            ChecklistItem("ex4", "Exterior Doors"),
            ChecklistItem("ex5", "Deck / Porch / Patio"),
            ChecklistItem("ex6", "Stairs, Handrails & Guards (Exterior)"),
            ChecklistItem("ex7", "Grading & Drainage"),
            ChecklistItem("ex8", "Walkways & Driveways"),
            ChecklistItem("ex9", "Vegetation / Landscape Concerns"),
            ChecklistItem("ex10", "Retaining Walls & Site Features")
        ),
        "structure" to listOf(
            ChecklistItem("st1", "Foundation Walls"),
            ChecklistItem("st2", "Floor Framing / Joists / Girders"),
            ChecklistItem("st3", "Columns & Piers"),
            ChecklistItem("st4", "Roof Framing / Rafters / Trusses"),
            ChecklistItem("st5", "Wall Framing"),
            ChecklistItem("st6", "Evidence of Settlement / Movement"),
            ChecklistItem("st7", "Evidence of Water Intrusion"),
            ChecklistItem("st8", "Crawlspace Conditions"),
            ChecklistItem("st9", "Basement (if applicable)")
        ),
        "electrical" to listOf(
            ChecklistItem("el1", "Service Entry & Conductors"),
            ChecklistItem("el2", "Main Electrical Panel"),
            ChecklistItem("el3", "Sub-Panel(s)"),
            ChecklistItem("el4", "Branch Circuit Wiring"),
            ChecklistItem("el5", "GFCI Protection"),
            ChecklistItem("el6", "AFCI Protection"),
            ChecklistItem("el7", "Outlets / Receptacles"),
            ChecklistItem("el8", "Switches & Fixtures"),
            ChecklistItem("el9", "Smoke & CO Detectors"),
            ChecklistItem("el10", "Grounding & Bonding"),
            ChecklistItem("el11", "Aluminum Branch Circuit Wiring")
        ),
        "hvac" to listOf(
            ChecklistItem("hv1", "Heating System Operation"),
            ChecklistItem("hv2", "Cooling System Operation"),
            ChecklistItem("hv3", "Ductwork & Distribution"),
            ChecklistItem("hv4", "Flue / Venting / Combustion Air"),
            ChecklistItem("hv5", "Thermostat"),
            ChecklistItem("hv6", "Filter Condition"),
            ChecklistItem("hv7", "Gas Connections"),
            ChecklistItem("hv8", "Fireplace / Wood Stove"),
            ChecklistItem("hv9", "Kitchen Exhaust Fan"),
            ChecklistItem("hv10", "Bath Exhaust Fans")
        ),
       "plumbing" to listOf(
            ChecklistItem("pl1", "Water Supply Lines"),
            ChecklistItem("pl2", "Drain, Waste & Vent System"),
            ChecklistItem("pl3", "Water Heater"),
            ChecklistItem("pl4", "Kitchen Plumbing & Fixtures"),
            ChecklistItem("pl5", "Bathroom 1 — Fixtures & Drains"),
            ChecklistItem("pl6", "Bathroom 2 — Fixtures & Drains"),
            ChecklistItem("pl7", "Bathroom 3 — Fixtures & Drains"),
            ChecklistItem("pl8", "Laundry / Utility Connections"),
            ChecklistItem("pl9", "Main Water Shut-Off Valve"),
            ChecklistItem("pl10", "Sump Pump"),
            ChecklistItem("pl11", "Exterior Hose Bibs"),
            ChecklistItem("pl12", "Dishwasher"),
            ChecklistItem("pl13", "Garbage Disposal")
                ),
     "interior" to listOf(
            ChecklistItem("in1", "Interior Walls & Ceilings"),
            ChecklistItem("in2", "Interior Floors"),
            ChecklistItem("in3", "Interior Windows & Sills"),
            ChecklistItem("in4", "Interior Doors & Hardware"),
            ChecklistItem("in5", "Stairs, Handrails & Guardrails"),
            ChecklistItem("in6", "Kitchen — Cabinets, Counters & Appliances"),
            ChecklistItem("in7", "Bathrooms — Tile, Caulk & Waterproofing"),
            ChecklistItem("in8", "Smoke Detectors"),
            ChecklistItem("in9", "CO Detectors"),
            ChecklistItem("in10", "Attic Access & Hatch"),
            ChecklistItem("in11", "Range / Oven / Cooktop"),
            ChecklistItem("in12", "Built-in Microwave / Exhaust Hood")
                    
        ),
        "insulation" to listOf(
            ChecklistItem("is1", "Attic Insulation"),
            ChecklistItem("is2", "Attic Ventilation"),
            ChecklistItem("is3", "Crawlspace / Basement Insulation"),
            ChecklistItem("is4", "Vapor Barrier"),
            ChecklistItem("is5", "Wall Insulation (visible)"),
            ChecklistItem("is6", "Exhaust Fan Terminations")
        ),
        "garage" to listOf(
            ChecklistItem("ga1", "Garage Door Operation & Safety Reverse"),
            ChecklistItem("ga2", "Garage Door Opener"),
            ChecklistItem("ga3", "Fire-Rated Door to Living Space"),
            ChecklistItem("ga4", "Garage Floor"),
            ChecklistItem("ga5", "Garage Walls & Ceiling"),
            ChecklistItem("ga6", "Overhead Gas Heater"),
            ChecklistItem("ga7", "Attic Access in Garage")
        )
    )

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

    val allItems: List<ChecklistItem> = items.values.flatten()

    val sectionNames = mapOf(
        "roofing" to "Roofing System",
        "exterior" to "Exterior",
        "structure" to "Structural Components",
        "electrical" to "Electrical System",
        "hvac" to "HVAC System",
        "plumbing" to "Plumbing System",
        "interior" to "Interior",
        "insulation" to "Insulation & Ventilation",
        "garage" to "Garage"
    )

    val sectionIcons = mapOf(
        "roofing" to "🏠",
        "exterior" to "🧱",
        "structure" to "🏗️",
        "electrical" to "⚡",
        "hvac" to "🌡️",
        "plumbing" to "🔧",
        "interior" to "🪟",
        "insulation" to "🌿",
        "garage" to "🚗"
    )
    // IRC Code Reference System
data class IrcCode(
    val section: String,
    val code: String,
    val description: String
)

object IrcCodes {
    private val codes = mapOf(
        "2021 IRC" to mapOf(
            "roofing" to IrcCode("R905", "R905.2", "Asphalt shingles shall be installed per manufacturer specifications with proper fastening and underlayment"),
            "exterior" to IrcCode("R703", "R703.1", "Exterior walls shall provide weather protection for the building"),
            "structure" to IrcCode("R301", "R301.1", "Buildings shall be designed to support all applicable loads per the code"),
            "electrical" to IrcCode("E3401", "E3401.1", "Service equipment shall be listed, labeled and installed per manufacturer instructions"),
            "hvac" to IrcCode("M1401", "M1401.3", "Heating and cooling equipment shall be sized per approved heating and cooling calculation (Manual J)"),
            "plumbing" to IrcCode("P2603", "P2603.2", "Water supply systems shall be sized for the demand per fixture unit calculations"),
            "interior" to IrcCode("R302", "R302.1", "Fire-resistance-rated construction required between dwelling units and certain spaces"),
            "insulation" to IrcCode("N1102", "N1102.1", "Insulation shall meet minimum thermal resistance (R-value) requirements for climate zone"),
            "garage" to IrcCode("R309", "R309.1", "Garage separation from dwelling requires fire-rated construction and self-closing doors")
        ),
        "2018 IRC" to mapOf(
            "roofing" to IrcCode("R905", "R905.2", "Asphalt shingles installation requirements per manufacturer specifications"),
            "exterior" to IrcCode("R703", "R703.1", "Exterior covering shall protect walls from weather"),
            "structure" to IrcCode("R301", "R301.1", "Design criteria - buildings shall withstand design loads"),
            "electrical" to IrcCode("E3401", "E3401.1", "Service equipment general requirements and listing"),
            "hvac" to IrcCode("M1401", "M1401.3", "Equipment sizing and capacity requirements"),
            "plumbing" to IrcCode("P2603", "P2603.2", "Water supply system sizing per fixture units"),
            "interior" to IrcCode("R302", "R302.1", "Fire-resistance rating requirements for dwelling separations"),
            "insulation" to IrcCode("N1102", "N1102.1", "Insulation and fenestration criteria by climate zone"),
            "garage" to IrcCode("R309", "R309.1", "Garage separation requirements from dwelling")
        ),
        "2015 IRC" to mapOf(
            "roofing" to IrcCode("R905", "R905.2", "Asphalt shingles - material standards and installation"),
            "exterior" to IrcCode("R703", "R703.1", "Exterior covering and weather protection"),
            "structure" to IrcCode("R301", "R301.1", "Design loads and structural requirements"),
            "electrical" to IrcCode("E3401", "E3401.1", "Service equipment installation and listing"),
            "hvac" to IrcCode("M1401", "M1401.3", "Heating and cooling equipment requirements"),
            "plumbing" to IrcCode("P2603", "P2603.2", "Water supply system sizing"),
            "interior" to IrcCode("R302", "R302.1", "Fire resistance rated construction"),
            "insulation" to IrcCode("N1102", "N1102.1", "Insulation requirements by climate zone"),
            "garage" to IrcCode("R309", "R309.1", "Garage separation from residence")
        ),
        "2012 IRC" to mapOf(
            "roofing" to IrcCode("R905", "R905.2", "Asphalt shingles requirements"),
            "exterior" to IrcCode("R703", "R703.1", "Exterior covering"),
            "structure" to IrcCode("R301", "R301.1", "Design loads"),
            "electrical" to IrcCode("E3401", "E3401.1", "Service equipment"),
            "hvac" to IrcCode("M1401", "M1401.3", "Equipment requirements"),
            "plumbing" to IrcCode("P2603", "P2603.2", "Water supply sizing"),
            "interior" to IrcCode("R302", "R302.1", "Fire resistance"),
            "insulation" to IrcCode("N1102", "N1102.1", "Insulation requirements"),
            "garage" to IrcCode("R309", "R309.1", "Garage separation")
        )
    )
    
    fun getCode(ircVersion: String, section: String): IrcCode? {
        return codes[ircVersion]?.get(section)
    }
    
    fun getAvailableVersions(): List<String> = listOf("2021 IRC", "2018 IRC", "2015 IRC", "2012 IRC")
}

// Add serial decode pattern entity
@Entity(tableName = "serial_decode_patterns")
data class SerialDecodePattern(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val manufacturer: String = "",
    val pattern: String = "",
    val yearGroup: Int = 0,
    val monthGroup: Int = 0,
    val priority: Int = 0
)

}

