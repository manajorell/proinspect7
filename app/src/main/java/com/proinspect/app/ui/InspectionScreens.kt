package com.proinspect.app.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.proinspect.app.data.*
import com.proinspect.app.pdf.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun InspectionSectionScreen(section: String, viewModel: InspectionViewModel) {
    val context = LocalContext.current
    val report by viewModel.currentReport.collectAsState()
    val items by viewModel.items.collectAsState()
    val photos by viewModel.photos.collectAsState()
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        viewModel.onPhotoCaptured(success)
    }
    fun launchCamera(sec: String, itemId: String?) {
        val uri = viewModel.prepareCameraUri(context, sec, itemId)
        cameraUri = uri
        cameraLauncher.launch(uri)
    }
    val sectionItems = InspectionSections.items[section] ?: emptyList()
    val sectionName = InspectionSections.sectionNames[section] ?: section

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(14.dp)) {
                    Text("$sectionName — Overview Photos", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
                    Spacer(Modifier.height(10.dp))
                    PhotoStrip(
                        photos = photos.filter { it.section == section && it.itemId == null },
                        onCameraClick = { launchCamera(section, null) },
                        onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, null) },
                        onDeletePhoto = { id -> viewModel.deletePhoto(id) }
                    )
                }
            }
        }
        item {
            Text("InterNACHI Checklist", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280), modifier = Modifier.padding(top = 4.dp, bottom = 2.dp))
        }
        items(sectionItems) { ci ->
            val itemState = items[ci.id]
            ChecklistItemCard(
                item = ci,
                rating = itemState?.rating ?: Rating.NOT_RATED,
                narrative = itemState?.narrative ?: "",
                photos = photos.filter { it.itemId == ci.id },
                onRatingChanged = { rating -> viewModel.setItemRating(ci.id, section, rating) },
                onNarrativeChanged = { text -> viewModel.setItemNarrative(ci.id, section, text) },
                onCameraClick = { launchCamera(section, ci.id) },
                onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, ci.id) },
                onDeletePhoto = { id -> viewModel.deletePhoto(id) }
            )
        }
        item {
            val narrative = when (section) {
                "roofing" -> report?.roofingNarrative ?: ""
                "exterior" -> report?.exteriorNarrative ?: ""
                "structure" -> report?.structureNarrative ?: ""
                "electrical" -> report?.electricalNarrative ?: ""
                "hvac" -> report?.hvacNarrative ?: ""
                "plumbing" -> report?.plumbingNarrative ?: ""
                "interior" -> report?.interiorNarrative ?: ""
                "insulation" -> report?.insulationNarrative ?: ""
                "garage" -> report?.garageNarrative ?: ""
                else -> ""
            }
            NarrativeBox(
                value = narrative,
                onValueChange = { value ->
                    report?.let { r ->
                        val updated = when (section) {
                            "roofing" -> r.copy(roofingNarrative = value)
                            "exterior" -> r.copy(exteriorNarrative = value)
                            "structure" -> r.copy(structureNarrative = value)
                            "electrical" -> r.copy(electricalNarrative = value)
                            "hvac" -> r.copy(hvacNarrative = value)
                            "plumbing" -> r.copy(plumbingNarrative = value)
                            "interior" -> r.copy(interiorNarrative = value)
                            "insulation" -> r.copy(insulationNarrative = value)
                            "garage" -> r.copy(garageNarrative = value)
                            else -> r
                        }
                        viewModel.saveReport(updated)
                    }
                },
                label = "📝 Overall $sectionName Narrative",
                placeholder = "Summarize overall $sectionName findings..."
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
    }
}

