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
