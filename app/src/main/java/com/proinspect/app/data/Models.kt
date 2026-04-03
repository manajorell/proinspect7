package com.proinspect.app.data

import androidx.room.*

// ── Report Entity ──────────────────────────────────────────────────────────────
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
    val overviewNarrative: String = "",
    val limitations: String = "",
    
    // Section narratives
    val roofingNarrative: String = "",
    val exteriorNarrative: String = "",
    val structureNarrative: String = "",
    val electricalNarrative: String = "",
    val hvacNarrative: String = "",
    val plumbingNarrative: String = "",
    val interiorNarrative: String = "",
    val insulationNarrative: String = "",
    val garageNarrative: String = "",
    
    // Agreement paths
    val agreementPath: String = "",
    val signedAgreementPath: String = "",
    
    // Payment fields
    val inspectionService: String = "",
    val inspectionAmount: String = "",
    val ancillaryServices: String = "",
    val ancillaryAmount: String = "",
    val paymentStatus: String = "Amount Due",
    val paymentMethod: String = "",
    val paymentNotes: String = ""
)

// ── Inspection Item Entity ─────────────────────────────────────────────────────
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
    val reportId: Long,
    val itemId: String,
    val section: String,
    val rating: Rating = Rating.NOT_RATED,
    val narrative: String = ""
)

// ── Photo Entity ───────────────────────────────────────────────────────────────
@Entity(
    tableName = "photos",
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
    val reportId: Long,
    val section: String,
    val itemId: String? = null,
    val filePath: String,
    val timestamp: Long = System.currentTimeMillis()
)

// ── App Settings Entity ────────────────────────────────────────────────────────
@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val companyLogoPath: String = "",
    val badge1Path: String = "",
    val badge2Path: String = "",
    val badge3Path: String = "",
    val badge4Path: String = "",
    val anthropicApiKey: String = "",
    val ircState: String = "2021 IRC"  // NEW: IRC version
)

// ── Rating Enum ────────────────────────────────────────────────────────────────
enum class Rating(val label: String, val short: String) {
    NOT_RATED("Not Rated", "—"),
    GOOD("Good", "✓"),
    MONITOR("Monitor", "👁"),
    MAJOR("Major Concern", "⚠"),
    SAFETY("Safety Concern", "🚨"),
    NOT_PRESENT("Not Present", "N/A"),
    NOT_INSPECTED("Not Inspected", "N/I")
}

// ── Checklist Item ─────────────────────────────────────────────────────────────
data class ChecklistItem(
    val id: String,
    val title: String,
    val section: String
)

// ── IRC Code Data Class ────────────────────────────────────────────────────────
data class IrcCodeReference(
    val section: String,
    val code: String,
    val description: String
)

// ── IRC Codes Object ───────────────────────────────────────────────────────────
object IrcCodes {
    
    fun getAvailableVersions(): List<String> = listOf(
        "2021 IRC",
        "2018 IRC",
        "2015 IRC",
        "2012 IRC"
    )
    
    private val codes2021 = mapOf(
        "roofing" to IrcCodeReference(
            section = "Roofing",
            code = "R905",
            description = "Requirements for roof coverings including asphalt shingles, clay and concrete tile, metal roof panels, and other approved materials. Covers installation, underlayment, flashing, and drainage requirements."
        ),
        "exterior" to IrcCodeReference(
            section = "Exterior Walls",
            code = "R703",
            description = "Exterior covering requirements including weather-resistant barriers, water-resistive barriers, and exterior wall coverings. Addresses siding materials, stucco, masonry veneer, and proper installation methods."
        ),
        "structure" to IrcCodeReference(
            section = "Wall Construction",
            code = "R602",
            description = "Wood wall framing requirements including stud size, spacing, headers, and bracing. Covers structural integrity, load-bearing walls, and proper construction methods for wood-framed structures."
        ),
        "electrical" to IrcCodeReference(
            section = "Electrical",
            code = "E3901",
            description = "General electrical requirements including service equipment, branch circuits, grounding, GFCI and AFCI protection. References NEC (National Electrical Code) for detailed electrical installation standards."
        ),
        "hvac" to IrcCodeReference(
            section = "Mechanical",
            code = "M1401",
            description = "Heating and cooling equipment installation requirements including clearances, combustion air, venting, and duct systems. Covers furnaces, heat pumps, air conditioners, and ventilation systems."
        ),
        "plumbing" to IrcCodeReference(
            section = "Plumbing",
            code = "P2601",
            description = "Water supply and distribution requirements including pipe materials, sizing, water heaters, and fixtures. Covers potable water systems, backflow prevention, and proper installation methods."
        ),
        "interior" to IrcCodeReference(
            section = "Interior Finishes",
            code = "R702",
            description = "Interior wall and ceiling finish requirements including gypsum board, plaster, and other approved materials. Addresses fire resistance, moisture resistance, and proper installation."
        ),
        "insulation" to IrcCodeReference(
            section = "Energy Efficiency",
            code = "N1102",
            description = "Insulation and air sealing requirements for thermal envelope including walls, ceilings, floors, and foundations. Specifies minimum R-values and air barrier installation for energy efficiency."
        ),
        "garage" to IrcCodeReference(
            section = "Garages and Carports",
            code = "R309",
            description = "Garage construction requirements including fire separation from dwelling, vehicle door openings, and ventilation. Covers attached and detached garages, carports, and fire-resistance requirements."
        )
    )
    
