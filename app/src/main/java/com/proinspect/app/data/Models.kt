package com.proinspect.app.data

import androidx.room.*

enum class Rating(val short: String) {
    SAFETY("Safety"),
    MAJOR("Major"),
    MONITOR("Monitor"),
    GOOD("Good"),
    NOT_RATED("N/R"),
    NOT_PRESENT("N/A")
}

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
        "roofing" to IrcCodeReference("R905", "R905.2", "Roof coverings shall be applied in accordance with the manufacturer's installation instructions. Asphalt shingles shall have a minimum of four fasteners per strip shingle or two fasteners per individual shingle."),
        "exterior" to IrcCodeReference("R703", "R703.1", "Exterior walls shall provide the building with a weather-resistant exterior wall envelope. The exterior wall envelope shall be designed and constructed in a manner that prevents the accumulation of water within the wall assembly."),
        "structure" to IrcCodeReference("R301", "R301.1", "Buildings and structures, and parts thereof, shall be constructed to safely support all loads, including dead loads, live loads, roof loads, flood loads, snow loads, wind loads and seismic loads as prescribed by this code."),
        "electrical" to IrcCodeReference("E3405", "E3405.1", "Panelboards shall be protected by an overcurrent protective device having a rating not greater than that of the panelboard. Panelboards shall be durably marked by the manufacturer with the voltage and the current rating."),
        "hvac" to IrcCodeReference("M1401", "M1401.3", "Heating and cooling equipment shall be sized in accordance with ACCA Manual S or other approved heating and cooling calculation methods. Required heating and cooling capacities shall be based on building loads calculated in accordance with ACCA Manual J or other approved heating and cooling calculation methodologies."),
        "plumbing" to IrcCodeReference("P2503", "P2503.5", "Water heaters shall be installed in accordance with the manufacturer's installation instructions. Temperature and pressure relief valves shall be installed in the openings provided by the manufacturer or in the hot water distribution line within 18 inches of the heater."),
        "interior" to IrcCodeReference("R302", "R302.5.1", "Openings from a private garage directly into a room used for sleeping purposes shall not be permitted. Other openings between the garage and residence shall be equipped with solid wood doors not less than 1-3/8 inches in thickness, solid or honeycomb core steel doors not less than 1-3/8 inches thick, or 20-minute fire-rated doors."),
        "insulation" to IrcCodeReference("N1102", "N1102.4.1", "The building thermal envelope shall be durably marked with an R-value identification mark. Blown or sprayed insulation shall be marked with the initial installed thickness, settled thickness, coverage area, and R-value."),
        "garage" to IrcCodeReference("R309", "R309.2", "Garages shall be separated from the residence and its attic area by not less than 1/2-inch gypsum board applied to the garage side. Garages beneath habitable rooms shall be separated from all habitable rooms above by not less than 5/8-inch Type X gypsum board.")
    )

    private val codes2018 = mapOf(
        "roofing" to IrcCodeReference("R905", "R905.2.5", "Asphalt shingles shall be fastened to solidly sheathed decks. Fasteners shall be long enough to penetrate through the roofing materials and a minimum of 3/4 inch into the roof sheathing."),
        "exterior" to IrcCodeReference("R703", "R703.1", "Exterior walls shall provide the building with a weather-resistant exterior wall envelope. The exterior wall envelope shall include flashing as described in Section R703.8."),
        "structure" to IrcCodeReference("R301", "R301.2", "Buildings and portions thereof shall be constructed to sustain, within the limitations specified in this code, all loads set forth in Section R301.2 combined with earthquakes and extraordinary loads."),
        "electrical" to IrcCodeReference("E3405", "E3405.1", "All panelboards shall be protected on the supply side by overcurrent protective devices having a rating not greater than that of the panelboard."),
        "hvac" to IrcCodeReference("M1401", "M1401.3", "Heating and cooling equipment shall be sized based on building loads calculated in accordance with ACCA Manual J or other approved heating and cooling calculation methodologies."),
        "plumbing" to IrcCodeReference("P2503", "P2503.5", "Water heaters shall be installed in accordance with the manufacturer's installation instructions. Relief valves shall discharge through an air gap into the drainage system or outside the building."),
        "interior" to IrcCodeReference("R302", "R302.5", "The garage shall be separated from the residence and its attic area by not less than 1/2-inch gypsum board applied to the garage side."),
        "insulation" to IrcCodeReference("N1102", "N1102.4.1", "Insulation materials shall be installed per manufacturer specifications and building thermal envelope insulation shall be identified with R-value identification marks."),
        "garage" to IrcCodeReference("R309", "R309.1", "Private garages and carports shall comply with this section. Garages and carports shall be open on at least two sides.")
    )

    fun getCode(version: String, section: String): IrcCodeReference? {
        return when {
            version.contains("2021") -> codes2021[section]
            version.contains("2018") -> codes2018[section]
            version.contains("2015") -> codes2018[section]
            version.contains("2012") -> codes2018[section]
            else -> codes2021[section]
        }
    }

    fun getCodeForItem(section: String, itemId: String, ircVersion: String): String {
        val itemCodes = mapOf(
            // ROOFING
            "rf1" to "R905 — Roof Coverings: Roof coverings shall be applied per manufacturer instructions. Asphalt shingles require minimum 4 fasteners per strip shingle.",
            "rf2" to "R903.4 — Roof Drainage: Roof drainage systems shall be designed to collect and discharge roof drainage.",
            "rf3" to "R903.2 — Flashings: Flashings shall be installed at wall and roof intersections, at gutters, and at all other locations where moisture could infiltrate.",
            "rf4" to "R903.2.1 — Skylights & Roof Penetrations: All penetrations through the roof deck shall be flashed and sealed.",
            "rf5" to "R802 — Roof Structure: Rafters shall be framed to ridge board or to each other with gusset plates.",
            "rf6" to "R1001 — Chimney: Masonry chimneys shall be constructed of solid masonry units. Chimney caps required. Flashing and counterflashing shall be installed at all roof penetrations. Clearance from combustibles required.",
            // EXTERIOR
            "ex1" to "R703 — Exterior Covering: Exterior walls shall provide a weather-resistant exterior wall envelope to prevent accumulation of water.",
            "ex2" to "R612 — Exterior Doors: Exterior doors shall be weathertight with proper flashing at head and sill.",
            "ex3" to "R609 — Exterior Windows: Windows shall be installed per manufacturer instructions with proper flashing.",
            "ex4" to "R507 — Exterior Decks: Decks shall be designed for minimum 40 psf live load. Guardrails required when deck is 30 inches above grade.",
            "ex5" to "R903.2.1 — Eaves, Soffits & Fascias: Eaves shall be properly ventilated and protected from moisture intrusion.",
            "ex6" to "R401.3 — Grading: The ground adjacent to the foundation shall be sloped away at 6 inches in 10 feet minimum.",
            "ex7" to "R309 — Driveways & Walkways: Driveways shall drain away from the structure to prevent water infiltration.",
            "ex8" to "R404 — Retaining Walls: Retaining walls shall be designed to resist lateral soil loads.",
            // STRUCTURE
            "st1" to "R403 — Foundation: Footings shall be sized for the load and soil conditions. Minimum depth below frost line required.",
            "st2" to "R408 — Crawlspace: Crawlspaces shall have 18 inch minimum clearance and adequate cross-ventilation of 1 sq ft per 150 sq ft of floor area.",
            "st3" to "R502 — Floor Structure: Floor joists shall be sized per span tables. Notching and boring limited to code allowances.",
            "st4" to "R602 — Wall Structure: Studs shall be continuous from foundation to roof. Load-bearing walls require proper headers.",
            "st5" to "R802.4 — Ceiling Structure: Ceiling joists shall be sized per span tables and properly connected to rafters.",
            "st6" to "R802 — Roof Structure: Roof framing shall be sized per span tables. Ridge board minimum 1-inch nominal thickness.",
            // ELECTRICAL
            "el1" to "E3601 — Service Entrance: Service entrance conductors shall have adequate ampacity. Minimum 100-amp service required for dwellings.",
            "el2" to "E3706 — Main Panel: Panelboards shall be protected by overcurrent devices not exceeding panel rating. Dead front required.",
            "el3" to "E3706 — Branch Circuits: Branch circuit conductors shall have ampacity not less than the maximum load served.",
            "el4" to "E3903 — Devices & Fixtures: All outlets, switches and fixtures shall be properly installed and grounded.",
            "el5" to "E3902 — GFCI & AFCI: GFCI protection required within 6 feet of sinks, bathrooms, garages, outdoors, crawlspaces. AFCI required for bedrooms.",
            "el6" to "R314 — Smoke Detectors: Required in each sleeping room, outside each sleeping area, and on each level. CO detectors required within 15 feet of sleeping rooms.",
            "el7" to "E3702 — Aluminum Wiring: Solid conductor aluminum branch circuit wiring requires special devices rated for aluminum or copper-clad aluminum. Connections must be made with anti-oxidant compound. All devices must be rated AL-CU.",
            "el8" to "E3601 — Service Amperage: Minimum 100-amp service required for single-family dwellings. 200-amp service recommended for modern homes with electric appliances. Service amperage must be verified at main disconnect.",
            // HVAC
            "hv1" to "M1401 — Heating Equipment: Heating equipment shall be sized per ACCA Manual J. Minimum efficiency standards apply.",
            "hv2" to "M1401.3 — Cooling Equipment: Cooling equipment shall be sized per ACCA Manual S. Condensate drain required.",
            "hv3" to "M1601 — Duct Systems: Ducts shall be properly sized, insulated, and sealed. Minimum R-8 insulation in unconditioned spaces.",
            "hv4" to "M1801 — Vents & Flues: Venting shall be sized and installed per appliance manufacturer instructions. Minimum clearances required.",
            "hv5" to "M1401.3 — Thermostat: At least one thermostat shall be provided for each separate heating/cooling system.",
            "hv6" to "M1401 — Heat Source Per Room: Each habitable room shall have a heat source capable of maintaining a minimum temperature of 68°F at 3 feet above floor and 2 feet from exterior walls.",
            // PLUMBING
            "pl1" to "P2903 — Water Supply: Water supply system shall be designed to provide adequate pressure and flow. Minimum 40 psi at fixtures.",
            "pl2" to "P3001 — Drain Waste & Vent: DWV systems shall be designed to prevent siphoning, back-pressure, and leakage.",
            "pl3" to "P2801 — Water Heater: TPR valve required and shall discharge to within 6 inches of floor. Seismic straps required in seismic zones.",
            "pl4" to "G2414 — Gas Distribution: Gas piping shall be properly sized, supported, and protected. Sediment trap required at each appliance.",
            "pl5" to "P3113 — Sump Pump: Sump pumps shall discharge to approved location. Check valve required on discharge line.",
            // INTERIOR
            "in1" to "R702 — Interior Covering: Wall and ceiling finishes shall comply with flame spread and smoke development requirements.",
            "in2" to "R612 — Interior Doors & Windows: Doors shall provide minimum 32-inch clear opening. Safety glazing required in hazardous locations.",
            "in3" to "R311.7 — Stairs & Handrails: Handrails required for stairs with 4 or more risers. Grip-able profile required. Height 34-38 inches.",
            "in4" to "R302.1 — Counters & Cabinets: Cabinets shall be properly secured. Countertops in wet areas shall be water-resistant.",
            "in5" to "P2701 — Plumbing Fixtures: Fixtures shall be properly installed, sealed, and have adequate water supply and drainage.",
            "in6" to "M1507 — Ventilation: Bathrooms shall have mechanical ventilation of minimum 50 cfm or openable window of 1.5 sq ft.",
            "in7" to "R1001 — Fireplace & Solid Fuel Systems: Fireplaces shall be constructed per manufacturer specifications. Hearth extension required. Damper required. Combustion air required. Clearance from combustibles shall be maintained.",
            // INSULATION
            "is1" to "N1102.1 — Attic Insulation: Minimum R-38 required in climate zone 4, R-49 in zones 5-8. Ventilation baffles required at eaves.",
            "is2" to "N1102.1 — Wall Insulation: Minimum R-13 required for wood frame walls in most climate zones.",
            "is3" to "N1102.1 — Floor Insulation: Minimum R-19 required for floors over unconditioned spaces in most climate zones.",
            "is4" to "R601.3 — Vapor Retarders: Class I or II vapor retarder required on warm-in-winter side of insulation in climate zones 5-8.",
            "is5" to "R806 — Ventilation: Attic ventilation ratio of 1:150 of floor area, or 1:300 with vapor retarder. Ridge and soffit vents recommended.",
            // GARAGE
            "gr1" to "R309.5 — Garage Door: Automatic garage door openers shall have auto-reverse and photo-eye safety features.",
            "gr2" to "R302.6 — Garage Walls & Ceiling: Walls and ceilings adjacent to living space require minimum 1/2-inch Type X gypsum board on garage side.",
            "gr3" to "R309 — Garage Floor: Garage floors shall be of approved noncombustible material and slope to drain.",
            "gr4" to "R302.5 — Garage Vehicle Door: Door between garage and residence shall be solid wood, solid steel, or 20-minute fire-rated.",
            "gr5" to "E3902 — Garage Electrical: All 125-volt, single-phase, 15 and 20-amp receptacles in garages require GFCI protection.",
            "gr6" to "R309.5 — Garage Door Auto-Reverse: Automatic garage door opener shall reverse within 2 seconds of contacting a 1.5-inch rigid object on the floor. Photo-eye sensors required and shall reverse door when beam is interrupted."
        )

        val baseCode = itemCodes[itemId] ?: "IRC code reference not available for this item."
        val versionNote = when {
            ircVersion.contains("2021") -> "\n\nIRC Version: 2021"
            ircVersion.contains("2018") -> "\n\nIRC Version: 2018"
            ircVersion.contains("2015") -> "\n\nIRC Version: 2015"
            ircVersion.contains("2012") -> "\n\nIRC Version: 2012"
            else -> "\n\nIRC Version: 2021"
        }
        return baseCode + versionNote
    }

    fun getCodesForSection(section: String, ircVersion: String): String {
        val sectionKey = section.lowercase()
        val codeRef = getCode(ircVersion, sectionKey)
        return if (codeRef != null) {
            """
            ${section.uppercase()} INSPECTION - IRC CODES
            
            Section: ${codeRef.section}
            Code: ${codeRef.code}
            
            ${codeRef.description}
            
            IRC Version: $ircVersion
            
            Note: This is a summary of key IRC requirements for ${section.lowercase()} inspection. 
            Always refer to the complete IRC code book for comprehensive requirements.
            """.trimIndent()
        } else {
            "IRC code information not available for this section."
        }
    }
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
    val inspectionService: String = "",
    val inspectionAmount: String = "",
    val ancillaryServices: String = "",
    val ancillaryAmount: String = "",
    val paymentStatus: String = "Amount Due",
    val paymentMethod: String = "",
    val paymentNotes: String = "",
    val propertyType: String = "",
    val roofType: String = "",
    val roofAge: String = "",
    val roofMethod: String = "",
    val sidingType: String = "",
    val drivewayType: String = "",
    val foundationType: String = "",
    val framingType: String = "",
    val panelBrand: String = "",
    val panelAmps: String = "",
    val panelType: String = "",
    val wiringType: String = "",
    val serviceEntrance: String = "",
    val heatType: String = "",
    val heatBrand: String = "",
    val heatAge: String = "",
    val acType: String = "",
    val acBrand: String = "",
    val acAge: String = "",
    val fuelType: String = "",
    val filterDate: String = "",
    val supplyMaterial: String = "",
    val drainMaterial: String = "",
    val whType: String = "",
    val whAge: String = "",
    val whCapacity: String = "",
    val atticInsulation: String = "",
    val atticRValue: String = "",
    val crawlInsulation: String = "",
    val garageType: String = "",
    val garageCars: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
