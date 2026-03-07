package com.proinspect.app.ui

import android.content.Intent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import java.io.File
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
                        photos = photos.filter { photo -> photo.section == section && photo.itemId == null },
                        onCameraClick = { launchCamera(section, null) },
                        onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, null) },
                        onDeletePhoto = { photo -> viewModel.deletePhoto(photo) } 
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
                photos = photos.filter { photo -> photo.itemId == ci.id },
                onRatingChanged = { rating -> viewModel.setItemRating(ci.id, section, rating) },
                onNarrativeChanged = { text -> viewModel.setItemNarrative(ci.id, section, text) },
                onCameraClick = { launchCamera(section, ci.id) },
                onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, ci.id) },
                onDeletePhoto = { photo -> viewModel.deletePhoto(photo) }
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
       item {
            val agreementLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let {
                    val r = report ?: return@let
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_SUBJECT, "Pre-Inspection Agreement — ${r.propertyAddress}")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(r.clientEmail))
                        putExtra(Intent.EXTRA_TEXT, "Please review and sign the attached pre-inspection agreement.\n\nInspector: ${r.inspectorName}\nProperty: ${r.propertyAddress}\nDate: ${r.inspectionDate}")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Send Agreement"))
                    report?.id?.let { id -> viewModel.saveAgreementPath(id, uri.toString(), false) }
                }
            }
            val signedLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let {
                    report?.id?.let { id -> viewModel.saveAgreementPath(id, uri.toString(), true) }
                    Toast.makeText(context, "Signed agreement saved to report", Toast.LENGTH_SHORT).show()
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("📄 Inspection Agreement", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Navy)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (report?.signedAgreementPath?.isNotBlank() == true) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            null,
                            tint = if (report?.signedAgreementPath?.isNotBlank() == true) RatingGreen else Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (report?.signedAgreementPath?.isNotBlank() == true) "Signed agreement on file" else "No signed agreement yet",
                            fontSize = 12.sp,
                            color = if (report?.signedAgreementPath?.isNotBlank() == true) RatingGreen else Color(0xFF9CA3AF)
                        )
                    }
                    OutlinedButton(
                        onClick = { agreementLauncher.launch("application/pdf") },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, Gold),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, null, tint = Gold, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Select & Send Agreement to Client", color = Gold, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = { signedLauncher.launch("application/pdf") },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, Navy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Upload, null, tint = Navy, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Upload Signed Agreement", color = Navy, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
    }
}
}
    
@Composable
fun PropertyInfoScreen(viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        viewModel.onPhotoCaptured(success)
    }

    if (report == null) return  // <-- guard here instead

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Property Information", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    FormField(label = "Property Address", value = report?.propertyAddress ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(propertyAddress = v)) } })
                    FormField(label = "City, State, ZIP", value = report?.propertyCity ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(propertyCity = v)) } })
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FormField(label = "Year Built", value = report?.yearBuilt ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(yearBuilt = v)) } }, modifier = Modifier.weight(1f))
                        FormField(label = "Sq Ft", value = report?.squareFootage ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(squareFootage = v)) } }, modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FormField(label = "Inspection Date", value = report?.inspectionDate ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectionDate = v)) } }, modifier = Modifier.weight(1f))
                        FormField(label = "Weather", value = report?.weatherConditions ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(weatherConditions = v)) } }, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Client & Inspector", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    FormField(label = "Client Name", value = report?.clientName ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(clientName = v)) } })
                    FormField(label = "Client Email", value = report?.clientEmail ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(clientEmail = v)) } })
                    FormField(label = "Inspector Name", value = report?.inspectorName ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorName = v)) } })
                    FormField(label = "InterNACHI Cert #", value = report?.inspectorCert ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorCert = v)) } })
                    FormField(label = "Company", value = report?.inspectorCompany ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorCompany = v)) } })
                    FormField(label = "Phone", value = report?.inspectorPhone ?: "", onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorPhone = v)) } })
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Property Photos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    PhotoStrip(
                        photos = photos.filter { photo -> photo.section == "info" && photo.itemId == null },
                        onCameraClick = {
                            val uri = viewModel.prepareCameraUri(context, "info", null)
                            cameraLauncher.launch(uri)
                        },
                        onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, "info", null) },
                        onDeletePhoto = { photo -> viewModel.deletePhoto(photo) }
                    )
                    NarrativeBox(
                        value = report?.overviewNarrative ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(overviewNarrative = v)) } },
                        label = "📝 Property Overview Notes"
                    )
                    FormField(
                        label = "Access Limitations",
                        value = report?.limitations ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(limitations = v)) } },
                        singleLine = false
                    )
                }
            }
        }
    }
}
@Composable
fun SummaryScreen(viewModel: InspectionViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val report by viewModel.currentReport.collectAsState()
    val items by viewModel.items.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val settings by viewModel.appSettings.collectAsState()
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
                    Text(report?.propertyAddress?.ifBlank { "Address not set" } ?: "", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("Client: ${report?.clientName?.ifBlank { "—" } ?: "—"}", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
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
    val color = when (finding.rating) { Rating.SAFETY -> RatingRed; Rating.MAJOR -> RatingOrange; else -> RatingYellow }
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.padding(12.dp)) {
            Box(Modifier.width(4.dp).fillMaxHeight().background(color))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(finding.section.replaceFirstChar { it.uppercase() }, fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                Text(ci?.title ?: finding.itemId, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                if (finding.narrative.isNotBlank()) Text(finding.narrative, fontSize = 12.sp, color = Color(0xFF6B7280))
            }
            Surface(color = color, shape = RoundedCornerShape(4.dp)) {
                Text(finding.rating.short, Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                                PdfGenerator.generate(context, r, items.values.toList(), photos, settings)
                            }
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
                                PdfGenerator.generate(context, r, items.values.toList(), photos, settings)
                            }
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
}
