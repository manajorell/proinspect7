package com.proinspect.app.data

object DefectLibrary {

    data class DefectOption(val label: String, val description: String)

    val defects: Map<String, List<DefectOption>> = mapOf(
        "rf1" to listOf(
            DefectOption("Missing/Damaged Shingles", "Several asphalt shingles were observed to be missing or damaged at the time of inspection. Damaged shingles allow water infiltration and should be replaced by a licensed roofing contractor to prevent further deterioration."),
            DefectOption("Granule Loss", "Significant granule loss was observed on the asphalt shingles, indicating the roof covering is nearing the end of its serviceable life. Budget for roof replacement in the near future."),
            DefectOption("Curling/Cupping Shingles", "Shingles are exhibiting curling or cupping, which is indicative of age-related deterioration and/or moisture imbalance. Recommend evaluation by a licensed roofing contractor."),
            DefectOption("Near End of Life", "The roof covering appears to be near the end of its serviceable life based on visible wear. Budget for replacement within the next 1-3 years."),
            DefectOption("Good Condition", "The roof covering was observed to be in good condition with no significant deficiencies noted at the time of inspection.")
        ),
        "rf2" to listOf(
            DefectOption("Flashing Separation", "Flashing was observed to be separating from the adjoining surface. This condition allows water infiltration and should be repaired by a licensed roofing contractor."),
            DefectOption("Missing Flashing", "Flashing was observed to be missing at one or more locations. Proper flashing is critical to preventing water intrusion and should be installed by a licensed roofing contractor."),
            DefectOption("Sealed with Caulk Only", "Flashing transitions were observed to be sealed with caulk only, without proper step or counter flashing. Caulk is a temporary repair and proper flashing should be installed.")
        ),
        "rf4" to listOf(
            DefectOption("Gutters Pulling Away", "Gutters were observed to be pulling away from the fascia at one or more locations. This should be re-secured to prevent water damage to the fascia and foundation."),
            DefectOption("Debris Buildup", "Significant debris buildup was observed in the gutters. Gutters should be cleaned to ensure proper drainage away from the foundation."),
            DefectOption("Downspout at Foundation", "One or more downspouts discharge directly at the foundation. Extensions should be installed to direct water away from the structure.")
        ),
        "rf5" to listOf(
            DefectOption("Chimney Cap Missing", "The chimney cap was observed to be missing. A chimney cap prevents water, debris, and animals from entering the flue and should be installed."),
            DefectOption("Mortar Deterioration", "The mortar joints in the chimney masonry were observed to be deteriorating. Tuckpointing by a qualified mason is recommended to prevent water intrusion."),
            DefectOption("Chimney Crown Cracked", "The chimney crown was observed to be cracked. The crown should be repaired or replaced to prevent water infiltration into the chimney structure.")
        ),
        "ex1" to listOf(
            DefectOption("Paint Peeling/Failing", "Paint was observed to be peeling or failing at multiple locations on the exterior siding. Surfaces should be properly prepared and repainted to protect the underlying material."),
            DefectOption("Damaged Siding", "Siding was observed to be damaged at one or more locations. Damaged sections should be repaired or replaced to prevent moisture intrusion."),
            DefectOption("Wood Rot", "Wood rot was observed on the siding at one or more locations. Rotted wood should be replaced and the area properly primed and painted to prevent recurrence."),
            DefectOption("Siding Near Grade", "Siding was observed to be within 6 inches of grade at one or more locations, which can lead to moisture damage and insect infestation.")
        ),
        "ex5" to listOf(
            DefectOption("Deck Boards Deteriorating", "Deck boards were observed to be deteriorating due to weathering and age. Replacement of affected boards is recommended before they become a safety concern."),
            DefectOption("Missing Handrail", "Handrails were absent on one or more stairways with 3 or more risers. Handrails are required by code and should be installed for safety."),
            DefectOption("Baluster Spacing", "Baluster spacing exceeds 4 inches, which presents a safety hazard for young children. Recommend adding balusters to achieve compliant spacing.")
        ),
        "ex7" to listOf(
            DefectOption("Negative Grading", "The ground was observed to slope toward the foundation at one or more locations. Negative grading promotes water infiltration and should be corrected."),
            DefectOption("Vegetation Against Structure", "Vegetation was observed growing against the structure. Vegetation retains moisture against the siding and should be trimmed back a minimum of 12 inches.")
        ),
        "st1" to listOf(
            DefectOption("Cracks - Horizontal", "Horizontal cracks were observed in the foundation wall. Horizontal cracks can indicate lateral soil pressure and should be evaluated by a licensed structural engineer."),
            DefectOption("Cracks - Stair Step", "Stair-step cracks were observed in the foundation masonry. This pattern typically indicates differential settlement and should be evaluated by a structural engineer."),
            DefectOption("Cracks - Vertical", "Vertical cracks were observed in the foundation wall. Cracks should be sealed to prevent water infiltration and monitored for movement."),
            DefectOption("Efflorescence/Water Staining", "Efflorescence or water staining was observed on the foundation walls, indicating past or ongoing water infiltration.")
        ),
        "st7" to listOf(
            DefectOption("Active Water Seepage", "Evidence of active water seepage was observed. The source should be identified and corrected to prevent structural damage and mold growth."),
            DefectOption("Staining/Past Moisture", "Water staining was observed, indicating past moisture intrusion. The source should be identified and corrected."),
            DefectOption("Mold-Like Substance", "A mold-like substance was observed. Further evaluation by a qualified mold inspector is recommended.")
        ),
        "el2" to listOf(
            DefectOption("Double-Tapped Breakers", "Double-tapped circuit breakers were observed in the electrical panel. Most breakers are designed for only one conductor and this condition should be corrected by a licensed electrician."),
            DefectOption("Panel Corrosion", "Corrosion was observed inside the electrical panel. This should be evaluated by a licensed electrician as it may indicate moisture infiltration."),
            DefectOption("Overheating Evidence", "Evidence of overheating was observed including discolored wires or breakers. This is a fire hazard and should be evaluated by a licensed electrician immediately.")
        ),
        "el5" to listOf(
            DefectOption("GFCI Absent - Kitchen", "GFCI protection was absent at kitchen countertop circuits. GFCI protection is required within 6 feet of all sinks and should be installed by a licensed electrician."),
            DefectOption("GFCI Absent - Bathroom", "GFCI protection was absent in one or more bathrooms. GFCI protection is required in all bathrooms and should be installed by a licensed electrician."),
            DefectOption("GFCI Absent - Exterior", "GFCI protection was absent at one or more exterior outlets. GFCI protection is required for all exterior outlets."),
            DefectOption("GFCI Absent - Garage", "GFCI protection was absent in the garage. GFCI protection is required for all garage outlets.")
        ),
        "el9" to listOf(
            DefectOption("Smoke Detectors Absent", "Smoke detectors were absent in one or more required locations. Smoke detectors should be installed in each sleeping room, outside each sleeping area, and on each level of the home."),
            DefectOption("CO Detectors Absent", "Carbon monoxide detectors were absent. CO detectors are required within 15 feet of each sleeping room in homes with fuel-burning appliances or attached garages."),
            DefectOption("Detectors Older Than 10 Years", "Smoke detectors appeared to be older than 10 years. Smoke detectors should be replaced every 10 years per manufacturer recommendations.")
        ),
        "hv1" to listOf(
            DefectOption("Not Responding to Thermostat", "The heating system did not respond to thermostat operation at the time of inspection. Further evaluation by a licensed HVAC contractor is recommended."),
            DefectOption("Near End of Service Life", "The heating system is approaching or has exceeded its typical service life of 15-20 years. Budget for replacement and have the system serviced annually."),
            DefectOption("Operating Normally", "The heating system was tested and observed to be operating normally at the time of inspection.")
        ),
        "hv2" to listOf(
            DefectOption("Not Cooling", "The cooling system did not produce adequate temperature differential at the time of inspection. Further evaluation by a licensed HVAC contractor is recommended."),
            DefectOption("Condenser Dirty", "The condenser coil was observed to be dirty. The condenser should be cleaned by an HVAC contractor to maintain efficiency."),
            DefectOption("Near End of Service Life", "The cooling system is approaching or has exceeded its typical service life of 15-20 years. Budget for replacement."),
            DefectOption("Operating Normally", "The cooling system was tested and observed to be operating normally with adequate temperature differential.")
        ),
        "hv6" to listOf(
            DefectOption("Filter Dirty", "The HVAC filter was observed to be dirty and in need of replacement. Filters should be replaced every 1-3 months."),
            DefectOption("Filter Absent", "The HVAC filter was observed to be absent. Operating the system without a filter allows dust and debris to accumulate and should be corrected immediately.")
        ),
        "pl1" to listOf(
            DefectOption("Galvanized Pipe Deterioration", "Galvanized steel supply piping was observed. Galvanized pipe is subject to interior corrosion over time. Replacement with modern piping material should be budgeted."),
            DefectOption("Active Leak", "An active leak was observed at one or more supply connections. Leaks should be repaired by a licensed plumber immediately to prevent water damage.")
        ),
        "pl3" to listOf(
            DefectOption("Near End of Service Life", "The water heater is approaching or has exceeded its typical service life of 8-12 years. Budget for replacement."),
            DefectOption("No Expansion Tank", "No expansion tank was observed. An expansion tank is required on closed plumbing systems to prevent excessive pressure buildup."),
            DefectOption("TPR Valve Concern", "The temperature and pressure relief valve discharge pipe was observed to be improper or absent. This is a critical safety device and should be corrected by a licensed plumber."),
            DefectOption("Improper Venting", "The water heater flue venting was observed to be improperly installed. Improper venting can allow combustion gases to enter the living space.")
        ),
        "in1" to listOf(
            DefectOption("Water Staining on Ceiling", "Water staining was observed on the ceiling at one or more locations. The source of moisture should be identified and corrected."),
            DefectOption("Cracks in Walls/Ceilings", "Cracks were observed in the walls or ceilings. Significant cracks should be evaluated to rule out structural movement."),
            DefectOption("Mold-Like Substance", "A mold-like substance was observed. Further evaluation by a qualified mold inspector is recommended.")
        ),
        "in7" to listOf(
            DefectOption("Caulk Failing at Tub/Shower", "The caulk at the tub or shower surround was observed to be failing. Failed caulk allows water intrusion and should be replaced."),
            DefectOption("Grout Missing/Cracked", "Grout was observed to be missing or cracked in the tile surround. Missing grout allows water infiltration and should be repaired."),
            DefectOption("Soft Subfloor at Toilet", "The subfloor adjacent to the toilet was observed to be soft, indicating possible water damage. Further evaluation by a licensed plumber is recommended.")
        ),
        "is1" to listOf(
            DefectOption("Insufficient Insulation", "Attic insulation was observed to be below the recommended R-value for this climate zone. Adding insulation is recommended to improve energy efficiency."),
            DefectOption("Insulation Blocking Vents", "Insulation was observed to be blocking soffit vents. Baffles should be installed to maintain air flow from the soffit vents.")
        ),
        "is2" to listOf(
            DefectOption("Inadequate Ventilation", "Attic ventilation appeared to be inadequate. Inadequate ventilation can lead to moisture buildup and premature roof deterioration."),
            DefectOption("Exhaust Fan into Attic", "One or more bathroom exhaust fans were observed to be venting into the attic. This introduces moisture into the attic and should be corrected.")
        ),
        "ga1" to listOf(
            DefectOption("Auto-Reverse Not Functioning", "The automatic reversal safety feature of the garage door did not function properly during testing. This is a safety hazard and should be repaired immediately."),
            DefectOption("Photo Eye Misaligned", "The photo eye sensors appeared to be misaligned. The auto-reverse safety feature may not operate correctly and should be adjusted.")
        ),
        "ga3" to listOf(
            DefectOption("Not Fire-Rated Door", "The door between the garage and living space does not appear to be a fire-rated door. A minimum 20-minute fire-rated door is required at this location."),
            DefectOption("Door Not Self-Closing", "The door between the garage and living space was not self-closing. A self-closing mechanism is required at this location.")
        )
    )

    fun getDefectsForItem(itemId: String): List<DefectOption> = defects[itemId] ?: emptyList()
}