    private val codes2018 = mapOf(
        "roofing" to IrcCodeReference(
            section = "Roofing",
            code = "R905",
            description = "Requirements for roof coverings including asphalt shingles, clay and concrete tile, metal roof panels, and other approved materials. Covers installation, underlayment, flashing, and drainage requirements. (2018 IRC)"
        ),
        "exterior" to IrcCodeReference(
            section = "Exterior Walls",
            code = "R703",
            description = "Exterior covering requirements including weather-resistant barriers, water-resistive barriers, and exterior wall coverings. Addresses siding materials, stucco, masonry veneer, and proper installation methods. (2018 IRC)"
        ),
        "structure" to IrcCodeReference(
            section = "Wall Construction",
            code = "R602",
            description = "Wood wall framing requirements including stud size, spacing, headers, and bracing. Covers structural integrity, load-bearing walls, and proper construction methods for wood-framed structures. (2018 IRC)"
        ),
        "electrical" to IrcCodeReference(
            section = "Electrical",
            code = "E3901",
            description = "General electrical requirements including service equipment, branch circuits, grounding, GFCI and AFCI protection. References NEC (National Electrical Code) for detailed electrical installation standards. (2018 IRC)"
        ),
        "hvac" to IrcCodeReference(
            section = "Mechanical",
            code = "M1401",
            description = "Heating and cooling equipment installation requirements including clearances, combustion air, venting, and duct systems. Covers furnaces, heat pumps, air conditioners, and ventilation systems. (2018 IRC)"
        ),
        "plumbing" to IrcCodeReference(
            section = "Plumbing",
            code = "P2601",
            description = "Water supply and distribution requirements including pipe materials, sizing, water heaters, and fixtures. Covers potable water systems, backflow prevention, and proper installation methods. (2018 IRC)"
        ),
        "interior" to IrcCodeReference(
            section = "Interior Finishes",
            code = "R702",
            description = "Interior wall and ceiling finish requirements including gypsum board, plaster, and other approved materials. Addresses fire resistance, moisture resistance, and proper installation. (2018 IRC)"
        ),
        "insulation" to IrcCodeReference(
            section = "Energy Efficiency",
            code = "N1102",
            description = "Insulation and air sealing requirements for thermal envelope including walls, ceilings, floors, and foundations. Specifies minimum R-values and air barrier installation for energy efficiency. (2018 IRC)"
        ),
        "garage" to IrcCodeReference(
            section = "Garages and Carports",
            code = "R309",
            description = "Garage construction requirements including fire separation from dwelling, vehicle door openings, and ventilation. Covers attached and detached garages, carports, and fire-resistance requirements. (2018 IRC)"
        )
    )
    
