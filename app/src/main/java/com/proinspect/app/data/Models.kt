package com.proinspect.app.data

import androidx.room.*

// ── IRC Code Data ─────────────────────────────────────────────────────────────

data class IrcCodeReference(
    val section: String,
    val code: String,
    val description: String
)

object IrcCodes {
    fun getAvailableVersions(): List<String> = listOf(
        "2021 IRC",
        "2018 IRC",
        "2015 IRC",
        "2012 IRC"
    )

    private val codes2021 = mapOf(
        "roofing" to IrcCodeReference(
            section = "R905",
            code = "R905.2",
            description = "Roof coverings shall be applied in accordance with the manufacturer's installation instructions. Asphalt shingles shall have a minimum of four fasteners per strip shingle or two fasteners per individual shingle."
        ),
        "exterior" to IrcCodeReference(
            section = "R703",
            code = "R703.1",
            description = "Exterior walls shall provide the building with a weather-resistant exterior wall envelope. The exterior wall envelope shall be designed and constructed in a manner that prevents the accumulation of water within the wall assembly."
        ),
        "structure" to IrcCodeReference(
            section = "R301",
            code = "R301.1",
            description = "Buildings and structures, and parts thereof, shall be constructed to safely support all loads, including dead loads, live loads, roof loads, flood loads, snow loads, wind loads and seismic loads as prescribed by this code."
        ),
        "electrical" to IrcCodeReference(
            section = "E3405",
            code = "E3405.1",
            description = "Panelboards shall be protected by an overcurrent protective device having a rating not greater than that of the panelboard. Panelboards shall be durably marked by the manufacturer with the voltage and the current rating."
        ),
        "hvac" to IrcCodeReference(
            section = "M1401",
            code = "M1401.3",
            description = "Heating and cooling equipment shall be sized in accordance with ACCA Manual S or other approved heating and cooling calculation methods. Required heating and cooling capacities shall be based on building loads calculated in accordance with ACCA Manual J or other approved heating and cooling calculation methodologies."
        ),
        "plumbing" to IrcCodeReference(
            section = "P2503",
            code = "P2503.5",
            description = "Water heaters shall be installed in accordance with the manufacturer's installation instructions. Temperature and pressure relief valves shall be installed in the openings provided by the manufacturer or in the hot water distribution line within 18 inches of the heater."
        ),
        "interior" to IrcCodeReference(
            section = "R302",
            code = "R302.5.1",
            description = "Openings from a private garage directly into a room used for sleeping purposes shall not be permitted. Other openings between the garage and residence shall be equipped with solid wood doors not less than 1-3/8 inches in thickness, solid or honeycomb core steel doors not less than 1-3/8 inches thick, or 20-minute fire-rated doors."
        ),
        "insulation" to IrcCodeReference(
            section = "N1102",
            code = "N1102.4.1",
            description = "The building thermal envelope shall be durably marked with an R-value identification mark. Blown or sprayed insulation shall be marked with the initial installed thickness, settled thickness, coverage area, and R-value."
        ),
        "garage" to IrcCodeReference(
            section = "R309",
            code = "R309.2",
            description = "Garages shall be separated from the residence and its attic area by not less than 1/2-inch gypsum board applied to the garage side. Garages beneath habitable rooms shall be separated from all habitable rooms above by not less than 5/8-inch Type X gypsum board."
        )
    )