// Save-on-focus-lost text field
@Composable
fun SaveField(
    label: String,
    initialValue: String,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    var text by remember(initialValue) { mutableStateOf(initialValue) }
    var lastSaved by remember { mutableStateOf(initialValue) }

    Column(modifier = modifier) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF6B7280), modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && text != lastSaved) {
                        lastSaved = text
                        onSave(text)
                    }
                },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = if (singleLine) ImeAction.Next else ImeAction.Default
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedLabelColor = Gold
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun PropertyInfoScreen(viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()
    val r = report ?: return
    val photos by viewModel.photos.collectAsState()
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        viewModel.onPhotoCaptured(success)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Property Information", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                SaveField("Property Address", r.propertyAddress,
                    onSave = { v -> viewModel.saveReport(r.copy(propertyAddress = v)) })
                SaveField("City, State, ZIP", r.propertyCity,
                    onSave = { v -> viewModel.saveReport(r.copy(propertyCity = v)) })
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SaveField("Year Built", r.yearBuilt,
                        onSave = { v -> viewModel.saveReport(r.copy(yearBuilt = v)) },
                        modifier = Modifier.weight(1f))
                    SaveField("Sq Ft", r.squareFootage,
                        onSave = { v -> viewModel.saveReport(r.copy(squareFootage = v)) },
                        modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SaveField("Inspection Date", r.inspectionDate,
                        onSave = { v -> viewModel.saveReport(r.copy(inspectionDate = v)) },
                        modifier = Modifier.weight(1f))
                    SaveField("Weather", r.weatherConditions,
                        onSave = { v -> viewModel.saveReport(r.copy(weatherConditions = v)) },
                        modifier = Modifier.weight(1f))
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Client & Inspector", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                SaveField("Client Name", r.clientName,
                    onSave = { v -> viewModel.saveReport(r.copy(clientName = v)) })
                SaveField("Client Email", r.clientEmail,
                    onSave = { v -> viewModel.saveReport(r.copy(clientEmail = v)) })
                SaveField("Inspector Name", r.inspectorName,
                    onSave = { v -> viewModel.saveReport(r.copy(inspectorName = v)) })
                SaveField("InterNACHI Cert #", r.inspectorCert,
                    onSave = { v -> viewModel.saveReport(r.copy(inspectorCert = v)) })
                SaveField("Company", r.inspectorCompany,
                    onSave = { v -> viewModel.saveReport(r.copy(inspectorCompany = v)) })
                SaveField("Phone", r.inspectorPhone,
                    onSave = { v -> viewModel.saveReport(r.copy(inspectorPhone = v)) })
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Property Photos & Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                PhotoStrip(
                    photos = photos.filter { it.section == "info" && it.itemId == null },
                    onCameraClick = {
                        val uri = viewModel.prepareCameraUri(context, "info", null)
                        cameraLauncher.launch(uri)
                    },
                    onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, "info", null) },
                    onDeletePhoto = { id -> viewModel.deletePhoto(id) }
                )
                NarrativeBox(
                    value = r.overviewNarrative,
                    onValueChange = { v -> viewModel.saveReport(r.copy(overviewNarrative = v)) },
                    label = "📝 Property Overview Notes"
                )
                SaveField("Access Limitations", r.limitations,
                    onSave = { v -> viewModel.saveReport(r.copy(limitations = v)) },
                    singleLine = false)
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun SummaryScreen(viewModel: InspectionViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val report by viewModel.currentReport.collectAsState()
    val items by viewModel.items.collectAsState()
    val photos by viewModel.photos.collectAsState()
    var isGenerating by remember { mutableStateOf(false) }

    val counts = Rating.values().associateWith { r -> items.values.count { item -> item.rating == r } }
    val findings = items.values
        .filter { item -> item.rating == Rating.SAFETY || item.rating == Rating.MAJOR || item.rating == Rating.MONITOR }
        .sortedWith(compareBy { item -> when (item.rating) { Rating.SAFETY -> 0; Rating.MAJOR -> 1; else -> 2 } })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth().background(Navy, RoundedCornerShape(12.dp)).padding(20.dp)) {
                Column {
                    Text("Inspection Summary", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = GoldLight)
                    Text(report?.propertyAddress?.ifBlank { "Address not set" } ?: "",
                        color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("Client: ${report?.clientName?.ifBlank { "—" } ?: "—"}",
                        color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Triple("🚨", counts[Rating.SAFETY] ?: 0, RatingRed),
                    Triple("⚠", counts[Rating.MAJOR] ?: 0, RatingOrange),
                    Triple("👁", counts[Rating.MONITOR] ?: 0, RatingYellow),
                    Triple("✓", counts[Rating.GOOD] ?: 0, RatingGreen)
                ).forEach { (icon, count, color) ->
                    Card(modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(icon, fontSize = 18.sp)
                            Text(count.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
                        }
                    }
                }
            }
        }
        if (findings.isNotEmpty()) {
            item { Text("Priority Findings", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy) }
            items(findings) { finding ->
                val ci = InspectionSections.allItems().find { it.id == finding.itemId }
                val color = when (finding.rating) {
                    Rating.SAFETY -> RatingRed; Rating.MAJOR -> RatingOrange; else -> RatingYellow }
                Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(Modifier.padding(12.dp)) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(color))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(finding.section.replaceFirstChar { it.uppercase() },
                                fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                            Text(ci?.title ?: finding.itemId, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            if (finding.narrative.isNotBlank())
                                Text(finding.narrative, fontSize = 12.sp, color = Color(0xFF6B7280))
                        }
                        Surface(color = color, shape = RoundedCornerShape(4.dp)) {
                            Text(finding.rating.short,
                                Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = {
                    isGenerating = true
                    scope.launch {
                        try {
                            val r = report ?: return@launch
                            val file = withContext(Dispatchers.IO) {
                                PdfGenerator.generate(context, r, items.values.toList(), photos) }
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Open PDF Report"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "PDF error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally { isGenerating = false }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy),
                shape = RoundedCornerShape(10.dp),
                enabled = !isGenerating
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Navy, strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("Generating PDF...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                } else {
                    Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Export PDF Report for Client", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
        item {
            OutlinedButton(
                onClick = {
                    isGenerating = true
                    scope.launch {
                        try {
                            val r = report ?: return@launch
                            val file = withContext(Dispatchers.IO) {
                                PdfGenerator.generate(context, r, items.values.toList(), photos) }
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                putExtra(Intent.EXTRA_SUBJECT, "Home Inspection Report — ${r.propertyAddress}")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(r.clientEmail))
                                putExtra(Intent.EXTRA_TEXT, "Please find your home inspection report attached.\n\nInspector: ${r.inspectorName}\nDate: ${r.inspectionDate}")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Report"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally { isGenerating = false }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                border = BorderStroke(2.dp, Navy),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Email, null, tint = Navy)
                Spacer(Modifier.width(10.dp))
                Text("Email Report to Client", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Navy)
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
    }
}