    private val codes2015 = mapOf(
        "roofing" to IrcCodeReference(
            section = "Roofing",
            code = "R905",
            description = "Requirements for roof coverings including asphalt shingles, clay and concrete tile, metal roof panels, and other approved materials. Covers installation, underlayment, flashing, and drainage requirements. (2015 IRC)"
        ),
        "exterior" to IrcCodeReference(
            section = "Exterior Walls",
            code = "R703",
            description = "Exterior covering requirements including weather-resistant barriers and exterior wall coverings. Addresses siding materials, stucco, masonry veneer, and proper installation methods. (2015 IRC)"
        ),
        "structure" to IrcCodeReference(
            section = "Wall Construction",
            code = "R602",
            description = "Wood wall framing requirements including stud size, spacing, headers, and bracing. Covers structural integrity and proper construction methods for wood-framed structures. (2015 IRC)"
        ),
        "electrical" to IrcCodeReference(
            section = "Electrical",
            code = "E3901",
            description = "General electrical requirements including service equipment, branch circuits, grounding, and GFCI protection. References NEC for detailed electrical installation standards. (2015 IRC)"
        ),
        "hvac" to IrcCodeReference(
            section = "Mechanical",
            code = "M1401",
            description = "Heating and cooling equipment installation requirements including clearances, combustion air, venting, and duct systems. (2015 IRC)"
        ),
        "plumbing" to IrcCodeReference(
            section = "Plumbing",
            code = "P2601",
            description = "Water supply and distribution requirements including pipe materials, sizing, water heaters, and fixtures. Covers potable water systems and backflow prevention. (2015 IRC)"
        ),
        "interior" to IrcCodeReference(
            section = "Interior Finishes",
            code = "R702",
            description = "Interior wall and ceiling finish requirements including gypsum board and other approved materials. Addresses fire resistance and proper installation. (2015 IRC)"
        ),
        "insulation" to IrcCodeReference(
            section = "Energy Efficiency",
            code = "N1102",
            description = "Insulation and air sealing requirements for thermalenvelope. Specifies minimum R-values for energy efficiency. (2015 IRC)"
        ),
        "garage" to IrcCodeReference(
            section = "Garages and Carports",
            code = "R309",
            description = "Garage construction requirements including fire separation from dwelling and ventilation. Covers attached and detached garages. (2015 IRC)"
        )
    )
    
    private val codes2012 = mapOf(
        "roofing" to IrcCodeReference(
            section = "Roofing",
            code = "R905",
            description = "Requirements for roof coverings including asphalt shingles, tile, metal panels, and other approved materials. Covers installation and flashing requirements. (2012 IRC)"
        ),
        "exterior" to IrcCodeReference(
            section = "Exterior Walls",
            code = "R703",
            description = "Exterior covering requirements including weather-resistant barriers and wall coverings. Addresses siding materials and proper installation. (2012 IRC)"
        ),
        "structure" to IrcCodeReference(
            section = "Wall Construction",
            code = "R602",
            description = "Wood wall framing requirements including stud size, spacing, and headers. Covers structural integrity for wood-framed structures. (2012 IRC)"
        ),
        "electrical" to IrcCodeReference(
            section = "Electrical",
            code = "E3901",
            description = "General electrical requirements including service equipment, branch circuits, and grounding. References NEC for electrical standards. (2012 IRC)"
        ),
        "hvac" to IrcCodeReference(
            section = "Mechanical",
            code = "M1401",
            description = "Heating and cooling equipment installation requirements including clearances and venting. (2012 IRC)"
        ),
        "plumbing" to IrcCodeReference(
            section = "Plumbing",
            code = "P2601",
            description = "Water supply and distribution requirements including pipe materials and fixtures. (2012 IRC)"
        ),
        "interior" to IrcCodeReference(
            section = "Interior Finishes",
            code = "R702",
            description = "Interior wall and ceiling finish requirements including gypsum board and approved materials. (2012 IRC)"
        ),
        "insulation" to IrcCodeReference(
            section = "Energy Efficiency",
            code = "N1102",
            description = "Insulation requirements for thermal envelope. Specifies minimum R-values. (2012 IRC)"
        ),
        "garage" to IrcCodeReference(
            section = "Garages and Carports",
            code = "R309",
            description = "Garage construction requirements including fire separation from dwelling. (2012 IRC)"
        )
    )
    
    fun getCode(version: String, section: String): IrcCodeReference? {
        val codeMap = when (version) {
            "2021 IRC" -> codes2021
            "2018 IRC" -> codes2018
            "2015 IRC" -> codes2015
            "2012 IRC" -> codes2012
            else -> codes2021
        }
        return codeMap[section]
    }
}

// ── Inspection Sections ────────────────────────────────────────────────────────
object InspectionSections {
    val sections = listOf(
        "roofing", "exterior", "structure", "electrical",
        "hvac", "plumbing", "interior", "insulation", "garage"
    )

    val sectionNames = mapOf(
        "roofing" to "Roofing",
        "exterior" to "Exterior",
        "structure" to "Structure",
        "electrical" to "Electrical",
        "hvac" to "HVAC",
        "plumbing" to "Plumbing",
        "interior" to "Interior",
        "insulation" to "Insulation",
        "garage" to "Garage"
    )

