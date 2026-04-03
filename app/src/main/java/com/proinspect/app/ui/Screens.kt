package com.proinspect.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proinspect.app.data.InspectionSections
import com.proinspect.app.data.InspectionItem
import com.proinspect.app.data.InspectionPhoto
import com.proinspect.app.data.Rating
import com.proinspect.app.data.Report

// ══════════════════════════════════════════════════════════════════════════════
// REPORTS LIST SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsListScreen(
    reports: List<Report>,
    onNewReport: () -> Unit,
    onOpenReport: (Long) -> Unit,
    onDeleteReport: (Report) -> Unit,
    onSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ProInspect", fontWeight = FontWeight.Bold, color = Gold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy),
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNewReport, containerColor = Gold) {
                Icon(Icons.Default.Add, contentDescription = "New Report", tint = Color.White)
            }
        }
    ) { padding ->
        if (reports.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Assignment, contentDescription = null,
                        modifier = Modifier.size(64.dp), tint = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                    Text("No reports yet", fontSize = 18.sp, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))
                    Text("Tap + to create a new inspection", fontSize = 14.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports) { report ->
                    var showDelete by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onOpenReport(report.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null,
                                tint = Navy, modifier = Modifier.size(40.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    report.propertyAddress.ifBlank { "New Inspection" },
                                    fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy
                                )
                                if (report.clientName.isNotBlank())
                                    Text(report.clientName, fontSize = 13.sp, color = Color.Gray)
                                Text(report.inspectionDate, fontSize = 12.sp, color = Color.Gray)
                            }
                            IconButton(onClick = { showDelete = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                            }
                        }
                    }
                    if (showDelete) {
                        AlertDialog(
                            onDismissRequest = { showDelete = false },
                            title = { Text("Delete Report?") },
                            text = { Text("This will permanently delete this inspection report.") },
                            confirmButton = {
                                TextButton(onClick = { onDeleteReport(report); showDelete = false }) {
                                    Text("Delete", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDelete = false }) { Text("Cancel") }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// REPORT SCREEN (tab shell)
// ══════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    report: Report?,
    currentTab: Int,
    onTabChange: (Int) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val tabLabels = listOf("Info") +
        InspectionSections.sections.map { InspectionSections.sectionNames[it] ?: it } +
        listOf("Summary")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        report?.propertyAddress?.ifBlank { "New Inspection" } ?: "Inspection",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy)
            )
        },
        bottomBar = {
            ScrollableTabRow(
                selectedTabIndex = currentTab,
                containerColor = Navy,
                contentColor = Gold,
                edgePadding = 0.dp
            ) {
                tabLabels.forEachIndexed { index, label ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { onTabChange(index) },
                        text = {
                            Text(
                                label, fontSize = 11.sp,
                                color = if (currentTab == index) Gold else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            content()
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// PROPERTY INFO SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun PropertyInfoScreen(viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()

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
                        Text("Property Information", fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = currentReport.propertyAddress,
                            onValueChange = { viewModel.updatePropertyAddress(it) },
                            label = { Text("Property Address") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = currentReport.clientName,
                            onValueChange = { viewModel.updateClientName(it) },
                            label = { Text("Client Name") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = currentReport.inspectorName,
                            onValueChange = { viewModel.updateInspectorName(it) },
                            label = { Text("Inspector Name") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = currentReport.inspectionDate,
                            onValueChange = { viewModel.updateInspectionDate(it) },
                            label = { Text("Inspection Date") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true,
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
                        Text("Property Details", fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = currentReport.yearBuilt,
                            onValueChange = { viewModel.updateYearBuilt(it) },
                            label = { Text("Year Built") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = currentReport.squareFootage,
                            onValueChange = { viewModel.updateSquareFootage(it) },
                            label = { Text("Square Footage") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = currentReport.propertyType,
                            onValueChange = { viewModel.updatePropertyType(it) },
                            label = { Text("Property Type") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true,
                            placeholder = { Text("Single Family, Condo, etc.") }
                        )
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
    val context = LocalContext.current

    report?.let {
        val checklist = InspectionSections.items[section] ?: emptyList()

        if (currentItemId != null) {
            CameraScreen(
                onPhotoCaptured = { uri ->
                    viewModel.addPhoto(currentItemId!!, uri)
                    viewModel.clearCurrentItem()
                },
                onCancel = { viewModel.clearCurrentItem() }
            )
        } else {
            GenericSectionScreen(
                section = section,
                checklist = checklist,
                items = items,
                photos = photos,
                onRatingChange = { itemId, rating -> viewModel.updateRating(itemId, rating) },
                onNarrativeChange = { itemId, narrative -> viewModel.updateNarrative(itemId, narrative) },
                onCameraClick = { itemId -> viewModel.setCurrentItem(itemId) },
                onGalleryPick = { itemId, uri -> viewModel.addPhoto(itemId, uri) },
                onDeletePhoto = { photo -> viewModel.deletePhoto(photo) }
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// GENERIC SECTION SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun GenericSectionScreen(
    section: String,
    checklist: List<com.proinspect.app.data.ChecklistItem>,
    items: Map<String, InspectionItem>,
    photos: List<InspectionPhoto>,
    onRatingChange: (String, Rating) -> Unit,
    onNarrativeChange: (String, String) -> Unit,
    onCameraClick: (String) -> Unit,
    onGalleryPick: (String, Uri) -> Unit,
    onDeletePhoto: (InspectionPhoto) -> Unit
) {
    val sectionName = InspectionSections.sectionNames[section] ?: section
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(sectionName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
        }
        items(checklist) { checklistItem ->
            val item = items[checklistItem.id]
            val rating = item?.rating ?: Rating.NOT_RATED
            val narrative = item?.narrative ?: ""
            val itemPhotos = photos.filter { it.itemId == checklistItem.id }
            ChecklistItemCard(
                item = checklistItem,
                rating = rating,
                narrative = narrative,
                photos = itemPhotos,
                onRatingChanged = { onRatingChange(checklistItem.id, it) },
                onNarrativeChanged = { onNarrativeChange(checklistItem.id, it) },
                onCameraClick = { onCameraClick(checklistItem.id) },
                onGalleryPick = { onGalleryPick(checklistItem.id, it) },
                onDeletePhoto = onDeletePhoto
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// CAMERA SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun CameraScreen(
    onPhotoCaptured: (Uri) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val photoUri = remember {
        val file = java.io.File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
        androidx.core.content.FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) onPhotoCaptured(photoUri) else onCancel()
    }
    LaunchedEffect(Unit) { launcher.launch(photoUri) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Gold)
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onCancel) { Text("Cancel") }
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

    report?.let {
        val itemsByRating = items.values.groupBy { it.rating }
        val safetyCount  = itemsByRating[Rating.SAFETY]?.size ?: 0
        val majorCount   = itemsByRating[Rating.MAJOR]?.size ?: 0
        val monitorCount = itemsByRating[Rating.MONITOR]?.size ?: 0
        val goodCount    = itemsByRating[Rating.GOOD]?.size ?: 0
        val totalInspected = items.values.count { it.rating != Rating.NOT_RATED }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Inspection Summary", fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            SummaryStatItem("Total Items", totalInspected.toString(), Navy)
                            SummaryStatItem("Photos", photos.size.toString(), Navy)
                        }
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Findings Breakdown", fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        if (safetyCount > 0)  { RatingSummaryRow("Safety Issues",  safetyCount,  RatingRed);    Spacer(Modifier.height(8.dp)) }
                        if (majorCount > 0)   { RatingSummaryRow("Major Issues",   majorCount,   RatingOrange);  Spacer(Modifier.height(8.dp)) }
                        if (monitorCount > 0) { RatingSummaryRow("Monitor Items",  monitorCount, RatingYellow);  Spacer(Modifier.height(8.dp)) }
                        if (goodCount > 0)      RatingSummaryRow("Good Condition", goodCount,    RatingGreen)
                        if (totalInspected == 0)
                            Text("No items inspected yet", fontSize = 14.sp, color = Color.Gray,
                                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Actions", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Navy)
                        Button(
                            onClick = {
                                isGeneratingPdf = true
                                viewModel.generatePdf(context) { success ->
                                    isGeneratingPdf = false
                                    if (success) android.widget.Toast.makeText(
                                        context, "PDF generated successfully",
                                        android.widget.Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy),
                            enabled = !isGeneratingPdf
                        ) {
                            if (isGeneratingPdf) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp),
                                    color = Color.White, strokeWidth = 2.dp)
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
                            Icon(Send, contentDescription = null)
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
                    OutlinedTextField(value = emailAddress, onValueChange = { emailAddress = it },
                        label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.emailReport(context, emailAddress); showEmailDialog = false },
                    enabled = emailAddress.isNotBlank()) { Text("Send") }
            },
            dismissButton = {
                TextButton(onClick = { showEmailDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SETTINGS SCREEN
// ══════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: InspectionViewModel, onBack: () -> Unit) {
    val settings by viewModel.settings.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Inspector Settings", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = settings.ircState,
                            onValueChange = { viewModel.updateSettings(settings.copy(ircState = it)) },
                            label = { Text("Default IRC Version") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = settings.anthropicApiKey,
                            onValueChange = { viewModel.updateSettings(settings.copy(anthropicApiKey = it)) },
                            label = { Text("Anthropic API Key") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// HELPER COMPOSABLES
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun SummaryStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun RatingSummaryRow(label: String, count: Int, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(color, shape = CircleShape))
            Spacer(Modifier.width(12.dp))
            Text(label, fontSize = 15.sp, color = Color(0xFF374151))
        }
        Text(count.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
