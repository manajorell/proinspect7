// ══════════════════════════════════════════════════════════════════════════════
// PROPERTY INFO SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun PropertyInfoScreen(viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()
    val settings by viewModel.settings.collectAsState()
    
    report?.let { currentReport ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Property Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = currentReport.propertyAddress,
                            onValueChange = { viewModel.updatePropertyAddress(it) },
                            label = { Text("Property Address") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = currentReport.clientName,
                            onValueChange = { viewModel.updateClientName(it) },
                            label = { Text("Client Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = currentReport.inspectorName,
                            onValueChange = { viewModel.updateInspectorName(it) },
                            label = { Text("Inspector Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text(settings.inspectorName) }
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = currentReport.inspectionDate,
                            onValueChange = { viewModel.updateInspectionDate(it) },
                            label = { Text("Inspection Date") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("MM/DD/YYYY") }
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Property Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Navy
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = currentReport.yearBuilt,
                            onValueChange = { viewModel.updateYearBuilt(it) },
                            label = { Text("Year Built") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = currentReport.squareFootage,
                            onValueChange = { viewModel.updateSquareFootage(it) },
                            label = { Text("Square Footage") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = currentReport.propertyType,
                            onValueChange = { viewModel.updatePropertyType(it) },
                            label = { Text("Property Type") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("Single Family, Condo, etc.") }
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Gold.copy(alpha = 0.1f)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Gold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "IRC Code Version",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Navy
                                )
                                Text(
                                    currentReport.ircVersion,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// INSPECTION SECTION SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun InspectionSectionScreen(section: String, viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()
    val items by viewModel.currentItems.collectAsState()
    val photos by viewModel.currentPhotos.collectAsState()
    val currentItemId by viewModel.currentItemId.collectAsState()
    
    report?.let { currentReport ->
        val checklist = InspectionSections.getChecklistForSection(section)
        
        if (currentItemId != null) {
            // Camera screen
            CameraScreen(
                onPhotoCaptured = { uri ->
                    viewModel.addPhoto(currentItemId!!, uri)
                    viewModel.clearCurrentItem()
                },
                onCancel = { viewModel.clearCurrentItem() }
            )
        } else {
            // Section inspection screen
            GenericSectionScreen(
                section = section,
                checklist = checklist,
                items = items,
                photos = photos,
                ircVersion = currentReport.ircVersion,
                onRatingChange = { itemId, rating ->
                    viewModel.updateRating(itemId, rating)
                },
                onNarrativeChange = { itemId, narrative ->
                    viewModel.updateNarrative(itemId, narrative)
                },
                onCameraClick = { itemId ->
                    viewModel.setCurrentItem(itemId)
                },
                onGalleryPick = { itemId, uri ->
                    viewModel.addPhoto(itemId, uri)
                },
                onDeletePhoto = { photo ->
                    viewModel.deletePhoto(photo)
                }
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SUMMARY SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun SummaryScreen(viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()
    val items by viewModel.currentItems.collectAsState()
    val photos by viewModel.currentPhotos.collectAsState()
    val context = LocalContext.current
    var isGeneratingPdf by remember { mutableStateOf(false) }
    var showEmailDialog by remember { mutableStateOf(false) }
    
    report?.let { currentReport ->
        val itemsByRating = items.values.groupBy { it.rating }
        val safetyCount = itemsByRating[Rating.SAFETY]?.size ?: 0
        val majorCount = itemsByRating[Rating.MAJOR]?.size ?: 0
        val monitorCount = itemsByRating[Rating.MONITOR]?.size ?: 0
        val goodCount = itemsByRating[Rating.GOOD]?.size ?: 0
        val totalInspected = items.values.count { it.rating != Rating.NOT_RATED }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Inspection Summary",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SummaryStatItem("Total Items", totalInspected.toString(), Navy)
                            SummaryStatItem("Photos", photos.size.toString(), Navy)
                        }
                    }
                }
            }
            
            // Ratings Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Findings Breakdown",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Navy
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        if (safetyCount > 0) {
                            RatingSummaryRow("Safety Issues", safetyCount, RatingRed)
                            Spacer(Modifier.height(8.dp))
                        }
                        if (majorCount > 0) {
                            RatingSummaryRow("Major Issues", majorCount, RatingOrange)
                            Spacer(Modifier.height(8.dp))
                        }
                        if (monitorCount > 0) {
                            RatingSummaryRow("Monitor Items", monitorCount, RatingYellow)
                            Spacer(Modifier.height(8.dp))
                        }
                        if (goodCount > 0) {
                            RatingSummaryRow("Good Condition", goodCount, RatingGreen)
                        }
                        
                        if (totalInspected == 0) {
                            Text(
                                "No items inspected yet",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Actions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Actions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Navy
                        )
                        
                        Button(
                            onClick = {
                                isGeneratingPdf = true
                                viewModel.generatePdf(context) { success ->
                                    isGeneratingPdf = false
                                    if (success) {
                                        android.widget.Toast.makeText(
                                            context,
                                            "PDF generated successfully",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy),
                            enabled = !isGeneratingPdf
                        ) {
                            if (isGeneratingPdf) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Generating PDF...")
                            } else {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Generate PDF Report")
                            }
                        }
                        
                        OutlinedButton(
                            onClick = { showEmailDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                            border = BorderStroke(1.dp, Navy)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Email Report")
                        }
                    }
                }
            }
        }
    }
    
    if (showEmailDialog) {
        var emailAddress by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showEmailDialog = false },
            title = { Text("Email Report") },
            text = {
                Column {
                    Text("Enter recipient email address:")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = emailAddress,
                        onValueChange = { emailAddress = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.emailReport(context, emailAddress)
                        showEmailDialog = false
                    },
                    enabled = emailAddress.isNotBlank()
                ) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmailDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SummaryStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun RatingSummaryRow(label: String, count: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, shape = androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Text(label, fontSize = 15.sp, color = Color(0xFF374151))
        }
        Text(
            count.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
