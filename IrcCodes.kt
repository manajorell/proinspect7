fun getCodeForItem(section: String, itemId: String, version: String): String {
    // Get general section codes first
    val sectionCodes = getCodesForSection(section, version)
    
    // Return item-specific codes based on section and itemId
    return when (section) {
        "Structure" -> when (itemId) {
            "foundation" -> """
                **Foundation Requirements - $version**
                
                Section R403 - Footings
                • Footings shall be supported on undisturbed natural soils or engineered fill
                • Minimum width: 12 inches for 1-story, 15 inches for 2-story, 18 inches for 3-story
                • Minimum depth: 12 inches below undisturbed ground surface
                • Footings shall extend below frost line in freezing climates
                • Concrete minimum compressive strength: 2,500 psi at 28 days
                
                Section R404 - Foundation Walls
                • Foundation walls shall extend above grade and provide support for exterior walls
                • Minimum thickness: 6 inches for concrete, 8 inches for masonry
                • Waterproofing required from top of footing to finished grade
            """.trimIndent()
            
            "basement" -> """
                **Basement Requirements - $version**
                
                Section R408 - Under-Floor Space
                • Minimum height: 18 inches to underside of joists, 12 inches to underside of girders
                • Access opening minimum: 18" x 24"
                • Ventilation required: 1 sq ft per 150 sq ft of crawl space area
                
                Section R404.1.6 - Basement Walls
                • Concrete basement walls shall be designed to resist lateral soil loads
                • Waterproofing required on exterior surface from footing to grade
            """.trimIndent()
            
            "crawlspace" -> """
                **Crawl Space Requirements - $version**
                
                Section R408 - Under-Floor Space
                • Minimum clearance: 18 inches from bottom of joists to ground
                • Minimum clearance: 12 inches from bottom of girders to ground
                • Access opening: Minimum 18" x 24"
                • Ventilation: 1 square foot per 150 square feet of crawl space
                • Vapor retarder: 6-mil polyethylene required on ground surface
            """.trimIndent()
            
            else -> sectionCodes
        }
        
        "Roof" -> when (itemId) {
            "covering" -> """
                **Roof Covering Requirements - $version**
                
                Section R905 - Requirements for Roof Coverings
                
                Asphalt Shingles (R905.2):
                • Minimum slope: 2:12 (with underlayment), 4:12 (normal application)
                • Underlayment: One layer of No. 15 felt or approved synthetic
                • Fasteners: Minimum 4 per strip for 3-tab, 6 for architectural
            """.trimIndent()
            
            "flashing" -> """
                **Roof Flashing Requirements - $version**
                
                Section R903 - Weather Protection
                • Valley flashing: Minimum 24-inch-wide valley lining
                • Chimney flashing: Base flashing and counterflashing required
                • Wall flashing: Step flashing at roof-to-wall intersections
            """.trimIndent()
            
            else -> sectionCodes
        }
        
        "Electrical" -> when (itemId) {
            "service_panel" -> """
                **Electrical Service Panel - $version**
                
                Section E3601 - General Services
                • Minimum 100-amp service for dwellings
                • Readily accessible location
                • Minimum 30" wide x 36" deep working clearance
                • All circuits labeled to identify purpose
            """.trimIndent()
            
            "wiring" -> """
                **Electrical Wiring - $version**
                
                Section E3705 - Conductor Identification
                • 15-amp circuits: 14 AWG copper minimum
                • 20-amp circuits: 12 AWG copper minimum
                • AFCI protection required for most branch circuits
            """.trimIndent()
            
            else -> sectionCodes
        }
        
        else -> sectionCodes
    }
}