    val items = mapOf(
        "roofing" to listOf(
            ChecklistItem("rf1", "Roof Covering", "roofing"),
            ChecklistItem("rf2", "Roof Drainage System", "roofing"),
            ChecklistItem("rf3", "Flashings", "roofing"),
            ChecklistItem("rf4", "Skylights / Chimneys / Roof Penetrations", "roofing"),
            ChecklistItem("rf5", "Roof Structure & Attic", "roofing")
        ),
        "exterior" to listOf(
            ChecklistItem("ex1", "Wall Covering / Siding", "exterior"),
            ChecklistItem("ex2", "Doors", "exterior"),
            ChecklistItem("ex3", "Windows", "exterior"),
            ChecklistItem("ex4", "Trim / Eaves / Soffits / Fascias", "exterior"),
            ChecklistItem("ex5", "Grading & Drainage", "exterior"),
            ChecklistItem("ex6", "Walkways / Driveways / Patios", "exterior"),
            ChecklistItem("ex7", "Porches / Decks / Balconies / Railings", "exterior"),
            ChecklistItem("ex8", "Vegetation / Grading / Surface Drainage", "exterior")
        ),
        "structure" to listOf(
            ChecklistItem("st1", "Foundation", "structure"),
            ChecklistItem("st2", "Basement / Crawl Space", "structure"),
            ChecklistItem("st3", "Floor Structure", "structure"),
            ChecklistItem("st4", "Wall Structure", "structure"),
            ChecklistItem("st5", "Ceiling Structure", "structure"),
            ChecklistItem("st6", "Roof Structure", "structure")
        ),
        "electrical" to listOf(
            ChecklistItem("el1", "Service Entrance Conductors", "electrical"),
            ChecklistItem("el2", "Main Electrical Panel", "electrical"),
            ChecklistItem("el3", "Branch Circuit Conductors", "electrical"),
            ChecklistItem("el4", "Connected Devices & Fixtures", "electrical"),
            ChecklistItem("el5", "GFCI / AFCI Protection", "electrical"),
            ChecklistItem("el6", "Smoke & CO Detectors", "electrical")
        ),
        "hvac" to listOf(
            ChecklistItem("hv1", "Heating Equipment", "hvac"),
            ChecklistItem("hv2", "Cooling Equipment", "hvac"),
            ChecklistItem("hv3", "Duct Systems", "hvac"),
            ChecklistItem("hv4", "Vents / Flues / Chimneys", "hvac"),
            ChecklistItem("hv5", "Thermostat", "hvac")
        ),
        "plumbing" to listOf(
            ChecklistItem("pl1", "Water Supply System", "plumbing"),
            ChecklistItem("pl2", "Drain / Waste / Vent Systems", "plumbing"),
            ChecklistItem("pl3", "Water Heater", "plumbing"),
            ChecklistItem("pl4", "Fixtures & Faucets", "plumbing"),
            ChecklistItem("pl5", "Sump Pump", "plumbing")
        ),
        "interior" to listOf(
            ChecklistItem("in1", "Walls", "interior"),
            ChecklistItem("in2", "Ceilings", "interior"),
            ChecklistItem("in3", "Floors", "interior"),
            ChecklistItem("in4", "Doors", "interior"),
            ChecklistItem("in5", "Windows", "interior"),
            ChecklistItem("in6", "Stairs / Handrails / Guardrails", "interior"),
            ChecklistItem("in7", "Fireplace / Woodstove", "interior"),
            ChecklistItem("in8", "Kitchen Appliances", "interior"),
            ChecklistItem("in9", "Laundry Appliances", "interior")
        ),
        "insulation" to listOf(
            ChecklistItem("is1", "Attic Insulation", "insulation"),
            ChecklistItem("is2", "Wall Insulation", "insulation"),
            ChecklistItem("is3", "Floor Insulation", "insulation"),
            ChecklistItem("is4", "Ventilation", "insulation"),
            ChecklistItem("is5", "Vapor Retarders", "insulation")
        ),
        "garage" to listOf(
            ChecklistItem("gr1", "Garage Door & Opener", "garage"),
            ChecklistItem("gr2", "Garage Walls / Ceiling", "garage"),
            ChecklistItem("gr3", "Garage Floor", "garage"),
            ChecklistItem("gr4", "Fire Separation", "garage"),
            ChecklistItem("gr5", "Vehicle Exhaust System", "garage")
        )
    )

    val allItems: List<ChecklistItem> = items.values.flatten()
}