    private val codes2018 = mapOf(
        "roofing" to IrcCodeReference(
            section = "R905",
            code = "R905.2.5",
            description = "Asphalt shingles shall be fastened to solidly sheathed decks. Fasteners shall be long enough to penetrate through the roofing materials and a minimum of 3/4 inch into the roof sheathing."
        ),
        "exterior" to IrcCodeReference(
            section = "R703",
            code = "R703.1",
            description = "Exterior walls shall provide the building with a weather-resistant exterior wall envelope. The exterior wall envelope shall include flashing as described in Section R703.8."
        ),
        "structure" to IrcCodeReference(
            section = "R301",
            code = "R301.2",
            description = "Buildings and portions thereof shall be constructed to sustain, within the limitations specified in this code, all loads set forth in Section R301.2 combined with earthquakes and extraordinary loads."
        ),
        "electrical" to IrcCodeReference(
            section = "E3405",
            code = "E3405.1",
            description = "All panelboards shall be protected on the supply side by overcurrent protective devices having a rating not greater than that of the panelboard."
        ),
        "hvac" to IrcCodeReference(
            section = "M1401",
            code = "M1401.3",
            description = "Heating and cooling equipment shall be sized based on building loads calculated in accordance with ACCA Manual J or other approved heating and cooling calculation methodologies."
        ),
        "plumbing" to IrcCodeReference(
            section = "P2503",
            code = "P2503.5",
            description = "Water heaters shall be installed in accordance with the manufacturer's installation instructions. Relief valves shall discharge through an air gap into the drainage system or outside the building."
        ),
        "interior" to IrcCodeReference(
            section = "R302",
            code = "R302.5",
            description = "The garage shall be separated from the residence and its attic area by not less than 1/2-inch gypsum board applied to the garage side."
        ),
        "insulation" to IrcCodeReference(
            section = "N1102",
            code = "N1102.4.1",
            description = "Insulation materials shall be installed per manufacturer specifications and building thermal envelope insulation shall be identified with R-value identification marks."
        ),
        "garage" to IrcCodeReference(
            section = "R309",
            code = "R309.1",
            description = "Private garages and carports shall comply with this section. Garages and carports shall be open on at least two sides."
        )
    )

    fun getCode(version: String, section: String): IrcCodeReference? {
        return when {
            version.contains("2021") -> codes2021[section]
            version.contains("2018") -> codes2018[section]
            version.contains("2015") -> codes2018[section] // Fallback to 2018
            version.contains("2012") -> codes2018[section] // Fallback to 2018
            else -> codes2021[section] // Default to 2021
        }
    }
}

// ── Report Entity ─────────────────────────────────────────────────────────────

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
    val roofingNarrative: String = "",
    val exteriorNarrative: String = "",
    val structureNarrative: String = "",
    val electricalNarrative: String = "",
    val hvacNarrative: String = "",
    val plumbingNarrative: String = "",
    val interiorNarrative: String = "",
    val insulationNarrative: String = "",
    val garageNarrative: String = "",
    val agreementPath: String = "",
    val signedAgreementPath: String = "",
    
    // Payment fields
    val inspectionService: String = "",
    val inspectionAmount: String = "",
    val ancillaryServices: String = "",
    val ancillaryAmount: String = "",
    val paymentStatus: String = "Amount Due",
    val paymentMethod: String = "",
    val paymentNotes: String = "",
    
    // Property details
    val propertyType: String = "",
    
    // Roof details
    val roofType: String = "",
    val roofAge: String = "",
    val roofMethod: String = "",
    
    // Exterior details
    val sidingType: String = "",
    val drivewayType: String = "",
    
    // Structure details
    val foundationType: String = "",
    val framingType: String = "",
    
    // Electrical details
    val panelBrand: String = "",
    val panelAmps: String = "",
    val panelType: String = "",
    val wiringType: String = "",
    val serviceEntrance: String = "",
    
    // HVAC details
    val heatType: String = "",
    val heatBrand: String = "",
    val heatAge: String = "",
    val acType: String = "",
    val acBrand: String = "",
    val acAge: String = "",
    val fuelType: String = "",
    val filterDate: String = "",
    
    // Plumbing details
    val supplyMaterial: String = "",
    val drainMaterial: String = "",
    val whType: String = "",
    val whAge: String = "",
    val whCapacity: String = "",
    
    // Insulation details
    val atticInsulation: String = "",
    val atticRValue: String = "",
    val crawlInsulation: String = "",
    
    // Garage details
    val garageType: String = "",
    val garageCars: String = "",
    
    val createdAt: Long = System.currentTimeMillis()
)

// ── Inspection Item Entity ────────────────────────────────────────────────────

package com.proinspect.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inspection_items")
data class InspectionItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportId: Long,
    val itemId: String,
    val section: String,
    val rating: Rating = Rating.NOT_RATED,
    val narrative: String = "",
    val label: String = ""  // ← ADD THIS PROPERTY
) {
    // Helper function to get display label
    fun getDisplayLabel(): String {
        return if (label.isNotEmpty()) label else itemId
    }
}

// ── Inspection Photo Entity ───────────────────────────────────────────────────

@Entity(tableName = "inspection_photos")
data class InspectionPhoto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long,
    val section: String,
    val itemId: String?,
    val photoPath: String,
    val timestamp: Long = System.currentTimeMillis()
)

