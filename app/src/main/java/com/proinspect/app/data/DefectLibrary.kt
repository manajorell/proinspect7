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
            DefectOption("Gutters Pulling Away", "Gutters were observed to be pulling away from the fascia at one or more locations. This should be re-secured to prevent water damage to the fascia and foundation."),
            DefectOption("Debris Buildup in Gutters", "Significant debris buildup was observed in the gutters. Gutters should be cleaned to ensure proper drainage away from the foundation."),
            DefectOption("Downspout Discharges at Foundation", "One or more downspouts discharge directly at the foundation. Extensions should be installed to direct water away from the structure."),
            DefectOption("Gutters Sagging", "Gutters were observed to be sagging at one or more locations, preventing proper drainage. Gutters should be re-pitched or re-secured."),
            DefectOption("Holes/Rust in Gutters", "Holes or rust-through were observed in the gutters. Affected sections should be repaired or replaced."),
            DefectOption("Good Condition", "Gutters and downspouts were observed to be in good condition and properly directing water away from the structure.")
        ),
        "rf3" to listOf(
            DefectOption("Flashing Separation", "Flashing was observed to be separating from the adjoining surface. This condition allows water infiltration and should be repaired by a licensed roofing contractor."),
            DefectOption("Missing Flashing", "Flashing was observed to be missing at one or more locations. Proper flashing is critical to preventing water intrusion and should be installed by a licensed roofing contractor."),
            DefectOption("Sealed with Caulk Only", "Flashing transitions were observed to be sealed with caulk only, without proper step or counter flashing. Caulk is a temporary repair and proper flashing should be installed."),
            DefectOption("Rusted/Corroded Flashing", "Flashing was observed to be rusted or corroded, compromising its ability to seal against water intrusion. Replacement by a licensed roofing contractor is recommended."),
            DefectOption("Good Condition", "Flashing was observed to be properly installed and in good condition at the time of inspection.")
        ),
        "rf4" to listOf(
            DefectOption("Skylight Leaking", "Evidence of water intrusion was observed around one or more skylights. The skylight flashing and seals should be evaluated and repaired by a licensed roofing contractor."),
            DefectOption("Penetration Not Flashed", "One or more roof penetrations were observed to lack proper flashing. Flashing should be installed to prevent water intrusion."),
            DefectOption("Good Condition", "Skylights and roof penetrations were observed to be in good condition with no evidence of leakage.")
        ),
        "rf5" to listOf(
            DefectOption("Rafters Cracked/Broken", "One or more roof rafters or truss members were observed to be cracked or broken. This is a structural concern and should be evaluated by a licensed structural engineer."),
            DefectOption("Ridge Board Sagging", "The roof ridge board appeared to be sagging, indicating possible structural inadequacy or settlement. Evaluation by a structural engineer is recommended."),
            DefectOption("Truss Modified", "Evidence of field modifications to roof trusses was observed. Truss modifications must be engineered and approved. Evaluation by a structural engineer is recommended."),
            DefectOption("Inadequate Ventilation", "Attic ventilation appeared to be inadequate. Inadequate ventilation can lead to moisture buildup, ice damming, and premature roof deterioration."),
            DefectOption("Evidence of Water Intrusion", "Evidence of water intrusion was observed in the attic. The source should be identified and corrected."),
            DefectOption("Good Condition", "Roof structure and attic were observed to be in good condition with no significant deficiencies noted.")
        ),
        "rf6" to listOf(
            DefectOption("Chimney Cap Missing", "The chimney cap was observed to be missing. A chimney cap prevents water, debris, and animals from entering the flue and should be installed promptly."),
            DefectOption("Mortar Deterioration", "The mortar joints in the chimney masonry were observed to be deteriorating. Tuckpointing by a qualified mason is recommended to prevent water intrusion and structural damage."),
            DefectOption("Chimney Crown Cracked", "The chimney crown was observed to be cracked. The crown should be repaired or replaced to prevent water infiltration into the chimney structure."),
            DefectOption("Flashing Failed at Chimney", "The flashing at the chimney base was observed to be failing or improperly installed. Proper flashing should be installed by a licensed roofing contractor."),
            DefectOption("Flue Liner Damaged", "The flue liner appeared to be damaged or deteriorating. A damaged flue liner can allow combustion gases and heat to escape into the surrounding structure. Evaluation by a certified chimney sweep is recommended."),
            DefectOption("Chimney Leaning", "The chimney appeared to be leaning or out of plumb. This is a structural concern and should be evaluated by a licensed mason or structural engineer."),
            DefectOption("Good Condition", "The chimney was observed to be in good condition with cap present and mortar intact.")
        ),

        // ─── EXTERIOR ──────────────────────────────────────────────
        "ex1" to listOf(
            DefectOption("Paint Peeling/Failing", "Paint was observed to be peeling or failing at multiple locations on the exterior siding. Surfaces should be properly prepared and repainted to protect the underlying material."),
            DefectOption("Damaged Siding", "Siding was observed to be damaged at one or more locations. Damaged sections should be repaired or replaced to prevent moisture intrusion."),
            DefectOption("Wood Rot", "Wood rot was observed on the siding at one or more locations. Rotted wood should be replaced and the area properly primed and painted to prevent recurrence."),
            DefectOption("Siding Near Grade", "Siding was observed to be within 6 inches of grade at one or more locations, which can lead to moisture damage and insect infestation."),
            DefectOption("Gaps/Missing Caulk", "Gaps or missing caulk were observed at siding joints and penetrations. Caulking should be applied to prevent moisture and air infiltration."),
            DefectOption("Good Condition", "The exterior wall covering, flashing, and trim were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex2" to listOf(
            DefectOption("Door Not Sealing Properly", "One or more exterior doors were observed to not seal properly, allowing air and potential moisture infiltration. Weather stripping should be replaced."),
            DefectOption("Door Hardware Defective", "Door hardware including locks or latches were observed to be defective. Hardware should be repaired or replaced for security and function."),
            DefectOption("Door Frame Rot", "Wood rot was observed in one or more exterior door frames. Rotted sections should be repaired or replaced."),
            DefectOption("Good Condition", "Exterior doors were observed to be in good condition and functioning properly.")
        ),
        "ex3" to listOf(
            DefectOption("Failed Seal - Fogged Glass", "One or more window insulated glass units showed evidence of seal failure indicated by fogging or condensation between the panes. Affected units should be replaced."),
            DefectOption("Window Frame Rot", "Wood rot was observed in one or more window frames. Rotted sections should be repaired or the window replaced to prevent further deterioration."),
            DefectOption("Windows Not Opening/Closing", "One or more windows were observed to not open, close, or latch properly. Windows should be repaired to function as intended."),
            DefectOption("Caulk Failing at Windows", "Caulk was observed to be failing around window frames. Re-caulking is recommended to prevent moisture and air infiltration."),
            DefectOption("Good Condition", "Exterior windows were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex4" to listOf(
            DefectOption("Deck Boards Deteriorating", "Deck boards were observed to be deteriorating due to weathering and age. Replacement of affected boards is recommended before they become a safety concern."),
            DefectOption("Missing Handrail", "Handrails were absent on one or more stairways with 3 or more risers. Handrails are required by code and should be installed for safety."),
            DefectOption("Baluster Spacing Excessive", "Baluster spacing exceeds 4 inches, which presents a safety hazard for young children. Recommend adding balusters to achieve compliant spacing."),
            DefectOption("Ledger Board Concern", "The deck ledger board connection to the house appeared to be improperly fastened or showed signs of deterioration. Evaluation by a licensed contractor is recommended as this is a structural concern."),
            DefectOption("Posts Not Properly Anchored", "Deck posts were observed to not be properly anchored. Posts should be secured with approved post base hardware."),
            DefectOption("Good Condition", "The deck, balcony, porch, and railings were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex5" to listOf(
            DefectOption("Fascia Rotted", "The fascia board was observed to be rotted at one or more locations. Rotted fascia should be replaced to prevent further deterioration and structural damage."),
            DefectOption("Soffit Damaged", "The soffit was observed to be damaged or missing at one or more locations. Damaged soffit should be repaired to prevent animal entry and moisture intrusion."),
            DefectOption("Paint Peeling on Fascia", "Paint was observed to be peeling on the fascia boards. The fascia should be properly prepared and repainted to protect the wood."),
            DefectOption("Good Condition", "Eaves, soffits, and fascias were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex6" to listOf(
            DefectOption("Negative Grading", "The ground was observed to slope toward the foundation at one or more locations. Negative grading promotes water infiltration and should be corrected by adding fill and regrading."),
            DefectOption("Vegetation Against Structure", "Vegetation was observed growing against the structure. Vegetation retains moisture against the siding and should be trimmed back a minimum of 12 inches."),
            DefectOption("Window Well No Drain", "One or more window wells lacked an adequate drain. Window wells should have gravel and a drain to prevent water accumulation."),
            DefectOption("Good Condition", "Grading and drainage appeared to be directing water away from the foundation adequately.")
        ),
        "ex7" to listOf(
            DefectOption("Walkway Cracked/Heaved", "The walkway or driveway was observed to be significantly cracked or heaved, presenting a trip hazard. Affected areas should be repaired or replaced."),
            DefectOption("Settlement at Foundation", "The driveway or walkway was observed to have settled adjacent to the foundation, directing water toward the structure."),
            DefectOption("Good Condition", "Driveways and walkways were observed to be in good condition with no significant deficiencies noted.")
        ),
        "ex8" to listOf(
            DefectOption("Retaining Wall Leaning", "A retaining wall was observed to be leaning or showing signs of movement. Evaluation by a licensed structural engineer is recommended."),
            DefectOption("Mortar Deteriorating", "Mortar joints in the retaining wall were observed to be deteriorating. Tuckpointing is recommended to prevent further deterioration."),
            DefectOption("Good Condition", "Retaining walls were observed to be in good condition with no significant movement noted.")
        ),

        // ─── STRUCTURE ─────────────────────────────────────────────
        "st1" to listOf(
            DefectOption("Cracks - Horizontal", "Horizontal cracks were observed in the foundation wall. Horizontal cracks can indicate lateral soil pressure and should be evaluated by a licensed structural engineer."),
            DefectOption("Cracks - Stair Step", "Stair-step cracks were observed in the foundation masonry. This pattern typically indicates differential settlement and should be evaluated by a structural engineer."),
            DefectOption("Cracks - Vertical", "Vertical cracks were observed in the foundation wall. Cracks should be sealed to prevent water infiltration and monitored for movement."),
            DefectOption("Efflorescence/Water Staining", "Efflorescence or water staining was observed on the foundation walls, indicating past or ongoing water infiltration. The source should be identified and corrected."),
            DefectOption("Good Condition", "The foundation was observed to be in good condition with no significant cracking or movement noted.")
        ),
        "st2" to listOf(
            DefectOption("Standing Water in Crawlspace", "Standing water was observed in the crawlspace. This condition promotes mold growth and structural deterioration and should be corrected immediately."),
            DefectOption("High Humidity/Condensation", "High humidity or condensation was observed in the crawlspace. A vapor barrier and/or mechanical ventilation should be considered."),
            DefectOption("Vapor Barrier Absent/Damaged", "The vapor barrier in the crawlspace was absent or damaged. A minimum 6-mil polyethylene vapor barrier should be installed over the soil."),
            DefectOption("Water Staining in Basement", "Water staining was observed in the basement indicating past or recurring moisture intrusion. The source should be identified and corrected."),
            DefectOption("Active Seepage", "Active water seepage was observed. Waterproofing measures should be evaluated by a qualified contractor."),
            DefectOption("Good Condition", "The basement and crawlspace were observed to be in good condition with no significant moisture concerns.")
        ),
        "st3" to listOf(
            DefectOption("Sagging Floor Joists", "Floor joists were observed to be sagging at one or more locations. This condition should be evaluated by a licensed structural engineer."),
            DefectOption("Notched/Drilled Improperly", "Floor joists were observed to have been notched or drilled outside of allowable limits, potentially compromising structural integrity."),
            DefectOption("Insect Damage", "Evidence of insect damage was observed on floor framing. A licensed pest inspector should evaluate the extent of damage."),
            DefectOption("Good Condition", "Floor structure was observed to be in good condition with no significant deficiencies noted.")
        ),
        "st4" to listOf(
            DefectOption("Wall Framing Concern", "Concerns were observed with the wall framing including improper notching, missing blocking, or signs of movement. Evaluation by a licensed contractor is recommended."),
            DefectOption("Good Condition", "Wall structure appeared to be in good condition where visible.")
        ),
        "st5" to listOf(
            DefectOption("Ceiling Sag Observed", "Ceiling sag was observed at one or more locations. This condition should be evaluated to rule out structural concerns or water damage."),
            DefectOption("Good Condition", "Ceiling structure appeared to be in good condition.")
        ),
        "st6" to listOf(
            DefectOption("Rafters Cracked/Broken", "One or more roof rafters or truss members were observed to be cracked or broken. This is a structural concern and should be evaluated by a licensed structural engineer."),
            DefectOption("Ridge Board Sagging", "The roof ridge board appeared to be sagging. Evaluation by a structural engineer is recommended."),
            DefectOption("Truss Modified", "Evidence of field modifications to roof trusses was observed. Evaluation by a structural engineer is recommended."),
            DefectOption("Good Condition", "Roof structure was observed to be in good condition with no significant deficiencies noted.")
        ),

        // ─── ELECTRICAL ────────────────────────────────────────────
        "el1" to listOf(
            DefectOption("Service Entry Damage", "The service entry cable was observed to be damaged or deteriorating. This should be evaluated and repaired by a licensed electrician."),
            DefectOption("Drip Loop Absent", "A drip loop was not present at the service entry. A drip loop prevents water from following the conductors into the weatherhead."),
            DefectOption("Clearance Concern", "The service entry conductors did not appear to meet minimum clearance requirements. Evaluation by the utility company and a licensed electrician is recommended."),
            DefectOption("Good Condition", "The service entrance conductors appeared to be in good condition.")
        ),
        "el2" to listOf(
            DefectOption("Double-Tapped Breakers", "Double-tapped circuit breakers were observed in the electrical panel. Most breakers are designed for only one conductor and this condition should be corrected by a licensed electrician."),
            DefectOption("Panel Corrosion", "Corrosion was observed inside the electrical panel. This should be evaluated by a licensed electrician as it may indicate moisture infiltration."),
            DefectOption("Overheating Evidence", "Evidence of overheating was observed including discolored wires or breakers. This is a fire hazard and should be evaluated by a licensed electrician immediately."),
            DefectOption("Breakers Not Labeled", "Circuit breakers in the panel were not adequately labeled. All breakers should be labeled for safety and emergency response."),
            DefectOption("Open Knockouts", "Open knockouts were observed in the electrical panel. Open knockouts present a shock hazard and should be filled with appropriate closure plugs."),
            DefectOption("Good Condition", "The main electrical panel was observed to be in good condition with no significant deficiencies noted.")
        ),
        "el3" to listOf(
            DefectOption("Aluminum Wiring", "Aluminum branch circuit wiring was observed. Aluminum wiring requires special devices and connections. Evaluation by a licensed electrician is recommended."),
            DefectOption("Knob and Tube Wiring", "Knob and tube wiring was observed. This older wiring system lacks a ground conductor and should be evaluated by a licensed electrician."),
            DefectOption("Exposed Wiring", "Exposed wiring was observed at one or more locations. Wiring should be protected by conduit or enclosed within walls."),
            DefectOption("Good Condition", "Branch circuit conductors appeared to be in good condition where visible.")
        ),
        "el4" to listOf(
            DefectOption("Reversed Polarity", "Reversed polarity was detected at one or more outlets. This condition should be corrected by a licensed electrician."),
            DefectOption("Ungrounded Outlets", "Ungrounded outlets were observed at one or more locations. Grounded outlets should be installed or GFCI protection provided."),
            DefectOption("Outlets Not Working", "One or more outlets were observed to not function. Further evaluation by a licensed electrician is recommended."),
            DefectOption("Outlet Missing Cover Plate", "One or more outlets or switches were observed to be missing cover plates. Cover plates should be installed for safety."),
            DefectOption("Good Condition", "Connected devices and fixtures were observed to be in good condition and functioning properly.")
        ),
        "el5" to listOf(
            DefectOption("GFCI Absent - Kitchen", "GFCI protection was absent at kitchen countertop circuits. GFCI protection is required within 6 feet of all sinks and should be installed by a licensed electrician."),
            DefectOption("GFCI Absent - Bathroom", "GFCI protection was absent in one or more bathrooms. GFCI protection is required in all bathrooms and should be installed."),
            DefectOption("GFCI Absent - Exterior", "GFCI protection was absent at one or more exterior outlets. GFCI protection is required for all exterior outlets."),
            DefectOption("GFCI Absent - Garage", "GFCI protection was absent in the garage. GFCI protection is required for all garage outlets."),
            DefectOption("GFCI Not Tripping", "One or more GFCI outlets were observed to not trip when tested. GFCI devices that fail to function should be replaced immediately."),
            DefectOption("AFCI Absent - Bedrooms", "AFCI protection was absent on bedroom circuits. AFCI protection is required on all bedroom circuits in homes built after 2002."),
            DefectOption("GFCI/AFCI Present and Functional", "GFCI and AFCI protection was present and tested functional at all required locations.")
        ),
        "el6" to listOf(
            DefectOption("Smoke Detectors Absent", "Smoke detectors were absent in one or more required locations. Smoke detectors should be installed in each sleeping room, outside each sleeping area, and on each level of the home."),
            DefectOption("CO Detectors Absent", "Carbon monoxide detectors were absent. CO detectors are required within 15 feet of each sleeping room in homes with fuel-burning appliances or attached garages."),
            DefectOption("Detectors Older Than 10 Years", "Smoke detectors appeared to be older than 10 years. Smoke detectors should be replaced every 10 years per manufacturer recommendations."),
            DefectOption("Detectors Present and Tested", "Smoke and CO detectors were present and operational at the time of inspection.")
        ),
        "el7" to listOf(
            DefectOption("Aluminum Wiring Present - Evaluate", "Solid conductor aluminum branch circuit wiring was observed. Aluminum wiring is associated with a higher risk of fire due to its tendency to oxidize and loosen at connections. All devices must be rated AL-CU and connections made with anti-oxidant compound. Evaluation by a licensed electrician is strongly recommended."),
            DefectOption("Improper Devices for Aluminum", "Aluminum branch circuit wiring was observed connected to devices not rated for aluminum. All devices must be CO/ALR or AL-CU rated. Correction by a licensed electrician is required."),
            DefectOption("Anti-Oxidant Compound Absent", "Aluminum wiring connections were observed without anti-oxidant compound. Anti-oxidant compound must be applied at all aluminum wiring connections to prevent oxidation and overheating."),
            DefectOption("Aluminum Wiring - Pigtailed", "Aluminum branch circuit wiring has been pigtailed with copper at device connections. This is an acceptable repair method when properly performed with approved connectors. Verify all connections are properly made."),
            DefectOption("Not Present", "Aluminum branch circuit wiring was not observed. Standard copper wiring was present throughout.")
        ),
        "el8" to listOf(
            DefectOption("100 Amp Service - Adequate", "The electrical service was observed to be 100 amperes. This meets minimum code requirements and is adequate for most homes without electric heat or EV charging."),
            DefectOption("100 Amp Service - Upgrade Recommended", "The electrical service was observed to be 100 amperes. Given the size of the home and modern electrical demands, upgrading to 200-amp service is recommended."),
            DefectOption("200 Amp Service", "The electrical service was observed to be 200 amperes. This is adequate for modern electrical demands including electric appliances and EV charging."),
            DefectOption("60 Amp Service - Upgrade Required", "The electrical service was observed to be only 60 amperes. This is considered inadequate for modern usage and upgrading to a minimum 100-amp service is strongly recommended."),
            DefectOption("Service Amperage Not Determined", "The service amperage could not be determined at the time of inspection. Verification with a licensed electrician is recommended.")
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
            DefectOption("Ductwork Uninsulated", "Supply or return ductwork was observed to be uninsulated in an unconditioned space. Ductwork in unconditioned spaces should be insulated to maintain efficiency."),
            DefectOption("Good Condition", "Duct systems appeared to be in good condition with no significant deficiencies observed.")
        ),
        "hv4" to listOf(
            DefectOption("Flue Pipe Disconnected", "The furnace flue pipe was observed to be disconnected. This allows combustion gases including carbon monoxide to enter the living space and is an immediate safety hazard."),
            DefectOption("Improper Flue Pitch", "The flue pipe was observed to not have adequate pitch toward the chimney or vent termination. The pitch should be corrected."),
            DefectOption("Flue Pipe Corroded", "The flue pipe was observed to be corroded. Corroded flue pipes can leak combustion gases and should be replaced."),
            DefectOption("Combustion Air Inadequate", "Adequate combustion air supply for the fuel-burning equipment was not observed. Combustion air is required for safe and efficient operation."),
            DefectOption("Good Condition", "Vents, flues, and chimneys appeared to be properly configured and in good condition.")
        ),
        "hv5" to listOf(
            DefectOption("Thermostat Not Functioning", "The thermostat was observed to not function properly. The thermostat should be repaired or replaced."),
            DefectOption("Thermostat Improperly Located", "The thermostat appeared to be improperly located near a heat source or drafty area, which can cause improper system operation."),
            DefectOption("Good Condition", "The thermostat was observed to be functioning properly.")
        ),
        "hv6" to listOf(
            DefectOption("No Heat Source - Room Identified", "One or more habitable rooms were observed to lack an adequate heat source. Each habitable room is required to have a heat source capable of maintaining a minimum temperature of 68°F. A licensed HVAC contractor should evaluate and install appropriate heating."),
            DefectOption("Inadequate Heat Source", "The heat source in one or more rooms appeared to be inadequate for the size of the space. Evaluation by a licensed HVAC contractor is recommended."),
            DefectOption("Baseboard Heat Not Functioning", "One or more baseboard heaters were observed to not function. Heaters should be repaired or replaced by a licensed electrician or HVAC contractor."),
            DefectOption("Register Blocked/Closed", "One or more heating registers were observed to be blocked or closed. Registers should be open and unobstructed to allow proper heat distribution."),
            DefectOption("Heat Source Present in All Rooms", "A heat source was observed to be present in all habitable rooms inspected.")
        ),

        // ─── PLUMBING ──────────────────────────────────────────────
        "pl1" to listOf(
            DefectOption("Galvanized Pipe Deterioration", "Galvanized steel supply piping was observed. Galvanized pipe is subject to interior corrosion over time resulting in reduced flow and discolored water. Replacement with modern piping material should be budgeted."),
            DefectOption("Active Leak", "An active leak was observed at one or more supply connections. Leaks should be repaired by a licensed plumber immediately to prevent water damage."),
            DefectOption("Polybutylene Pipe", "Polybutylene supply piping was observed. Polybutylene pipe has a history of failure and many insurers will not cover homes with this piping. Replacement is strongly recommended."),
            DefectOption("Low Water Pressure", "Low water pressure was observed at one or more fixtures. The cause should be investigated by a licensed plumber."),
            DefectOption("Good Condition", "Water supply system and fixtures appeared to be in good condition with no active leaks observed.")
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
            DefectOption("Gas Leak Suspected", "An odor consistent with natural gas was detected near gas connections. The gas company should be contacted immediately and the area ventilated."),
            DefectOption("Flexible Connector Improper", "The gas flexible connector at one or more appliances was observed to be improper or damaged. Connectors should be replaced by a licensed plumber or gas contractor."),
            DefectOption("Sediment Trap Absent", "A sediment trap (drip leg) was not observed at the gas meter or appliance connections. Sediment traps are recommended to protect appliances from debris in the gas supply."),
            DefectOption("Good Condition", "Fuel storage and distribution appeared to be in good condition with no evidence of leaks.")
        ),
        "pl5" to listOf(
            DefectOption("Sump Pump Not Running", "The sump pump did not operate when tested. The pump should be serviced or replaced."),
            DefectOption("No Battery Backup", "No battery backup was observed on the sump pump. A battery backup is recommended to provide protection during power outages."),
            DefectOption("No Check Valve", "A check valve was not observed on the sump pump discharge line. A check valve prevents water from flowing back into the pit."),
            DefectOption("Not Present", "A sump pump was not present. Based on observed conditions, installation may be beneficial."),
            DefectOption("Operating Normally", "The sump pump was tested and observed to be operating normally.")
        ),

        // ─── INTERIOR ──────────────────────────────────────────────
        "in1" to listOf(
            DefectOption("Water Staining on Ceiling", "Water staining was observed on the ceiling at one or more locations. The source of moisture should be identified and corrected, and affected surfaces repaired."),
            DefectOption("Cracks in Walls/Ceilings", "Cracks were observed in the walls or ceilings. Significant cracks should be evaluated to rule out structural movement."),
            DefectOption("Mold-Like Substance", "A mold-like substance was observed. Further evaluation by a qualified mold inspector is recommended. This is a potential health concern."),
            DefectOption("Good Condition", "Interior walls, ceilings, and floors appeared to be in good condition with no significant deficiencies noted.")
        ),
        "in2" to listOf(
            DefectOption("Door Not Latching", "One or more interior doors were observed to not latch properly. Door hardware or the door frame should be adjusted or repaired."),
            DefectOption("Door Rubbing/Sticking", "One or more interior doors were observed to rub or stick. This can indicate settlement or moisture-related swelling."),
            DefectOption("Failed Seal - Fogged Glass", "One or more interior window insulated glass units showed evidence of seal failure. Affected units should be replaced."),
            DefectOption("Window Not Operating", "One or more windows were observed to not open, close, or latch properly. Windows should be repaired."),
            DefectOption("Good Condition", "Interior doors and windows appeared to be in good condition and functioning properly.")
        ),
        "in3" to listOf(
            DefectOption("Handrail Missing", "A handrail was absent on one or more interior stairways with 3 or more risers. Handrails are required for safety."),
            DefectOption("Handrail Loose", "The handrail was observed to be loose. Handrails must be capable of withstanding a 200-pound load and should be properly secured."),
            DefectOption("Baluster Spacing Excessive", "Baluster spacing exceeded 4 inches, presenting a safety hazard for young children."),
            DefectOption("Stair Risers Uneven", "Stair risers were observed to be uneven, presenting a trip hazard. Stairs should be repaired to provide uniform rise."),
            DefectOption("Good Condition", "Stairs, handrails, and guardrails appeared to be in good condition.")
        ),
        "in4" to listOf(
            DefectOption("Cabinet Doors/Drawers Not Functioning", "Cabinet doors or drawers were observed to not function properly. Hardware should be adjusted or replaced."),
            DefectOption("Countertop Damage", "Damage was observed to the countertop including chips, cracks, or separation at joints."),
            DefectOption("Good Condition", "Counters and cabinets appeared to be in good condition and functioning properly.")
        ),
        "in5" to listOf(
            DefectOption("Toilet Running", "The toilet was observed to be continuously running. The flush mechanism should be repaired to prevent water waste."),
            DefectOption("Toilet Loose at Floor", "The toilet was observed to be loose at the floor. A loose toilet can damage the wax ring seal and should be re-secured."),
            DefectOption("Slow Drain at Tub/Shower", "Slow draining was observed at the tub or shower. The drain should be cleared."),
            DefectOption("Caulk Failing", "Caulk was observed to be failing at the tub or shower surround. Failed caulk allows water intrusion and should be replaced."),
            DefectOption("Leak Under Sink", "An active leak or evidence of past leaking was observed under the sink. The source should be identified and repaired."),
            DefectOption("Good Condition", "Plumbing fixtures and faucets appeared to be in good condition and functioning properly.")
        ),
        "in6" to listOf(
            DefectOption("Exhaust Fan Not Working", "The bathroom exhaust fan was observed to not function properly. Exhaust fans are important for moisture control and should be repaired or replaced."),
            DefectOption("Fan Venting into Attic", "One or more exhaust fans were observed to be venting into the attic. This introduces moisture into the attic space and must be corrected by routing the duct to an exterior termination."),
            DefectOption("Kitchen Exhaust Not Vented", "The kitchen exhaust fan was observed to recirculate air rather than vent to the exterior. Exterior venting is recommended for better air quality."),
            DefectOption("Good Condition", "Ventilation and exhaust systems appeared to be functioning and venting to the exterior.")
        ),
        "in7" to listOf(
            DefectOption("Damper Not Operating", "The fireplace damper was observed to not operate properly. A functional damper is required to control airflow and prevent heat loss when the fireplace is not in use."),
            DefectOption("Firebox Cracks Observed", "Cracks were observed in the firebox or refractory panels. Cracks can allow heat and combustion gases to escape into the surrounding structure. Evaluation by a certified chimney sweep is recommended."),
            DefectOption("No Hearth Extension", "The hearth extension appeared to be absent or inadequate. A hearth extension is required to protect combustible flooring from sparks and embers."),
            DefectOption("Creosote Buildup", "Significant creosote buildup was observed in the fireplace flue. Creosote is highly combustible and a chimney fire hazard. Cleaning by a certified chimney sweep is recommended before use."),
            DefectOption("Glass Doors Cracked/Broken", "The fireplace glass doors were observed to be cracked or broken. Damaged glass doors should be replaced."),
            DefectOption("Gas Fireplace Not Igniting", "The gas fireplace was observed to not ignite properly. Evaluation by a qualified gas fireplace technician is recommended."),
            DefectOption("No Annual Inspection", "No evidence of recent annual chimney inspection was observed. Annual inspection and cleaning by a certified chimney sweep is recommended."),
            DefectOption("Good Condition", "The fireplace and solid fuel burning systems were observed to be in good condition with no significant deficiencies noted.")
        ),

        // ─── INSULATION ────────────────────────────────────────────
        "is1" to listOf(
            DefectOption("Insufficient Insulation", "Attic insulation was observed to be below the recommended R-value for this climate zone. Adding insulation is recommended to improve energy efficiency."),
            DefectOption("Insulation Blocking Vents", "Insulation was observed to be blocking soffit vents. Baffles should be installed to maintain airflow from the soffit vents."),
            DefectOption("Insulation Settled/Compressed", "Attic insulation appeared to be significantly settled or compressed, reducing its effective R-value. Additional insulation is recommended."),
            DefectOption("Good Condition", "Attic insulation appeared to be adequate and in good condition.")
        ),
        "is2" to listOf(
            DefectOption("Wall Insulation Absent - Visible", "Wall insulation was observed to be absent where visible. Adding insulation to exterior walls improves energy efficiency."),
            DefectOption("Good Condition", "Wall insulation appeared to be present where visible.")
        ),
        "is3" to listOf(
            DefectOption("Insulation Absent in Floor", "Insulation was absent from the floor joist bays over the crawlspace or unconditioned space. Insulation should be installed to improve energy efficiency."),
            DefectOption("Insulation Falling Down", "Floor insulation was observed to be falling down from the floor joist bays. Insulation should be re-secured with appropriate supports."),
            DefectOption("Good Condition", "Floor insulation appeared to be in good condition.")
        ),
        "is4" to listOf(
            DefectOption("Vapor Barrier Absent", "A vapor barrier was absent from the crawlspace floor. A minimum 6-mil polyethylene vapor barrier should be installed to prevent ground moisture from entering the structure."),
            DefectOption("Vapor Barrier Damaged", "The crawlspace vapor barrier was observed to be torn or damaged. The vapor barrier should be repaired or replaced."),
            DefectOption("Good Condition", "The vapor retarder appeared to be in good condition.")
        ),
        "is5" to listOf(
            DefectOption("Inadequate Ventilation", "Attic ventilation appeared to be inadequate. Inadequate ventilation can lead to moisture buildup, ice damming, and premature roof deterioration."),
            DefectOption("Exhaust Fan into Attic", "One or more bathroom exhaust fans were observed to be venting into the attic. This introduces moisture into the attic and must be corrected by routing ducts to the exterior."),
            DefectOption("Vents Blocked", "One or more attic vents were observed to be blocked. Vents should be cleared to allow proper air circulation."),
            DefectOption("Good Condition", "Attic ventilation appeared to be adequate.")
        ),

        // ─── GARAGE ────────────────────────────────────────────────
        "gr1" to listOf(
            DefectOption("Auto-Reverse Not Functioning", "The automatic reversal safety feature of the garage door did not function properly during testing. This is a safety hazard and should be repaired immediately by a qualified garage door technician."),
            DefectOption("Photo Eye Misaligned", "The photo eye sensors appeared to be misaligned. The auto-reverse safety feature may not operate correctly and the sensors should be realigned."),
            DefectOption("Door Off Track", "The garage door appeared to be off track at one or more locations. The door should be serviced by a qualified garage door technician."),
            DefectOption("Opener Not Functioning", "The garage door opener was observed to not function properly. Further evaluation by a qualified technician is recommended."),
            DefectOption("Good Condition", "The garage door and opener were observed to be functioning properly with all safety features operational.")
        ),
        "gr2" to listOf(
            DefectOption("Drywall Not Fire-Rated", "The drywall in the garage appeared to not be the required 5/8-inch Type X fire-rated drywall. Fire-rated drywall is required on walls and ceilings adjacent to living space."),
            DefectOption("Drywall Damaged/Penetrations", "The drywall in the garage was observed to be damaged or have penetrations. Openings in the fire separation must be properly patched to maintain the fire rating."),
            DefectOption("Good Condition", "Garage walls and ceiling appeared to be in good condition with proper fire separation.")
        ),
        "gr3" to listOf(
            DefectOption("Garage Floor Cracked", "The garage floor was observed to be significantly cracked. While typically a cosmetic concern, cracks should be sealed to prevent moisture intrusion."),
            DefectOption("Garage Floor Heaved", "The garage floor was observed to be heaved or uneven, presenting a trip hazard."),
            DefectOption("Good Condition", "The garage floor appeared to be in good condition.")
        ),
        "gr4" to listOf(
            DefectOption("Not Fire-Rated Door", "The door between the garage and living space does not appear to be a fire-rated door. A minimum 20-minute fire-rated door is required at this location for life safety."),
            DefectOption("Door Not Self-Closing", "The door between the garage and living space was not self-closing. A self-closing mechanism is required at this location to limit fire spread."),
            DefectOption("Door Has Pet Door", "A pet door was observed in the fire-rated door between the garage and living space. Pet doors compromise the fire rating of the door and should be removed."),
            DefectOption("Good Condition", "The garage vehicle door appeared to be proper, fire-rated, and functioning.")
        ),
        "gr5" to listOf(
            DefectOption("GFCI Absent - Garage", "GFCI protection was absent in the garage. GFCI protection is required for all 125-volt, single-phase, 15 and 20-amp receptacles in garages."),
            DefectOption("Outlets Not Functioning", "One or more garage outlets were observed to not function during testing. Further evaluation by a licensed electrician is recommended."),
            DefectOption("Exposed Wiring in Garage", "Exposed wiring was observed in the garage. Wiring should be protected by conduit."),
            DefectOption("No Lighting", "Adequate lighting was not observed in the garage. Proper lighting should be installed for safety."),
            DefectOption("Good Condition", "Garage electrical appeared to be in good condition with GFCI protection present and functional.")
        ),
        "gr6" to listOf(
            DefectOption("Auto-Reverse Failed - Contact Test", "The garage door opener did not reverse within 2 seconds of contacting a 1.5-inch rigid object placed on the floor. This is a safety hazard and should be corrected immediately by a qualified garage door technician."),
            DefectOption("Auto-Reverse Failed - Photo Eye Test", "The garage door opener did not reverse when the photo eye beam was interrupted during testing. The photo eye sensors should be adjusted or replaced."),
            DefectOption("Photo Eyes Absent", "Photo eye sensors were not present on the garage door opener. Photo eye sensors are required on all garage door openers manufactured after 1993 and should be installed."),
            DefectOption("Auto-Reverse Sensitivity Needs Adjustment", "The auto-reverse feature functioned but required multiple attempts or excessive force before reversing. The sensitivity should be adjusted per manufacturer instructions."),
            DefectOption("Auto-Reverse Passed All Tests", "The garage door auto-reverse feature was tested and functioned properly — reversing upon contact with a solid object and when the photo eye beam was interrupted.")
        )
    )

    fun getDefectsForItem(itemId: String): List<DefectOption> = defects[itemId] ?: emptyList()
}
