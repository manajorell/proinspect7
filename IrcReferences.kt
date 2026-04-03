package com.proinspect.app.data

// IRC Building Code References by State
object IrcReferences {
    
    data class IrcSection(
        val code: String,
        val title: String,
        val url: String
    )
    
    // Common IRC sections that inspectors reference
    val commonSections = listOf(
        IrcSection(
            code = "R202",
            title = "Definitions",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-2-definitions"
        ),
        IrcSection(
            code = "R301",
            title = "Design Criteria",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR301"
        ),
        IrcSection(
            code = "R302",
            title = "Fire-Resistant Construction",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR302"
        ),
        IrcSection(
            code = "R303",
            title = "Light, Ventilation and Heating",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR303"
        ),
        IrcSection(
            code = "R305",
            title = "Ceiling Height",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR305"
        ),
        IrcSection(
            code = "R308",
            title = "Glazing",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR308"
        ),
        IrcSection(
            code = "R310",
            title = "Emergency Escape and Rescue Openings",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR310"
        ),
        IrcSection(
            code = "R311",
            title = "Means of Egress",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR311"
        ),
        IrcSection(
            code = "R312",
            title = "Guards and Window Fall Protection",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR312"
        ),
        IrcSection(
            code = "R313",
            title = "Automatic Fire Sprinkler Systems",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR313"
        ),
        IrcSection(
            code = "R314",
            title = "Smoke Alarms",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR314"
        ),
        IrcSection(
            code = "R315",
            title = "Carbon Monoxide Alarms",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-3-building-planning#IRC2021P1_Pt03_Ch03_SecR315"
        ),
        IrcSection(
            code = "R401",
            title = "Foundation - General",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-4-foundations#IRC2021P1_Pt03_Ch04_SecR401"
        ),
        IrcSection(
            code = "R502",
            title = "Floor Framing",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-5-floors#IRC2021P1_Pt03_Ch05_SecR502"
        ),
        IrcSection(
            code = "R602",
            title = "Wall Framing",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-6-wall-construction#IRC2021P1_Pt03_Ch06_SecR602"
        ),
        IrcSection(
            code = "R703",
            title = "Exterior Covering",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-7-wall-covering#IRC2021P1_Pt03_Ch07_SecR703"
        ),
        IrcSection(
            code = "R802",
            title = "Roof Framing",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-8-roof-ceiling-construction#IRC2021P1_Pt03_Ch08_SecR802"
        ),
        IrcSection(
            code = "R905",
            title = "Roof Covering Requirements",
            url = "https://codes.iccsafe.org/content/IRC2021P1/chapter-9-roof-assemblies#IRC2021P1_Pt03_Ch09_SecR905"
        ),
        IrcSection(
            code = "E3401",
            title = "Electrical - General Services",
            url = "https://codes.iccsafe.org/content/IRC2021P5/chapter-34-general-requirements"
        ),
        IrcSection(
            code = "E3405",
            title = "Branch Circuits and Feeders",
            url = "https://codes.iccsafe.org/content/IRC2021P5/chapter-34-general-requirements#IRC2021P5_Pt07_Ch34_SecE3405"
        ),
        IrcSection(
            code = "E3406",
            title = "Conductors",
            url = "https://codes.iccsafe.org/content/IRC2021P5/chapter-34-general-requirements#IRC2021P5_Pt07_Ch34_SecE3406"
        ),
        IrcSection(
            code = "M1301",
            title = "General Mechanical Requirements",
            url = "https://codes.iccsafe.org/content/IRC2021P4/chapter-13-general-mechanical-system-requirements"
        ),
        IrcSection(
            code = "M1401",
            title = "Heating and Cooling Equipment",
            url = "https://codes.iccsafe.org/content/IRC2021P4/chapter-14-heating-and-cooling-equipment"
        ),
        IrcSection(
            code = "M1501",
            title = "Exhaust Systems",
            url = "https://codes.iccsafe.org/content/IRC2021P4/chapter-15-exhaust-systems"
        ),
        IrcSection(
            code = "P2602",
            title = "Individual Water Supply",
            url = "https://codes.iccsafe.org/content/IRC2021P6/chapter-26-general-plumbing-requirements#IRC2021P6_Pt08_Ch26_SecP2602"
        ),
        IrcSection(
            code = "P2603",
            title = "Structural and Piping Protection",
            url = "https://codes.iccsafe.org/content/IRC2021P6/chapter-26-general-plumbing-requirements#IRC2021P6_Pt08_Ch26_SecP2603"
        ),
        IrcSection(
            code = "P2801",
            title = "Fixtures - General",
            url = "https://codes.iccsafe.org/content/IRC2021P6/chapter-28-water-heaters#IRC2021P6_Pt08_Ch28_SecP2801"
        ),
        IrcSection(
            code = "P3001",
            title = "Vents",
            url = "https://codes.iccsafe.org/content/IRC2021P6/chapter-30-sanitary-drainage#IRC2021P6_Pt08_Ch30_SecP3001"
        ),
        IrcSection(
            code = "G2404",
            title = "Gas Piping Installations",
            url = "https://codes.iccsafe.org/content/IRC2021P7/chapter-24-fuel-gas#IRC2021P7_Pt09_Ch24_SecG2404"
        )
    )
    