// ── App Settings Entity ───────────────────────────────────────────────────────

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val companyLogoPath: String = "",
    val badge1Path: String = "",
    val badge2Path: String = "",
    val badge3Path: String = "",
    val badge4Path: String = "",
    val anthropicApiKey: String = "",
    val ircState: String = "2021 IRC"
)

// ── Checklist Data ────────────────────────────────────────────────────────────

data class ChecklistItem(
    val id: String,
    val title: String,
    val section: String
)

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
    
    val sectionIcons = mapOf(
        "roofing" to "🏠",
        "exterior" to "🏡",
        "structure" to "🏗️",
        "electrical" to "⚡",
        "hvac" to "🌡️",
        "plumbing" to "🚰",
        "interior" to "🛋️",
        "insulation" to "🧱",
        "garage" to "🚗"
    )

    val items = mapOf(
        "roofing" to listOf(
            ChecklistItem("rf1", "Roof Covering", "roofing"),
            ChecklistItem("rf2", "Roof Drainage System", "roofing"),
            ChecklistItem("rf3", "Flashings", "roofing"),
            ChecklistItem("rf4", "Skylights, Chimneys & Roof Penetrations", "roofing"),
            ChecklistItem("rf5", "Roof Structure & Attic", "roofing")
        ),
        "exterior" to listOf(
            ChecklistItem("ex1", "Wall Covering, Flashing & Trim", "exterior"),
            ChecklistItem("ex2", "Doors", "exterior"),
            ChecklistItem("ex3", "Windows", "exterior"),
            ChecklistItem("ex4", "Decks, Balconies, Stoops, Steps, Porches & Railings", "exterior"),
            ChecklistItem("ex5", "Eaves, Soffits & Fascias", "exterior"),
            ChecklistItem("ex6", "Grading & Drainage", "exterior"),
            ChecklistItem("ex7", "Driveways & Walkways", "exterior"),
            ChecklistItem("ex8", "Retaining Walls", "exterior")
        ),
        "structure" to listOf(
            ChecklistItem("st1", "Foundation", "structure"),
            ChecklistItem("st2", "Basement & Crawlspace", "structure"),
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
            ChecklistItem("el5", "GFCI & AFCI Protection", "electrical"),
            ChecklistItem("el6", "Smoke & CO Detectors", "electrical")
        ),
        "hvac" to listOf(
            ChecklistItem("hv1", "Heating Equipment", "hvac"),
            ChecklistItem("hv2", "Cooling Equipment", "hvac"),
            ChecklistItem("hv3", "Duct Systems", "hvac"),
            ChecklistItem("hv4", "Vents, Flues & Chimneys", "hvac"),
            ChecklistItem("hv5", "Thermostat", "hvac")
        ),
        "plumbing" to listOf(
            ChecklistItem("pl1", "Water Supply System & Fixtures", "plumbing"),
            ChecklistItem("pl2", "Drain, Waste & Vent Systems", "plumbing"),
            ChecklistItem("pl3", "Water Heater", "plumbing"),
            ChecklistItem("pl4", "Fuel Storage & Distribution", "plumbing"),
            ChecklistItem("pl5", "Sump Pump", "plumbing")
        ),
        "interior" to listOf(
            ChecklistItem("in1", "Walls, Ceilings & Floors", "interior"),
            ChecklistItem("in2", "Doors & Windows", "interior"),
            ChecklistItem("in3", "Stairs, Steps, Handrails & Guardrails", "interior"),
            ChecklistItem("in4", "Counters & Cabinets", "interior"),
            ChecklistItem("in5", "Plumbing Fixtures & Faucets", "interior"),
            ChecklistItem("in6", "Ventilation & Exhaust Systems", "interior")
        ),
        "insulation" to listOf(
            ChecklistItem("is1", "Attic Insulation", "insulation"),
            ChecklistItem("is2", "Wall Insulation", "insulation"),
            ChecklistItem("is3", "Floor Insulation", "insulation"),
            ChecklistItem("is4", "Vapor Retarders", "insulation"),
            ChecklistItem("is5", "Ventilation", "insulation")
        ),
        "garage" to listOf(
            ChecklistItem("gr1", "Garage Door & Opener", "garage"),
            ChecklistItem("gr2", "Garage Walls & Ceiling", "garage"),
            ChecklistItem("gr3", "Garage Floor", "garage"),
            ChecklistItem("gr4", "Garage Vehicle Door", "garage"),
            ChecklistItem("gr5", "Garage Electrical", "garage")
        )
    )

    val allItems = items.values.flatten()
}
