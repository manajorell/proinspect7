package com.proinspect.app.data

object DefectLibrary {

    data class DefectOption(val label: String, val description: String)

    val defects: Map<String, List<DefectOption>> = mapOf(

        // ─── ROOFING ───────────────────────────────────────────────
        "rf1" to listOf(
            DefectOption("Missing/Damaged Shingles", "Several asphalt shingles were observed to be missing or damaged at the time of inspection. Damaged shingles allow water infiltration and should be replaced by a licensed roofing contractor to prevent further deterioration."),
            DefectOption("Granule Loss", "Significant granule loss was observed on the asphalt shingles, indicating the roof covering is nearing the end of its serviceable life. Budget for roof replacement in the near future."),
            DefectOption("Curling/Cupping Shingles", "Shingles are exhibiting curling or cupping, which is indicative of age-related deterioration and/or moisture imbalance. Recommend evaluation by a licensed roofing contractor."),
            DefectOption("Near End of Life", "The roof covering appears to be near the end of its serviceable life based on visible wear. Budget for replacement within the next 1-3 years."),
            DefectOption("Moss/Algae Growth", "Moss or algae growth was observed on the roof surface. Growth retains moisture against the shingles and accelerates deterioration. Treatment and cleaning by a roofing contractor is recommended."),
            DefectOption("Good Condition", "The roof covering was observed to be in good condition with no significant deficiencies noted at the time of inspection.")
        ),
        "rf2" to listOf(
            DefectOption("Flashing Separation", "Flashing was observed to be separating from the adjoining surface. This condition allows water infiltration and should be repaired by a licensed roofing contractor."),
            DefectOption("Missing Flashing", "Flashing was observed to be missing at one or more locations. Proper flashing is critical to preventing water intrusion and should be installed by a licensed roofing contractor."),
            DefectOption("Sealed with Caulk Only", "Flashing transitions were observed to be sealed with caulk only, without proper step or counter flashing. Caulk is a temporary repair and proper flashing should be installed."),
            DefectOption("Rusted/Corroded Flashing", "Flashing was observed to be rusted or corroded, compromising its ability to seal against water intrusion. Replacement by a licensed roofing contractor is recommended."),
            DefectOption("Good Condition", "Flashing was observed to be properly installed and in good condition at the time of inspection.")
        ),
        "rf3" to listOf(
            DefectOption("Ridge Cap Damaged", "Ridge cap shingles were observed to be damaged or missing at one or more locations. The ridge cap should be repaired or replaced to prevent water intrusion at the roof peak."),
            DefectOption("Ridge Cap Lifting", "Ridge cap shingles were observed to be lifting at the edges. This condition can allow wind-driven rain to penetrate and should be re-secured."),
            DefectOption("Good Condition", "The ridge cap was observed to be in good condition with no significant deficiencies noted.")
        ),
        "rf4" to listOf(
            DefectOption("Gutters Pulling Away", "Gutters were observed to be pulling away from the fascia at one or more locations. This should be re-secured to prevent water damage to the fascia and foundation."),
            DefectOption("Debris Buildup", "Significant debris buildup was observed in the gutters. Gutters should be cleaned to ensure proper drainage away from the foundation."),
            DefectOption("Downspout at Foundation", "One or more downspouts discharge directly at the foundation. Extensions should be installed to direct water away from the structure."),
            DefectOption("Gutters Sagging", "Gutters were observed to be sagging at one or more locations, preventing proper drainage. Gutters should be re-pitched or re-secured."),
            DefectOption("Holes/Rust in Gutters", "Holes or rust-through were observed in the gutters. Affected sections should be repaired or replaced."),
            DefectOption("Good Condition", "Gutters and downspouts were observed to be in good condition and properly directing water away from the structure.")
        ),
        "rf5" to listOf(
            DefectOption("Chimney Cap Missing", "The chimney cap was observed to be missing. A chimney cap prevents water, debris, and animals from entering the flue and should be installed."),
            DefectOption("Mortar Deterioration", "The mortar joints in the chimney masonry were observed to be deteriorating. Tuckpointing by a qualified mason is recommended to prevent water intrusion."),
            DefectOption("Chimney Crown Cracked", "The chimney crown was observed to be cracked. The crown should be repaired or replaced to prevent water infiltration into the chimney structure."),
            DefectOption("Chimney Leaning", "The chimney appeared to be leaning or out of plumb. This condition should be evaluated by a licensed structural engineer or masonry contractor."),
            DefectOption("Flashing at Chimney Failed", "The flashing at the chimney base was observed to be failing or improperly installed. Proper flashing should be installed by a licensed roofing contractor."),
            DefectOption("Good Condition", "The chimney was observed to be in good condition with no significant deficiencies noted at the time of inspection.")
        ),
        "rf6" to listOf(
            DefectOption("Skylight Leaking", "Evidence of water intrusion was observed around one or more skylights. The skylight flashing and seals should be evaluated and repaired by a licensed roofing contractor."),
            DefectOption("Skylight Seal Failing", "The seal around one or more skylights appeared to be failing. Re-sealing or flashing repair is recommended."),
            DefectOption("Penetration Not Flashed", "One or more roof penetrations were observed to lack proper flashing. Flashing should be installed to prevent water intrusion."),
            DefectOption("Good Condition", "Skylights and roof penetrations were observed to be in good condition with no evidence of leakage.")
        ),
        "rf7" to listOf(
            DefectOption("Fascia Rotted", "The fascia board was observed to be rotted at one or more locations. Rotted fascia should be replaced to prevent further deterioration and structural damage."),
            DefectOption("Soffit Damaged", "The soffit was observed to be damaged or missing at one or more locations. Damaged soffit should be repaired to prevent animal entry and moisture intrusion."),
            DefectOption("Paint Peeling on Fascia", "Paint was observed to be peeling on the fascia boards. The fascia should be properly prepared and repainted to protect the wood."),
            DefectOption("Good Condition", "Fascia and soffit were observed to be in good condition with no significant deficiencies noted.")
        ),
        "rf8" to listOf(
            DefectOption("Inadequate Ventilation", "Attic ventilation appeared to be inadequate based on the ratio of ventilation area to attic floor area. Inadequate ventilation can lead to moisture buildup, ice damming, and premature roof deterioration."),
            DefectOption("Vents Blocked", "One or more attic vents were observed to be blocked. Vents should be cleared to allow proper air circulation."),
            DefectOption("Ridge Vent Damaged", "The ridge vent was observed to be damaged. A damaged ridge vent allows water infiltration and should be repaired or replaced."),
            DefectOption("Good Condition", "Roof ventilation was observed to be adequate with properly functioning vents.")
        ),
        "rf9" to listOf(
            DefectOption("Evidence of Ice Damming", "Evidence of past ice damming was observed including staining or damaged shingles at the eaves. Ice and water shield should be installed under the shingles at the eaves if not already present."),
            DefectOption("Ponding Water Areas", "Areas prone to ponding water were observed on the roof surface. Ponding water accelerates roof deterioration and should be addressed."),
            DefectOption("No Evidence Observed", "No evidence of ponding water or ice damming was observed at the time of inspection.")
        ),

        // ─── EXTERIOR ──────────────────────────────────────────────
        "ex1" to listOf(
            DefectOption("Paint Peeling/Failing", "Paint was observed to be peeling or failing at multiple locations on the exterior siding. Surfaces should be properly prepared and repainted to protect the underlying material."),
            DefectOption("Damaged Siding", "Siding was observed to be damaged at one or more locations. Damaged sections should be repaired or replaced to prevent moisture intrusion."),
            DefectOption("Wood Rot", "Wood rot was observed on the siding at one or more locations. Rotted wood should be replaced and the area properly primed and painted to prevent recurrence."),
            DefectOption("Siding Near Grade", "Siding was observed to be within 6 inches of grade at one or more locations, which can lead to moisture damage and insect infestation."),
            DefectOption("Gaps/Missing Caulk", "Gaps or missing caulk were observed at siding joints and penetrations. Caulking should be applied to prevent moisture and air infiltration."),
            DefectOption("Good Condition", "The exterior siding was observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex2" to listOf(
            DefectOption("Trim Rotted", "Exterior trim was observed to be rotted at one or more locations. Rotted trim should be replaced and properly painted to prevent recurrence."),
            DefectOption("Trim Paint Failing", "Paint on exterior trim was observed to be peeling or failing. Trim should be properly prepared and repainted."),
            DefectOption("Good Condition", "Exterior trim and finishes were observed to be in good condition.")
        ),
        "ex3" to listOf(
            DefectOption("Failed Seal - Fogged Glass", "One or more window insulated glass units showed evidence of seal failure indicated by fogging or condensation between the panes. Affected units should be replaced."),
            DefectOption("Window Frame Rot", "Wood rot was observed in one or more window frames. Rotted sections should be repaired or the window replaced to prevent further deterioration."),
            DefectOption("Windows Not Opening/Closing", "One or more windows were observed to not open, close, or latch properly. Windows should be repaired to function as intended."),
            DefectOption("Missing/Damaged Screens", "Window screens were observed to be missing or damaged at multiple locations."),
            DefectOption("Caulk Failing at Windows", "Caulk was observed to be failing around window frames. Re-caulking is recommended to prevent moisture and air infiltration."),
            DefectOption("Good Condition", "Exterior windows were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex4" to listOf(
            DefectOption("Door Not Sealing Properly", "One or more exterior doors were observed to not seal properly, allowing air and potential moisture infiltration. Weather stripping should be replaced."),
            DefectOption("Door Hardware Defective", "Door hardware including locks or latches were observed to be defective. Hardware should be repaired or replaced for security and function."),
            DefectOption("Door Frame Rot", "Wood rot was observed in one or more exterior door frames. Rotted sections should be repaired or replaced."),
            DefectOption("Good Condition", "Exterior doors were observed to be in good condition and functioning properly.")
        ),
        "ex5" to listOf(
            DefectOption("Deck Boards Deteriorating", "Deck boards were observed to be deteriorating due to weathering and age. Replacement of affected boards is recommended before they become a safety concern."),
            DefectOption("Missing Handrail", "Handrails were absent on one or more stairways with 3 or more risers. Handrails are required by code and should be installed for safety."),
            DefectOption("Baluster Spacing", "Baluster spacing exceeds 4 inches, which presents a safety hazard for young children. Recommend adding balusters to achieve compliant spacing."),
            DefectOption("Ledger Board Concern", "The deck ledger board connection to the house appeared to be improperly fastened or showed signs of deterioration. Evaluation by a licensed contractor is recommended as this is a structural concern."),
            DefectOption("Posts Not Properly Anchored", "Deck posts were observed to not be properly anchored. Posts should be secured with approved post base hardware."),
            DefectOption("Good Condition", "The deck/porch was observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex6" to listOf(
            DefectOption("Handrail Missing", "Handrails were absent on exterior stairs with 3 or more risers. Handrails are required and should be installed for safety."),
            DefectOption("Handrail Not Graspable", "The handrail profile was observed to not be graspable. A graspable handrail with a circular or oval profile is required."),
            DefectOption("Guard Too Low", "The guardrail height was observed to be below the required 36 inches. Guards should be raised to meet current safety standards."),
            DefectOption("Stair Risers Uneven", "Stair risers were observed to be uneven, presenting a trip hazard. Stairs should be repaired to provide uniform rise."),
            DefectOption("Good Condition", "Exterior stairs, handrails, and guards were observed to be in good condition.")
        ),
        "ex7" to listOf(
            DefectOption("Negative Grading", "The ground was observed to slope toward the foundation at one or more locations. Negative grading promotes water infiltration and should be corrected by adding fill and regrading."),
            DefectOption("Vegetation Against Structure", "Vegetation was observed growing against the structure. Vegetation retains moisture against the siding and should be trimmed back a minimum of 12 inches."),
            DefectOption("Window Well No Drain", "One or more window wells lacked an adequate drain. Window wells should have gravel and a drain to prevent water accumulation."),
            DefectOption("Good Condition", "Grading and drainage appeared to be directing water away from the foundation adequately.")
        ),
        "ex8" to listOf(
            DefectOption("Walkway Cracked/Heaved", "The walkway or driveway was observed to be significantly cracked or heaved, presenting a trip hazard. Affected areas should be repaired or replaced."),
            DefectOption("Settlement at Foundation", "The driveway or walkway was observed to have settled adjacent to the foundation, directing water toward the structure."),
            DefectOption("Good Condition", "Walkways and driveway were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex9" to listOf(
            DefectOption("Trees Overhanging Roof", "Tree branches were observed to be overhanging the roof. Overhanging branches can damage the roof and should be trimmed back."),
            DefectOption("Roots Near Foundation", "Large tree roots were observed in close proximity to the foundation. Root intrusion can damage foundations over time and should be monitored."),
            DefectOption("No Concerns Observed", "No significant vegetation concerns were observed at the time of inspection.")
        ),
        "ex10" to listOf(
            DefectOption("Retaining Wall Leaning", "A retaining wall was observed to be leaning or showing signs of movement. Evaluation by a licensed structural engineer is recommended."),
            DefectOption("No Concerns Observed", "No significant retaining wall or site feature concerns were observed.")
        ),

        // ─── STRUCTURAL ────────────────────────────────────────────
        "st1" to listOf(
            DefectOption("Cracks - Horizontal", "Horizontal cracks were observed in the foundation wall. Horizontal cracks can indicate lateral soil pressure and should be evaluated by a licensed structural engineer."),
            DefectOption("Cracks - Stair Step", "Stair-step cracks were observed in the foundation masonry. This pattern typically indicates differential settlement and should be evaluated by a structural engineer."),
            DefectOption("Cracks - Vertical", "Vertical cracks were observed in the foundation wall. Cracks should be sealed to prevent water infiltration and monitored for movement."),
            DefectOption("Efflorescence/Water Staining", "Efflorescence or water staining was observed on the foundation walls, indicating past or ongoing water infiltration. The source should be identified and corrected."),
            DefectOption("Good Condition", "The foundation walls were observed to be in good condition with no significant cracking or movement noted.")
        ),
        "st2" to listOf(
            DefectOption("Sagging Floor Joists", "Floor joists were observed to be sagging at one or more locations. This condition should be evaluated by a licensed structural engineer."),
            DefectOption("Notched/Drilled Improperly", "Floor joists were observed to have been notched or drilled outside of allowable limits, potentially compromising structural integrity."),
            DefectOption("Insect Damage", "Evidence of insect damage (termites or wood-boring beetles) was observed on floor framing. A licensed pest inspector should evaluate the extent of damage."),
            DefectOption("Good Condition", "Floor framing was observed to be in good condition with no significant deficiencies noted.")
        ),
        "st3" to listOf(
            DefectOption("Column Improperly Supported", "A support column was observed to be improperly bearing on the footing or beam. Evaluation by a licensed structural engineer is recommended."),
            DefectOption("Column Deterioration", "A support column was observed to show signs of deterioration including rot or corrosion. Evaluation and repair by a licensed contractor is recommended."),
            DefectOption("Good Condition", "Columns and piers were observed to be in good condition.")
        ),
        "st4" to listOf(
            DefectOption("Rafters Cracked/Broken", "One or more roof rafters or truss members were observed to be cracked or broken. This is a structural concern and should be evaluated by a licensed structural engineer."),
            DefectOption("Ridge Board Sagging", "The roof ridge board appeared to be sagging, indicating possible structural inadequacy or settlement. Evaluation by a structural engineer is recommended."),
            DefectOption("Truss Modified", "Evidence of field modifications to roof trusses was observed. Truss modifications must be engineered and approved. Evaluation by a structural engineer is recommended."),
            DefectOption("Good Condition", "Roof framing was observed to be in good condition with no significant deficiencies noted.")
        ),
        "st5" to listOf(
            DefectOption("Wall Framing Concern", "Concerns were observed with the wall framing including improper notching, missing blocking, or signs of movement. Evaluation by a licensed contractor is recommended."),
            DefectOption("Good Condition", "Wall framing appeared to be in good condition where visible.")
        ),
        "st6" to listOf(
            DefectOption("Significant Settlement Observed", "Evidence of significant differential settlement was observed including sloping floors, sticking doors, and diagonal cracking. Evaluation by a licensed structural engineer is strongly recommended."),
            DefectOption("Minor Settlement - Monitor", "Minor evidence of settlement was observed. This appears to be historic and stable but should be monitored for any progression."),
            DefectOption("No Evidence Observed", "No evidence of significant settlement or structural movement was observed at the time of inspection.")
        ),
        "st7" to listOf(
            DefectOption("Active Water Seepage", "Evidence of active water seepage was observed. The source should be identified and corrected to prevent structural damage and mold growth."),
            DefectOption("Staining/Past Moisture", "Water staining was observed, indicating past moisture intrusion. The source should be identified and corrected to prevent recurrence."),
            DefectOption("Mold-Like Substance", "A mold-like substance was observed. Further evaluation by a qualified mold inspector is recommended. This is a health concern."),
            DefectOption("No Evidence Observed", "No evidence of water intrusion was observed at the time of inspection.")
        ),
        "st8" to listOf(
            DefectOption("Standing Water in Crawlspace", "Standing water was observed in the crawlspace. This condition promotes mold growth and structural deterioration and should be corrected immediately."),
            DefectOption("High Humidity/Condensation", "High humidity or condensation was observed in the crawlspace. A vapor barrier and/or mechanical ventilation should be considered."),
            DefectOption("Vapor Barrier Absent/Damaged", "The vapor barrier in the crawlspace was absent or damaged. A minimum 6-mil polyethylene vapor barrier should be installed over the soil."),
            DefectOption("No Access - Not Inspected", "The crawlspace was not accessible and could not be inspected at the time of inspection."),
            DefectOption("Good Condition", "The crawlspace was observed to be in good condition with adequate clearance, ventilation, and no evidence of moisture concerns.")
        ),
        "st9" to listOf(
            DefectOption("Water Staining in Basement", "Water staining was observed in the basement indicating past or recurring moisture intrusion. The source should be identified and corrected."),
            DefectOption("Active Seepage", "Active water seepage was observed in the basement. Waterproofing measures should be evaluated by a qualified contractor."),
            DefectOption("Sump Pump Needed", "Given the observed moisture conditions, installation of a sump pump system is recommended."),
            DefectOption("Good Condition", "The basement was observed to be in good condition with no significant moisture concerns.")
        ),

        // ─── ELECTRICAL ────────────────────────────────────────────
        "el1" to listOf(
            DefectOption("Service Entry Damage", "The service entry cable was observed to be damaged or deteriorating. This should be evaluated and repaired by a licensed electrician."),
            DefectOption("Drip Loop Absent", "A drip loop was not present at the service entry. A drip loop prevents water from following the conductors into the weatherhead."),
            DefectOption("Clearance Concern", "The service entry conductors did not appear to meet minimum clearance requirements. Evaluation by the utility company and a licensed electrician is recommended."),
            DefectOption("Good Condition", "The service entry and conductors appeared to be in good condition.")
        ),
        "el2" to listOf(
            DefectOption("Double-Tapped Breakers", "Double-tapped circuit breakers were observed in the electrical panel. Most breakers are designed for only one conductor and this condition should be corrected by a licensed electrician."),
            DefectOption("Panel Corrosion", "Corrosion was observed inside the electrical panel. This should be evaluated by a licensed electrician as it may indicate moisture infiltration."),
            DefectOption("Overheating Evidence", "Evidence of overheating was observed including discolored wires or breakers. This is a fire hazard and should be evaluated by a licensed electrician immediately."),
            DefectOption("Breakers Not Labeled", "Circuit breakers in the panel were not adequately labeled. All breakers should be labeled for safety and emergency response."),
            DefectOption("Open Knockouts", "Open knockouts were observed in the electrical panel. Open knockouts present a shock hazard and should be filled with appropriate closure plugs."),
            DefectOption("Federal Pacific/Zinsco Panel", "The electrical panel is a Federal Pacific or Zinsco brand, which have known safety concerns including failure to trip under overload conditions. Replacement by a licensed electrician is recommended."),
            DefectOption("Good Condition", "The electrical panel was observed to be in good condition with no significant deficiencies noted.")
        ),
        "el3" to listOf(
            DefectOption("Sub-Panel Concerns", "Concerns were observed with the sub-panel including improper wiring, missing cover, or double-tapped breakers. Evaluation by a licensed electrician is recommended."),
            DefectOption("Good Condition", "The sub-panel was observed to be in good condition.")
        ),
        "el4" to listOf(
            DefectOption("Aluminum Wiring", "Aluminum branch circuit wiring was observed. Aluminum wiring requires special devices and connections. Evaluation by a licensed electrician is recommended."),
            DefectOption("Knob and Tube Wiring", "Knob and tube wiring was observed. This older wiring system lacks a ground conductor and should be evaluated by a licensed electrician. Many insurance companies have concerns with this type of wiring."),
            DefectOption("Exposed Wiring", "Exposed wiring was observed at one or more locations. Wiring should be protected by conduit or enclosed within walls."),
            DefectOption("Good Condition", "Branch circuit wiring appeared to be in good condition where visible.")
        ),
        "el5" to listOf(
            DefectOption("GFCI Absent - Kitchen", "GFCI protection was absent at kitchen countertop circuits. GFCI protection is required within 6 feet of all sinks and should be installed by a licensed electrician."),
            DefectOption("GFCI Absent - Bathroom", "GFCI protection was absent in one or more bathrooms. GFCI protection is required in all bathrooms and should be installed."),
            DefectOption("GFCI Absent - Exterior", "GFCI protection was absent at one or more exterior outlets. GFCI protection is required for all exterior outlets."),
            DefectOption("GFCI Absent - Garage", "GFCI protection was absent in the garage. GFCI protection is required for all garage outlets."),
            DefectOption("GFCI Not Tripping", "One or more GFCI outlets were observed to not trip when tested. GFCI devices that fail to function should be replaced immediately."),
            DefectOption("GFCI Present and Functional", "GFCI protection was present and tested functional at all required locations.")
        ),
        "el6" to listOf(
            DefectOption("AFCI Absent - Bedrooms", "AFCI protection was absent on bedroom circuits. AFCI protection is required on all bedroom circuits in homes built after 2002."),
            DefectOption("AFCI Present", "AFCI protection was observed to be present on required circuits.")
        ),
        "el7" to listOf(
            DefectOption("Reversed Polarity", "Reversed polarity was detected at one or more outlets. This condition should be corrected by a licensed electrician."),
            DefectOption("Ungrounded Outlets", "Ungrounded outlets were observed at one or more locations. Grounded outlets should be installed or GFCI protection provided."),
            DefectOption("Outlets Not Working", "One or more outlets were observed to not function. Further evaluation by a licensed electrician is recommended."),
            DefectOption("Outlet Missing Cover Plate", "One or more outlets were observed to be missing cover plates. Cover plates should be installed for safety."),
            DefectOption("Good Condition", "Outlets and receptacles were observed to be in good condition and functioning properly.")
        ),
        "el8" to listOf(
            DefectOption("Switch Not Functioning", "One or more light switches were observed to not function properly. Further evaluation is recommended."),
            DefectOption("Fixture Missing Cover", "One or more light fixtures were observed to be missing covers or globes."),
            DefectOption("Good Condition", "Switches and fixtures were observed to be in good condition and functioning properly.")
        ),
        "el9" to listOf(
            DefectOption("Smoke Detectors Absent", "Smoke detectors were absent in one or more required locations. Smoke detectors should be installed in each sleeping room, outside each sleeping area, and on each level of the home."),
            DefectOption("CO Detectors Absent", "Carbon monoxide detectors were absent. CO detectors are required within 15 feet of each sleeping room in homes with fuel-burning appliances or attached garages."),
            DefectOption("Detectors Older Than 10 Years", "Smoke detectors appeared to be older than 10 years. Smoke detectors should be replaced every 10 years per manufacturer recommendations."),
            DefectOption("Detectors Present and Tested", "Smoke and CO detectors were present and operational at the time of inspection.")
        ),
        "el10" to listOf(
            DefectOption("Ground Rod Not Visible", "The grounding electrode (ground rod) was not visible or accessible for inspection. A licensed electrician should verify the grounding system is complete."),
            DefectOption("Bonding Jumper Absent", "The main bonding jumper appeared to be absent in the main panel. This is a safety concern and should be corrected by a licensed electrician."),
            DefectOption("Good Condition", "Grounding and bonding appeared to be properly installed.")
        ),
        "el11" to listOf(
            DefectOption("Aluminum Wiring Present", "Aluminum branch circuit wiring was observed. Aluminum wiring is prone to loosening at connections due to thermal expansion and can be a fire hazard. A licensed electrician should evaluate all connections and install CO/ALR rated devices throughout."),
            DefectOption("No Aluminum Wiring Observed", "No aluminum branch circuit wiring was observed. Wiring appeared to be copper throughout.")
        ),

        // ─── HVAC ──────────────────────────────────────────────────
        "hv1" to listOf(
            DefectOption("Not Responding to Thermostat", "The heating system did not respond to thermostat operation at the time of inspection. Further evaluation by a licensed HVAC contractor is recommended."),
            DefectOption("Near End of Service Life", "The heating system is approaching or has exceeded its typical service life of 15-20 years. Budget for replacement and have the system serviced annually."),
            DefectOption("Rust/Deterioration on Heat Exchanger", "Rust or deterioration was observed on the heat exchanger. A cracked heat exchanger can allow combustion gases including carbon monoxide to enter the living space. Evaluation by a licensed HVAC contractor is strongly recommended."),
            DefectOption("Unusual Noise/Operation", "The heating system was observed to operate with unusual noise or cycling. Further evaluation by a licensed HVAC contractor is recommended."),
            DefectOption("No Annual Service Record", "No evidence of recent annual service was observed. Annual service of the heating system is recommended."),
            DefectOption("Operating Normally", "The heating system was tested and observed to be operating normally at the time of inspection.")
        ),
        "hv2" to listOf(
            DefectOption("Not Cooling", "The cooling system did not produce adequate temperature differential at the time of inspection. Further evaluation by a licensed HVAC contractor is recommended."),
            DefectOption("Condenser Dirty", "The condenser coil was observed to be dirty. The condenser should be cleaned by an HVAC contractor to maintain efficiency and prevent compressor failure."),
            DefectOption("Near End of Service Life", "The cooling system is approaching or has exceeded its typical service life of 15-20 years. Budget for replacement."),
            DefectOption("Refrigerant Lines Uninsulated", "The refrigerant suction line was observed to be uninsulated at one or more locations. Insulation should be restored to maintain system efficiency."),
            DefectOption("Condenser Pad Uneven", "The condenser unit pad was observed to be uneven. The condenser should be level to ensure proper operation and prevent compressor damage."),
            DefectOption("Operating Normally", "The cooling system was tested and observed to be operating normally with adequate temperature differential.")
        ),
        "hv3" to listOf(
            DefectOption("Ductwork Disconnected", "One or more duct connections were observed to be disconnected. Disconnected ducts result in conditioned air being delivered to unconditioned spaces."),
            DefectOption("Ductwork Damaged", "Ductwork was observed to be damaged or deteriorating at one or more locations. Damaged ductwork should be repaired or replaced."),
            DefectOption("Ductwork Uninsulated in Unconditioned Space", "Supply or return ductwork was observed to be uninsulated in an unconditioned space. Ductwork in unconditioned spaces should be insulated to maintain efficiency."),
            DefectOption("Good Condition", "Ductwork appeared to be in good condition with no significant deficiencies observed.")
        ),
        "hv4" to listOf(
            DefectOption("Flue Pipe Disconnected", "The furnace flue pipe was observed to be disconnected. This allows combustion gases including carbon monoxide to enter the living space and is an immediate safety hazard."),
            DefectOption("Improper Flue Pitch", "The flue pipe was observed to not have adequate pitch toward the chimney or vent termination. Combustion gases must flow upward and the pitch should be corrected."),
            DefectOption("Flue Pipe Corroded", "The flue pipe was observed to be corroded. Corroded flue pipes can leak combustion gases and should be replaced."),
            DefectOption("Combustion Air Inadequate", "Adequate combustion air supply for the fuel-burning equipment was not observed. Combustion air is required for safe and efficient operation."),
            DefectOption("Good Condition", "Flue venting and combustion air appeared to be properly configured.")
        ),
        "hv5" to listOf(
            DefectOption("Thermostat Not Functioning", "The thermostat was observed to not function properly. The thermostat should be repaired or replaced."),
            DefectOption("Thermostat Improperly Located", "The thermostat appeared to be improperly located near a heat source or drafty area, which can cause improper system operation."),
            DefectOption("Good Condition", "The thermostat was observed to be functioning properly.")
        ),
        "hv6" to listOf(
            DefectOption("Filter Dirty", "The HVAC filter was observed to be dirty and in need of replacement. Filters should be replaced every 1-3 months to maintain system efficiency and air quality."),
            DefectOption("Filter Absent", "The HVAC filter was observed to be absent. Operating the system without a filter allows dust and debris to accumulate in the system and should be corrected immediately."),
            DefectOption("Filter Clean", "The HVAC filter was observed to be clean and in good condition.")
        ),
        "hv7" to listOf(
            DefectOption("Gas Leak Suspected", "An odor consistent with natural gas was detected near gas connections. The gas company should be contacted immediately and the area ventilated."),
            DefectOption("Flexible Connector Improper", "The gas flexible connector at one or more appliances was observed to be improper or damaged. Connectors should be replaced by a licensed plumber or gas contractor."),
            DefectOption("Sediment Trap Absent", "A sediment trap (drip leg) was not observed at the gas meter or appliance connections. Sediment traps are recommended to protect appliances from debris in the gas supply."),
            DefectOption("Good Condition", "Gas connections and piping appeared to be in good condition with no evidence of leaks.")
        ),
        "hv8" to listOf(
            DefectOption("Damper Not Functioning", "The fireplace damper was observed to not function properly. A properly functioning damper is required to prevent heat loss and animal entry."),
            DefectOption("Firebox Deterioration", "Deterioration was observed in the firebox including cracked firebrick or failing mortar. The firebox should be repaired by a qualified mason before use."),
            DefectOption("No Spark Arrestor", "No spark arrestor was observed on the chimney. A spark arrestor prevents embers from exiting the chimney and causing fires."),
            DefectOption("Chimney Not Cleaned", "The chimney flue appeared to have creosote buildup. The chimney should be cleaned and inspected by a certified chimney sweep before use."),
            DefectOption("Good Condition", "The fireplace was observed to be in good condition and functioning properly.")
        ),
        "hv9" to listOf(
            DefectOption("Exhaust Fan Not Working", "The kitchen exhaust fan was observed to not function properly. The fan should be repaired or replaced."),
            DefectOption("Venting into Attic/Wall", "The kitchen exhaust fan was observed to vent into the attic or wall cavity rather than to the exterior. This introduces moisture and grease into the structure and must be corrected."),
            DefectOption("Recirculating Only - No Exterior Vent", "The kitchen exhaust fan is a recirculating type with no exterior duct. While functional, an exterior-vented range hood is recommended for better air quality."),
            DefectOption("Good Condition", "The kitchen exhaust fan was observed to be functioning properly and venting to the exterior.")
        ),
        "hv10" to listOf(
            DefectOption("Exhaust Fan Not Working", "The bathroom exhaust fan in one or more bathrooms was observed to not function. Exhaust fans are important for moisture control and should be repaired or replaced."),
            DefectOption("Fan Venting into Attic", "One or more bathroom exhaust fans were observed to be venting into the attic. This introduces moisture into the attic space and must be corrected by routing the duct to an exterior termination."),
            DefectOption("Fan Noisy/Inefficient", "One or more bathroom exhaust fans were observed to be excessively noisy. Replacement with a modern efficient fan is recommended."),
            DefectOption("Good Condition", "Bathroom exhaust fans were observed to be functioning and venting to the exterior.")
        ),

        // ─── PLUMBING ──────────────────────────────────────────────
        "pl1" to listOf(
            DefectOption("Galvanized Pipe Deterioration", "Galvanized steel supply piping was observed. Galvanized pipe is subject to interior corrosion over time resulting in reduced flow and discolored water. Replacement with modern piping material should be budgeted."),
            DefectOption("Active Leak", "An active leak was observed at one or more supply connections. Leaks should be repaired by a licensed plumber immediately to prevent water damage."),
            DefectOption("Polybutylene Pipe", "Polybutylene supply piping was observed. Polybutylene pipe has a history of failure and many insurers will not cover homes with this piping. Replacement is strongly recommended."),
            DefectOption("Good Condition", "Water supply lines appeared to be in good condition with no active leaks observed.")
        ),
        "pl2" to listOf(
            DefectOption("Slow Drains", "Slow draining was observed at one or more fixtures. Drains should be cleared to ensure proper function."),
            DefectOption("Active Drain Leak", "An active leak was observed at one or more drain connections. Leaks should be repaired by a licensed plumber."),
            DefectOption("Improper Venting", "The drain, waste, and vent system appeared to have improper venting. Signs included slow drains and gurgling. Evaluation by a licensed plumber is recommended."),
            DefectOption("Good Condition", "The drain, waste, and vent system appeared to be functioning properly.")
        ),
        "pl3" to listOf(
            DefectOption("Near End of Service Life", "The water heater is approaching or has exceeded its typical service life of 8-12 years. Budget for replacement in the near future."),
            DefectOption("No Expansion Tank", "No expansion tank was observed on a closed plumbing system. An expansion tank is required to prevent excessive pressure buildup and protect the water heater."),
            DefectOption("TPR Valve Concern", "The temperature and pressure relief valve discharge pipe was observed to be improper or absent. The discharge pipe must terminate within 6 inches of the floor or to an approved location. This is a critical safety device."),
            DefectOption("Improper Venting", "The water heater flue venting was observed to be improperly installed. Improper venting can allow combustion gases including carbon monoxide to enter the living space."),
            DefectOption("Rust/Corrosion", "Rust or corrosion was observed on the water heater tank or connections. This may indicate the tank is failing and should be evaluated."),
            DefectOption("No Seismic Straps", "Seismic straps were not observed on the water heater. Seismic strapping is required in many jurisdictions to prevent the water heater from tipping during seismic activity."),
            DefectOption("Operating Normally", "The water heater was observed to be operating normally with no significant deficiencies noted.")
        ),
        "pl4" to listOf(
            DefectOption("Leak Under Kitchen Sink", "An active leak or evidence of past leaking was observed under the kitchen sink. The source should be identified and repaired by a licensed plumber."),
            DefectOption("Faucet Dripping", "The kitchen faucet was observed to be dripping. The faucet should be repaired or replaced."),
            DefectOption("Dishwasher Not Draining", "The dishwasher was observed to not drain properly. Further evaluation is recommended."),
            DefectOption("High Loop Absent at Dishwasher", "A high loop or air gap was not observed at the dishwasher drain. A high loop prevents contaminated water from siphoning back into the dishwasher."),
            DefectOption("Good Condition", "Kitchen plumbing and fixtures appeared to be in good condition and functioning properly.")
        ),
        "pl5" to listOf(
            DefectOption("Toilet Running", "The toilet was observed to be continuously running. The flush mechanism should be repaired to prevent water waste."),
            DefectOption("Toilet Loose at Floor", "The toilet was observed to be loose at the floor. A loose toilet can damage the wax ring seal. The toilet should be re-secured."),
            DefectOption("Slow Drain at Tub/Shower", "Slow draining was observed at the tub or shower. The drain should be cleared."),
            DefectOption("Caulk Failing", "Caulk was observed to be failing at the tub or shower surround. Failed caulk allows water intrusion and should be replaced."),
            DefectOption("Good Condition", "Bathroom 1 fixtures and plumbing appeared to be in good condition and functioning properly.")
        ),
        "pl6" to listOf(
            DefectOption("Toilet Running", "The toilet was observed to be continuously running. The flush mechanism should be repaired."),
            DefectOption("Toilet Loose at Floor", "The toilet was observed to be loose at the floor and should be re-secured."),
            DefectOption("Slow Drain", "Slow draining was observed and should be cleared."),
            DefectOption("Good Condition", "Bathroom 2 fixtures and plumbing appeared to be in good condition.")
        ),
        "pl7" to listOf(
            DefectOption("Toilet Running", "The toilet was observed to be continuously running. The flush mechanism should be repaired."),
            DefectOption("Good Condition", "Bathroom 3 fixtures and plumbing appeared to be in good condition."),
            DefectOption("Not Applicable", "Bathroom 3 was not present at this property.")
        ),
        "pl8" to listOf(
            DefectOption("No Washer Drain Pan", "No washer drain pan was observed under the washing machine location. A drain pan with drain is recommended to prevent water damage in the event of a hose failure."),
            DefectOption("Plastic Washer Hoses", "Plastic or rubber washer supply hoses were observed. Braided stainless steel hoses are recommended to reduce the risk of failure."),
            DefectOption("Dryer Not Vented to Exterior", "The dryer was observed to not be vented to the exterior. Dryer vents must terminate at the exterior to prevent moisture and lint buildup."),
            DefectOption("Good Condition", "Laundry and utility connections appeared to be in good condition.")
        ),
        "pl9" to listOf(
            DefectOption("Shut-Off Not Accessible", "The main water shut-off valve was not readily accessible. The location of the main shut-off should be known for emergency response."),
            DefectOption("Valve Not Fully Operational", "The main water shut-off valve appeared to not be fully operational. The valve should be replaced by a licensed plumber."),
            DefectOption("Location Identified", "The main water shut-off valve was identified and appeared to be functional.")
        ),
        "pl10" to listOf(
            DefectOption("Sump Pump Not Running", "The sump pump did not operate when tested. The pump should be serviced or replaced."),
            DefectOption("No Battery Backup", "No battery backup was observed on the sump pump. A battery backup is recommended to provide protection during power outages."),
            DefectOption("No Check Valve", "A check valve was not observed on the sump pump discharge line. A check valve prevents water from flowing back into the pit."),
            DefectOption("Not Present", "A sump pump was not present. Based on observed conditions, installation may be beneficial."),
            DefectOption("Operating Normally", "The sump pump was tested and observed to be operating normally.")
        ),
        "pl11" to listOf(
            DefectOption("Hose Bib Leaking", "One or more exterior hose bibs were observed to be leaking. Leaking hose bibs should be repaired or replaced."),
            DefectOption("No Backflow Preventer", "Backflow prevention devices were not observed on the hose bibs. Backflow preventers are recommended to prevent contamination of the potable water supply."),
            DefectOption("No Freeze Protection", "Anti-siphon or frost-free hose bibs were not observed. In cold climates, frost-free hose bibs are recommended to prevent freezing."),
            DefectOption("Good Condition", "Exterior hose bibs were observed to be in good condition.")
        ),

        // ─── INTERIOR ──────────────────────────────────────────────
        "in1" to listOf(
            DefectOption("Water Staining on Ceiling", "Water staining was observed on the ceiling at one or more locations. The source of moisture should be identified and corrected, and affected surfaces repaired."),
            DefectOption("Cracks in Walls/Ceilings", "Cracks were observed in the walls or ceilings. Significant cracks should be evaluated to rule out structural movement."),
            DefectOption("Mold-Like Substance", "A mold-like substance was observed. Further evaluation by a qualified mold inspector is recommended. This is a potential health concern."),
            DefectOption("Good Condition", "Interior walls and ceilings appeared to be in good condition with no significant deficiencies noted.")
        ),
        "in2" to listOf(
            DefectOption("Floor Squeaking", "Squeaking was observed in the floor at one or more locations. While typically a cosmetic issue, squeaking can sometimes indicate loose subfloor connections."),
            DefectOption("Floor Soft/Spongy", "The floor felt soft or spongy at one or more locations, which may indicate water damage to the subfloor. Further evaluation is recommended."),
            DefectOption("Floor Covering Damaged", "Floor covering was observed to be damaged at one or more locations."),
            DefectOption("Good Condition", "Interior floors appeared to be in good condition.")
        ),
        "in3" to listOf(
            DefectOption("Failed Seal - Fogged Glass", "One or more interior window insulated glass units showed evidence of seal failure. Affected units should be replaced."),
            DefectOption("Window Not Operating", "One or more windows were observed to not open, close, or latch properly. Windows should be repaired."),
            DefectOption("Good Condition", "Interior windows and sills appeared to be in good condition.")
        ),
        "in4" to listOf(
            DefectOption("Door Not Latching", "One or more interior doors were observed to not latch properly. Door hardware or the door frame should be adjusted or repaired."),
            DefectOption("Door Rubbing/Sticking", "One or more interior doors were observed to rub or stick. This can indicate settlement or moisture-related swelling."),
            DefectOption("Good Condition", "Interior doors and hardware appeared to be in good condition and functioning properly.")
        ),
        "in5" to listOf(
            DefectOption("Handrail Missing", "A handrail was absent on one or more interior stairways with 3 or more risers. Handrails are required for safety."),
            DefectOption("Handrail Loose", "The handrail was observed to be loose. Handrails must be capable of withstanding a 200-pound load and should be properly secured."),
            DefectOption("Baluster Spacing Excessive", "Baluster spacing exceeded 4 inches, presenting a safety hazard for young children."),
            DefectOption("Good Condition", "Interior stairs, handrails, and guardrails appeared to be in good condition.")
        ),
        "in6" to listOf(
            DefectOption("Cabinet Doors/Drawers Not Functioning", "Cabinet doors or drawers were observed to not function properly. Hardware should be adjusted or replaced."),
            DefectOption("Countertop Damage", "Damage was observed to the countertop including chips, cracks, or separation at joints."),
            DefectOption("Appliance Not Functioning", "One or more kitchen appliances were observed to not function during testing."),
            DefectOption("Good Condition", "Kitchen cabinets, counters, and appliances appeared to be in good condition and functioning properly.")
        ),
        "in7" to listOf(
            DefectOption("Caulk Failing at Tub/Shower", "The caulk at the tub or shower surround was observed to be failing. Failed caulk allows water intrusion behind the surround and should be replaced."),
            DefectOption("Grout Missing/Cracked", "Grout was observed to be missing or cracked in the tile surround. Missing grout allows water infiltration and should be repaired promptly."),
            DefectOption("Soft Subfloor at Toilet", "The subfloor adjacent to the toilet was observed to be soft, indicating possible water damage from a leaking wax ring. Further evaluation by a licensed plumber is recommended."),
            DefectOption("Tile Loose", "One or more wall or floor tiles were observed to be loose. Loose tiles should be re-secured to prevent water infiltration."),
            DefectOption("Good Condition", "Bathroom tile, caulk, and waterproofing appeared to be in good condition.")
        ),
        "in8" to listOf(
            DefectOption("Smoke Detectors Absent", "Smoke detectors were absent in one or more required locations. Detectors should be installed in each bedroom, outside each sleeping area, and on each level."),
            DefectOption("Detectors Not Functioning", "One or more smoke detectors did not function when tested. Detectors should be replaced."),
            DefectOption("Detectors Present", "Smoke detectors were present and functional at all required locations.")
        ),
        "in9" to listOf(
            DefectOption("CO Detectors Absent", "Carbon monoxide detectors were absent. CO detectors are required within 15 feet of each sleeping room where fuel-burning appliances or an attached garage are present."),
            DefectOption("CO Detectors Present", "Carbon monoxide detectors were present and functional.")
        ),
        "in10" to listOf(
            DefectOption("Attic Hatch Not Insulated", "The attic access hatch was observed to lack insulation. The hatch should be insulated to the same R-value as the surrounding attic insulation."),
            DefectOption("Attic Hatch Not Sealed", "The attic access hatch was observed to lack weatherstripping or a proper seal. Air sealing the hatch improves energy efficiency."),
            DefectOption("Good Condition", "The attic access hatch appeared to be in good condition.")
        ),

        // ─── INSULATION ────────────────────────────────────────────
        "is1" to listOf(
            DefectOption("Insufficient Insulation", "Attic insulation was observed to be below the recommended R-value for this climate zone. Adding insulation is recommended to improve energy efficiency."),
            DefectOption("Insulation Blocking Vents", "Insulation was observed to be blocking soffit vents. Baffles should be installed to maintain airflow from the soffit vents."),
            DefectOption("Insulation Settled/Compressed", "Attic insulation appeared to be significantly settled or compressed, reducing its effective R-value. Additional insulation is recommended."),
            DefectOption("Good Condition", "Attic insulation appeared to be adequate and in good condition.")
        ),
        "is2" to listOf(
            DefectOption("Inadequate Ventilation", "Attic ventilation appeared to be inadequate. Inadequate ventilation can lead to moisture buildup, ice damming, and premature roof deterioration."),
            DefectOption("Exhaust Fan into Attic", "One or more bathroom exhaust fans were observed to be venting into the attic. This introduces moisture into the attic and must be corrected by routing ducts to the exterior."),
            DefectOption("Good Condition", "Attic ventilation appeared to be adequate.")
        ),
        "is3" to listOf(
            DefectOption("Insulation Absent in Crawlspace", "Insulation was absent from the crawlspace floor joist bays. Insulation should be installed to improve energy efficiency."),
            DefectOption("Insulation Falling Down", "Crawlspace insulation was observed to be falling down from the floor joist bays. Insulation should be re-secured with appropriate supports."),
            DefectOption("Good Condition", "Crawlspace and basement insulation appeared to be in good condition.")
        ),
        "is4" to listOf(
            DefectOption("Vapor Barrier Absent", "A vapor barrier was absent from the crawlspace floor. A minimum 6-mil polyethylene vapor barrier should be installed to prevent ground moisture from entering the structure."),
            DefectOption("Vapor Barrier Damaged", "The crawlspace vapor barrier was observed to be torn or damaged. The vapor barrier should be repaired or replaced."),
            DefectOption("Good Condition", "The vapor barrier appeared to be in good condition.")
        ),
        "is5" to listOf(
            DefectOption("Wall Insulation Absent - Visible", "Wall insulation was observed to be absent where visible. Adding insulation to exterior walls improves energy efficiency."),
            DefectOption("Good Condition", "Wall insulation appeared to be present where visible.")
        ),
        "is6" to listOf(
            DefectOption("Fan Venting into Attic/Wall", "One or more exhaust fan ducts were observed to terminate in the attic or wall cavity. All exhaust fans must vent to the exterior."),
            DefectOption("Duct Disconnected", "An exhaust fan duct was observed to be disconnected. The duct should be reconnected and secured."),
            DefectOption("Good Condition", "Exhaust fan duct terminations appeared to be properly routed to the exterior.")
        ),

        // ─── GARAGE ────────────────────────────────────────────────
        "ga1" to listOf(
            DefectOption("Auto-Reverse Not Functioning", "The automatic reversal safety feature of the garage door did not function properly during testing. This is a safety hazard and should be repaired immediately by a qualified garage door technician."),
            DefectOption("Photo Eye Misaligned", "The photo eye sensors appeared to be misaligned. The auto-reverse safety feature may not operate correctly and the sensors should be realigned."),
            DefectOption("Door Off Track", "The garage door appeared to be off track at one or more locations. The door should be serviced by a qualified garage door technician."),
            DefectOption("Good Condition", "The garage door and safety features were observed to be functioning properly.")
        ),
        "ga2" to listOf(
            DefectOption("Opener Not Functioning", "The garage door opener was observed to not function properly. Further evaluation by a qualified technician is recommended."),
            DefectOption("No Auto-Reverse", "The garage door opener did not have a functioning auto-reverse feature. This is a safety hazard and the opener should be repaired or replaced."),
            DefectOption("Good Condition", "The garage door opener was observed to be functioning properly.")
        ),
        "ga3" to listOf(
            DefectOption("Not Fire-Rated Door", "The door between the garage and living space does not appear to be a fire-rated door. A minimum 20-minute fire-rated door is required at this location for life safety."),
            DefectOption("Door Not Self-Closing", "The door between the garage and living space was not self-closing. A self-closing mechanism is required at this location to limit fire spread."),
            DefectOption("Door Has Pet Door", "A pet door was observed in the fire-rated door between the garage and living space. Pet doors compromise the fire rating of the door and should be removed."),
            DefectOption("Good Condition", "The fire-rated door between the garage and living space appeared to be proper and functioning.")
        ),
        "ga4" to listOf(
            DefectOption("Garage Floor Cracked", "The garage floor was observed to be significantly cracked. While typically a cosmetic concern, cracks should be sealed to prevent moisture intrusion."),
            DefectOption("Garage Floor Heaved", "The garage floor was observed to be heaved or uneven, presenting a trip hazard."),
            DefectOption("Good Condition", "The garage floor appeared to be in good condition.")
        ),
        "ga5" to listOf(
            DefectOption("Drywall Not Fire-Rated", "The drywall in the garage appeared to not be the required 5/8-inch Type X fire-rated drywall. Fire-rated drywall is required on walls and ceilings adjacent to living space."),
            DefectOption("Drywall Damaged/Penetrations", "The drywall in the garage was observed to be damaged or have penetrations. Openings in the fire separation must be properly patched to maintain the fire rating."),
            DefectOption("Good Condition", "Garage walls and ceiling appeared to be in good condition with proper fire separation.")
        ),
        "ga6" to listOf(
            DefectOption("Unit Not Functioning", "The overhead garage heater did not function when tested. Further evaluation by a qualified HVAC contractor is recommended."),
            DefectOption("Unit Near End of Life", "The overhead garage heater appeared to be near the end of its serviceable life."),
            DefectOption("Good Condition", "The overhead garage heater was observed to be functioning properly."),
            DefectOption("Not Present", "No overhead garage heater was present.")
        ),
        "ga7" to listOf(
            DefectOption("Attic Access Not Insulated", "The attic access in the garage was observed to lack insulation. The hatch should be insulated."),
            DefectOption("Good Condition", "The attic access in the garage appeared to be in good condition."),
            DefectOption("Not Present", "No attic access was present in the garage.")
        )
    )

    fun getDefectsForItem(itemId: String): List<DefectOption> = defects[itemId] ?: emptyList()
}