@Entity(tableName = "inspection_items")
data class InspectionItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportId: Long,
    val itemId: String,
    val section: String,
    val rating: Rating = Rating.NOT_RATED,
    val narrative: String = "",
    val systemOperated: Boolean = false,
    val notInspectedReason: String = ""
)

@Entity(tableName = "inspection_photos")
data class InspectionPhoto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long,
    val section: String,
    val itemId: String?,
    val photoPath: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val companyLogoPath: String = "",
    val badge1Path: String = "",
    val badge2Path: String = "",
    val badge3Path: String = "",
    val badge4Path: String = "",
    val anthropicApiKey: String = "",
    val ircState: String = "2021 IRC",
    val inspectorName: String = "",
    val inspectorLicense: String = "",
    val inspectorCompany: String = "",
    val inspectorPhone: String = "",
    val inspectorEmail: String = ""
)

data class ChecklistItem(
    val id: String,
    val label: String,
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
            ChecklistItem("rf4", "Skylights & Roof Penetrations", "roofing"),
            ChecklistItem("rf5", "Roof Structure & Attic", "roofing"),
            ChecklistItem("rf6", "Chimney", "roofing")
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
            ChecklistItem("el6", "Smoke & CO Detectors", "electrical"),
            ChecklistItem("el7", "Aluminum Branch Circuit Wiring", "electrical"),
            ChecklistItem("el8", "Service Amperage", "electrical")
        ),
        "hvac" to listOf(
            ChecklistItem("hv1", "Heating Equipment", "hvac"),
            ChecklistItem("hv2", "Cooling Equipment", "hvac"),
            ChecklistItem("hv3", "Duct Systems", "hvac"),
            ChecklistItem("hv4", "Vents, Flues & Chimneys", "hvac"),
            ChecklistItem("hv5", "Thermostat", "hvac"),
            ChecklistItem("hv6", "Heat Source in Each Room", "hvac")
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
            ChecklistItem("in6", "Ventilation & Exhaust Systems", "interior"),
            ChecklistItem("in7", "Fireplace & Solid Fuel Burning Systems", "interior")
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
            ChecklistItem("gr5", "Garage Electrical", "garage"),
            ChecklistItem("gr6", "Garage Door Auto-Reverse Test", "garage")
        )
    )

    val allItems = items.values.flatten()
}
