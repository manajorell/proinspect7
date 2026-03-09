package com.proinspect.app.data

data class ChecklistItem(
    val id: String,
    val title: String,
    val description: String = ""
)

object InspectionSections {
    
    val sections = listOf(
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
    
    private val roofingItems = listOf(
        ChecklistItem("roof_covering", "Roof Covering Material", "Inspect shingles, tiles, or metal roofing"),
        ChecklistItem("roof_structure", "Roof Structure & Framing", "Check for sagging, damage, or structural issues"),
        ChecklistItem("roof_flashing", "Flashing & Penetrations", "Inspect chimney, vent, and skylight flashing"),
        ChecklistItem("roof_drainage", "Gutters & Drainage", "Check gutters, downspouts, and drainage systems"),
        ChecklistItem("roof_ventilation", "Attic Ventilation", "Verify adequate ridge, soffit, or gable vents")
    )
    
    private val exteriorItems = listOf(
        ChecklistItem("ext_siding", "Siding & Exterior Walls", "Inspect for damage, rot, or deterioration"),
        ChecklistItem("ext_windows", "Windows & Doors", "Check operation, seals, and weatherstripping"),
        ChecklistItem("ext_foundation", "Foundation & Grading", "Inspect for cracks, settlement, or water issues"),
        ChecklistItem("ext_deck", "Decks, Porches & Balconies", "Check structural integrity and safety"),
        ChecklistItem("ext_driveway", "Driveway & Walkways", "Inspect for cracks, trip hazards, or drainage issues")
    )
    
    private val structureItems = listOf(
        ChecklistItem("struct_foundation", "Foundation Walls", "Check for cracks, bowing, or moisture"),
        ChecklistItem("struct_floor", "Floor Structure & Joists", "Inspect for sagging, rot, or pest damage"),
        ChecklistItem("struct_walls", "Wall Framing & Load Bearing", "Check for plumb, cracks, or structural issues"),
        ChecklistItem("struct_ceiling", "Ceiling Structure", "Inspect for sagging, water stains, or damage"),
        ChecklistItem("struct_stairs", "Stairs & Railings", "Verify code compliance and safety")
    )
    
    private val electricalItems = listOf(
        ChecklistItem("elec_panel", "Main Electrical Panel", "Inspect service size, breakers, and labeling"),
        ChecklistItem("elec_wiring", "Wiring & Connections", "Check for aluminum wiring, knob & tube, or hazards"),
        ChecklistItem("elec_outlets", "Outlets & Switches", "Test GFCI/AFCI protection and functionality"),
        ChecklistItem("elec_grounding", "Grounding & Bonding", "Verify proper grounding system"),
        ChecklistItem("elec_smoke", "Smoke & CO Detectors", "Check presence and operation")
    )
    
    private val hvacItems = listOf(
        ChecklistItem("hvac_heating", "Heating System", "Inspect furnace, heat pump, or boiler operation"),
        ChecklistItem("hvac_cooling", "Air Conditioning System", "Check AC unit, refrigerant lines, and operation"),
        ChecklistItem("hvac_ductwork", "Ductwork & Distribution", "Inspect for leaks, insulation, and airflow"),
        ChecklistItem("hvac_ventilation", "Ventilation Systems", "Check exhaust fans and fresh air intake"),
        ChecklistItem("hvac_thermostat", "Thermostat & Controls", "Test operation and temperature control")
    )
    
    private val plumbingItems = listOf(
        ChecklistItem("plumb_supply", "Water Supply System", "Check pipes, pressure, and water quality"),
        ChecklistItem("plumb_drain", "Drain, Waste & Vent", "Inspect for leaks, clogs, or improper venting"),
        ChecklistItem("plumb_fixtures", "Fixtures & Faucets", "Test sinks, toilets, showers, and tubs"),
        ChecklistItem("plumb_water_heater", "Water Heater", "Inspect age, operation, TPR valve, and venting"),
        ChecklistItem("plumb_sewer", "Sewer & Septic", "Check main line, cleanouts, or septic system")
    )
    
    private val interiorItems = listOf(
        ChecklistItem("int_walls", "Interior Walls & Ceilings", "Check for cracks, water damage, or defects"),
        ChecklistItem("int_floors", "Flooring", "Inspect carpet, tile, hardwood, or laminate"),
        ChecklistItem("int_doors", "Interior Doors", "Check operation, hardware, and condition"),
        ChecklistItem("int_windows", "Interior Windows", "Test operation and check for condensation"),
        ChecklistItem("int_kitchen", "Kitchen & Appliances", "Inspect cabinets, counters, and built-in appliances")
    )
    
    private val insulationItems = listOf(
        ChecklistItem("insul_attic", "Attic Insulation", "Check type, depth, and coverage"),
        ChecklistItem("insul_walls", "Wall Insulation", "Verify presence and adequacy"),
        ChecklistItem("insul_crawl", "Crawlspace Insulation", "Inspect insulation and vapor barriers"),
        ChecklistItem("insul_ventilation", "Attic & Crawl Ventilation", "Check for adequate airflow"),
        ChecklistItem("insul_moisture", "Moisture & Condensation", "Look for signs of moisture problems")
    )
    
    private val garageItems = listOf(
        ChecklistItem("garage_door", "Garage Door & Opener", "Test operation, safety sensors, and auto-reverse"),
        ChecklistItem("garage_structure", "Garage Structure", "Inspect walls, ceiling, and floor"),
        ChecklistItem("garage_electrical", "Garage Electrical", "Check outlets, lighting, and GFCI protection"),
        ChecklistItem("garage_fire", "Fire Separation", "Verify fire-rated walls and door to house"),
        ChecklistItem("garage_ventilation", "Garage Ventilation", "Check for adequate ventilation")
    )
    
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
    
    val allItems: List<ChecklistItem> = items.values.flatten()
}