    // State-specific IRC adoption information
    val stateInfo = mapOf(
        "Alabama" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Alaska" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Arizona" to StateIrcInfo("2021 IRC", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Arkansas" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "California" to StateIrcInfo("2021 IRC (Modified)", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Colorado" to StateIrcInfo("2021 IRC", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Connecticut" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Delaware" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Florida" to StateIrcInfo("2020 Florida Building Code", "https://codes.iccsafe.org/content/FLBC2020P1"),
        "Georgia" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Hawaii" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Idaho" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Illinois" to StateIrcInfo("2021 IRC", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Indiana" to StateIrcInfo("2020 Indiana Residential Code", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Iowa" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Kansas" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Kentucky" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Louisiana" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Maine" to StateIrcInfo("2015 IRC", "https://codes.iccsafe.org/content/IRC2015P1"),
        "Maryland" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Massachusetts" to StateIrcInfo("2018 IRC (Modified)", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Michigan" to StateIrcInfo("2015 IRC", "https://codes.iccsafe.org/content/IRC2015P1"),
        "Minnesota" to StateIrcInfo("2020 Minnesota Residential Code", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Mississippi" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Missouri" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Montana" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Nebraska" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Nevada" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "New Hampshire" to StateIrcInfo("2015 IRC", "https://codes.iccsafe.org/content/IRC2015P1"),
        "New Jersey" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "New Mexico" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "New York" to StateIrcInfo("2020 New York Residential Code", "https://codes.iccsafe.org/content/IRC2018P1"),
        "North Carolina" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "North Dakota" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Ohio" to StateIrcInfo("2017 Ohio Residential Code", "https://codes.iccsafe.org/content/IRC2015P1"),
        "Oklahoma" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Oregon" to StateIrcInfo("2021 Oregon Residential Specialty Code", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Pennsylvania" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Rhode Island" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "South Carolina" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "South Dakota" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Tennessee" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Texas" to StateIrcInfo("2021 IRC", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Utah" to StateIrcInfo("2021 IRC", "https://codes.iccsafe.org/content/IRC2021P1"),
        "Vermont" to StateIrcInfo("2020 Vermont Residential Building Code", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Virginia" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Washington" to StateIrcInfo("2021 Washington State Residential Code", "https://codes.iccsafe.org/content/IRC2021P1"),
        "West Virginia" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Wisconsin" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1"),
        "Wyoming" to StateIrcInfo("2018 IRC", "https://codes.iccsafe.org/content/IRC2018P1")
    )
    
    data class StateIrcInfo(
        val codeName: String,
        val baseUrl: String
    )
    
    fun getStates(): List<String> = stateInfo.keys.sorted()
    
    fun getStateCodeInfo(state: String): StateIrcInfo? = stateInfo[state]
}
