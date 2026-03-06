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
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "inspection_items", foreignKeys = [
    ForeignKey(entity = Report::class, parentColumns = ["id"], childColumns = ["reportId"], onDelete = ForeignKey.CASCADE)
])
data class InspectionItem(
    @PrimaryKey val itemId: String,
    val reportId: Long,
    val rating: Rating = Rating.NOT_RATED,
    val narrative: String = "",
    val section: String = ""
)

@Entity(tableName = "photos", foreignKeys = [
    ForeignKey(entity = Report::class, parentColumns = ["id"], childColumns = ["reportId"], onDelete = ForeignKey.CASCADE)
])
@Entity(tableName = "inspection_photos")
data class InspectionPhoto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long,
    val filePath: String,
    val section: String,
    val itemId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChecklistItem(val id: String, val title: String, val section: String)

object InspectionSections {
    val sections = listOf("roofing","exterior","structure","electrical","hvac","plumbing","interior","insulation","garage")
    val sectionNames = mapOf(
        "roofing" to "Roofing System", "exterior" to "Exterior",
        "structure" to "Structural Components", "electrical" to "Electrical System",
        "hvac" to "HVAC System", "plumbing" to "Plumbing System",
        "interior" to "Interior", "insulation" to "Insulation & Ventilation",
        "garage" to "Garage"
    )
    val sectionIcons = mapOf(
        "roofing" to "🏠", "exterior" to "🧱", "structure" to "🏗️",
        "electrical" to "⚡", "hvac" to "🌡️", "plumbing" to "🔧",
        "interior" to "🪟", "insulation" to "🌿", "garage" to "🚗"
    )
    val items = mapOf(
        "roofing" to listOf(
            ChecklistItem("rf1","Roof Covering — Shingles/Surface Material","roofing"),
            ChecklistItem("rf2","Flashing (Chimney, Valleys, Walls, Penetrations)","roofing"),
            ChecklistItem("rf3","Ridge Cap","roofing"),
            ChecklistItem("rf4","Gutters & Downspouts","roofing"),
            ChecklistItem("rf5","Chimney (Masonry, Cap, Flashing)","roofing"),
            ChecklistItem("rf6","Skylights & Roof Penetrations","roofing"),
            ChecklistItem("rf7","Fascia & Soffit","roofing"),
            ChecklistItem("rf8","Ventilation (Ridge, Soffit, Attic Vents)","roofing"),
            ChecklistItem("rf9","Evidence of Ponding Water / Ice Dams","roofing")
        ),
        "exterior" to listOf(
            ChecklistItem("ex1","Wall Cladding / Siding","exterior"),
            ChecklistItem("ex2","Trim & Exterior Finishes","exterior"),
            ChecklistItem("ex3","Exterior Windows & Frames","exterior"),
            ChecklistItem("ex4","Exterior Doors","exterior"),
            ChecklistItem("ex5","Deck / Porch / Patio","exterior"),
            ChecklistItem("ex6","Stairs, Handrails & Guards (Exterior)","exterior"),
            ChecklistItem("ex7","Grading & Drainage","exterior"),
            ChecklistItem("ex8","Walkways & Driveways","exterior"),
            ChecklistItem("ex9","Vegetation / Landscape Concerns","exterior"),
            ChecklistItem("ex10","Retaining Walls & Site Features","exterior")
        ),
        "structure" to listOf(
            ChecklistItem("st1","Foundation Walls","structure"),
            ChecklistItem("st2","Floor Framing / Joists / Girders","structure"),
            ChecklistItem("st3","Columns & Piers","structure"),
            ChecklistItem("st4","Roof Framing / Rafters / Trusses","structure"),
            ChecklistItem("st5","Wall Framing","structure"),
            ChecklistItem("st6","Evidence of Settlement / Movement","structure"),
            ChecklistItem("st7","Evidence of Water Intrusion","structure"),
            ChecklistItem("st8","Crawlspace Conditions","structure"),
            ChecklistItem("st9","Basement (if applicable)","structure")
        ),
        "electrical" to listOf(
            ChecklistItem("el1","Service Entry & Conductors","electrical"),
            ChecklistItem("el2","Main Electrical Panel","electrical"),
            ChecklistItem("el3","Sub-Panel(s)","electrical"),
            ChecklistItem("el4","Branch Circuit Wiring","electrical"),
            ChecklistItem("el5","GFCI Protection","electrical"),
            ChecklistItem("el6","AFCI Protection","electrical"),
            ChecklistItem("el7","Outlets / Receptacles","electrical"),
            ChecklistItem("el8","Switches & Fixtures","electrical"),
            ChecklistItem("el9","Smoke & CO Detectors","electrical"),
            ChecklistItem("el10","Grounding & Bonding","electrical"),
            ChecklistItem("el11","Aluminum Branch Circuit Wiring","electrical")
        ),
        "hvac" to listOf(
            ChecklistItem("hv1","Heating System Operation","hvac"),
            ChecklistItem("hv2","Cooling System Operation","hvac"),
            ChecklistItem("hv3","Ductwork & Distribution","hvac"),
            ChecklistItem("hv4","Flue / Venting / Combustion Air","hvac"),
            ChecklistItem("hv5","Thermostat","hvac"),
            ChecklistItem("hv6","Filter Condition","hvac"),
            ChecklistItem("hv7","Gas Connections","hvac"),
            ChecklistItem("hv8","Fireplace / Wood Stove","hvac"),
            ChecklistItem("hv9","Kitchen Exhaust Fan","hvac"),
            ChecklistItem("hv10","Bath Exhaust Fans","hvac")
        ),
        "plumbing" to listOf(
            ChecklistItem("pl1","Water Supply Lines","plumbing"),
            ChecklistItem("pl2","Drain, Waste & Vent System","plumbing"),
            ChecklistItem("pl3","Water Heater","plumbing"),
            ChecklistItem("pl4","Kitchen Plumbing & Fixtures","plumbing"),
            ChecklistItem("pl5","Bathroom 1 — Fixtures & Drains","plumbing"),
            ChecklistItem("pl6","Bathroom 2 — Fixtures & Drains","plumbing"),
            ChecklistItem("pl7","Bathroom 3 — Fixtures & Drains","plumbing"),
            ChecklistItem("pl8","Laundry / Utility Connections","plumbing"),
            ChecklistItem("pl9","Main Water Shut-Off Valve","plumbing"),
            ChecklistItem("pl10","Sump Pump","plumbing"),
            ChecklistItem("pl11","Exterior Hose Bibs","plumbing")
        ),
        "interior" to listOf(
            ChecklistItem("in1","Interior Walls & Ceilings","interior"),
            ChecklistItem("in2","Interior Floors","interior"),
            ChecklistItem("in3","Interior Windows & Sills","interior"),
            ChecklistItem("in4","Interior Doors & Hardware","interior"),
            ChecklistItem("in5","Stairs, Handrails & Guardrails","interior"),
            ChecklistItem("in6","Kitchen — Cabinets, Counters & Appliances","interior"),
            ChecklistItem("in7","Bathrooms — Tile, Caulk & Waterproofing","interior"),
            ChecklistItem("in8","Smoke Detectors","interior"),
            ChecklistItem("in9","CO Detectors","interior"),
            ChecklistItem("in10","Attic Access & Hatch","interior")
        ),
        "insulation" to listOf(
            ChecklistItem("is1","Attic Insulation","insulation"),
            ChecklistItem("is2","Attic Ventilation","insulation"),
            ChecklistItem("is3","Crawlspace / Basement Insulation","insulation"),
            ChecklistItem("is4","Vapor Barrier","insulation"),
            ChecklistItem("is5","Wall Insulation (visible)","insulation"),
            ChecklistItem("is6","Exhaust Fan Terminations","insulation")
        ),
        "garage" to listOf(
            ChecklistItem("ga1","Garage Door Operation & Safety Reverse","garage"),
            ChecklistItem("ga2","Garage Door Opener","garage"),
            ChecklistItem("ga3","Fire-Rated Door to Living Space","garage"),
            ChecklistItem("ga4","Garage Floor","garage"),
            ChecklistItem("ga5","Garage Walls & Ceiling","garage"),
            ChecklistItem("ga6","Overhead Gas Heater","garage"),
            ChecklistItem("ga7","Attic Access in Garage","garage")
        )
    )
    fun allItems() = items.values.flatten()
}
@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val companyLogoPath: String = "",
    val badge1Path: String = "",
    val badge2Path: String = "",
    val badge3Path: String = "",
    val badge4Path: String = ""
)
