import androidx.compose.foundation.layout.imePadding
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

// ─── Known problem electrical panels ──────────────────────────────────────────
data class ProblemPanel(
    val brand: String,
    val shortName: String,
    val narrative: String
)

val knownProblemPanels = listOf(
    ProblemPanel(
        brand = "Federal Pacific Electric (FPE) Stab-Lok®",
        shortName = "FPE Stab-Lok",
        narrative = "The electrical panel was identified as a Federal Pacific Electric (FPE) Stab-Lok® panel. FPE Stab-Lok panels have a well-documented history of breaker failure, including failure to trip under overload conditions, which presents a significant fire hazard. Replacement of this panel by a licensed electrician is strongly recommended. This is considered a safety concern."
    ),
    ProblemPanel(
        brand = "Zinsco / GTE-Sylvania",
        shortName = "Zinsco",
        narrative = "The electrical panel was identified as a Zinsco or GTE-Sylvania panel. These panels have a known history of breaker failure including breakers that overheat, melt, and fail to trip during an overload or short circuit, presenting a fire and shock hazard. Replacement by a licensed electrician is strongly recommended. This is considered a safety concern."
    ),
    ProblemPanel(
        brand = "Bulldog Pushmatic",
        shortName = "Pushmatic",
        narrative = "The electrical panel was identified as a Bulldog Pushmatic panel. Pushmatic panels use push-button breakers that are no longer manufactured, making replacement parts difficult to obtain. These breakers are known to fail to trip under overload conditions and the panel should be evaluated by a licensed electrician for replacement. This is considered a safety concern."
    ),
    ProblemPanel(
        brand = "Challenger",
        shortName = "Challenger",
        narrative = "The electrical panel was identified as a Challenger panel. Challenger panels have been associated with breaker failure and overheating issues. Evaluation and likely replacement by a licensed electrician is recommended. This is considered a safety concern."
    )
)

@Composable
fun ProblemPanelCard(
    onPanelSelected: (String) -> Unit
) {
    var selectedPanel by remember { mutableStateOf<ProblemPanel?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selectedPanel != null) Color(0xFFFEF2F2) else Color(0xFFFFFBF0)
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.5.dp,
            if (selectedPanel != null) RatingRed else Gold.copy(alpha = 0.5f)
        )
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚡", fontSize = 16.sp)
                Spacer(Modifier.width(6.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "Problem Panel Identifier",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (selectedPanel != null) RatingRed else Navy
                    )
                    Text(
                        "Tap if panel brand is identified below",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }
                if (selectedPanel != null) {
                    IconButton(
                        onClick = {
                            selectedPanel = null
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Panel brand chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                knownProblemPanels.chunked(2).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { panel ->
                            val isSelected = selectedPanel?.brand == panel.brand
                            OutlinedButton(
                                onClick = {
                                    selectedPanel = if (isSelected) null else panel
                                    if (!isSelected) onPanelSelected(panel.narrative)
                                },
                                modifier = Modifier.weight(1f).height(40.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) RatingRed.copy(alpha = 0.1f) else Color.Transparent,
                                    contentColor = if (isSelected) RatingRed else Color(0xFF374151)
                                ),
                                border = BorderStroke(
                                    1.5.dp,
                                    if (isSelected) RatingRed else Color(0xFFD1D5DB)
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    panel.shortName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                        // Fill remaining space if row has only 1 item
                        if (row.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            if (selectedPanel != null) {
                Surface(
                    color = RatingRed.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = RatingRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "⚠️ ${selectedPanel!!.brand} identified — narrative added to Main Panel (el2)",
                            fontSize = 11.sp,
                            color = RatingRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// ─── Custom checklist item card ────────────────────────────────────────────────
data class CustomChecklistItem(
    val id: String,
    val title: String,
    val section: String
)

@Composable
fun CustomItemCard(
    item: CustomChecklistItem,
    rating: Rating,
    narrative: String,
    photos: List<InspectionPhoto>,
    onRatingChanged: (Rating) -> Unit,
    onNarrativeChanged: (String) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryPick: (Uri) -> Unit,
    onDeletePhoto: (InspectionPhoto) -> Unit,
    onDeleteItem: () -> Unit,
    apiKey: String = ""
) {
    // Reuse ChecklistItemCard appearance by wrapping in same style
    var expanded by remember { mutableStateOf(true) }
    val rColor = ratingColor(rating)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF0)),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(rColor)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Gold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        "Custom",
                        fontSize = 9.sp,
                        color = Gold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
                IconButton(
                    onClick = onDeleteItem,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = RatingRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Gold
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            RatingRow(current = rating, onRatingSelected = { r -> onRatingChanged(r) })
            if (expanded) {
                Spacer(Modifier.height(12.dp))
                PhotoStrip(
                    photos = photos,
                    onCameraClick = onCameraClick,
                    onGalleryPick = onGalleryPick,
                    onDeletePhoto = onDeletePhoto,
                    compact = true
                )
                Spacer(Modifier.height(8.dp))
                NarrativeBox(
                    value = narrative,
                    onValueChange = onNarrativeChanged,
                    label = "📝 Item Notes",
                    placeholder = "Describe findings for: ${item.title}..."
                )
            }
        }
    }
}

// ─── Add Custom Item Dialog ────────────────────────────────────────────────────
@Composable
fun AddCustomItemDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Custom Item", fontWeight = FontWeight.Bold, color = Navy)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Enter a title for the custom checklist item:",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("e.g. Solar Panel System", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold,
                        unfocusedBorderColor = Color(0xFFD1D5DB)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onConfirm(title.trim()) },
                colors = ButtonDefaults.buttonColors(containerColor = Navy),
                enabled = title.isNotBlank()
            ) {
                Text("Add Item")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ─── Main Inspection Section Screen ───────────────────────────────────────────
@Composable
fun InspectionSectionScreen(section: String, viewModel: InspectionViewModel) {
    val context = LocalContext.current
    val report by viewModel.currentReport.collectAsState()
    val itemsMap by viewModel.items.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val settings by viewModel.appSettings.collectAsState()

    // Custom items state (session only)
    var customItems by remember { mutableStateOf(listOf<CustomChecklistItem>()) }
    var customItemRatings by remember { mutableStateOf(mapOf<String, Rating>()) }
    var customItemNarratives by remember { mutableStateOf(mapOf<String, String>()) }
    var showAddCustomDialog by remember { mutableStateOf(false) }
    var customItemCounter by remember { mutableStateOf(0) }

    var pendingCameraSection by remember { mutableStateOf<String?>(null) }
    var pendingCameraItemId by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        viewModel.onPhotoCaptured(success)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val sec = pendingCameraSection
            val itemId = pendingCameraItemId
            if (sec != null) {
                try {
                    val uri = viewModel.prepareCameraUri(context, sec, itemId)
                    cameraLauncher.launch(uri)
                } catch (e: Exception) {
                    Toast.makeText(context, "Camera error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    fun launchCamera(sec: String, itemId: String?) {
        pendingCameraSection = sec
        pendingCameraItemId = itemId
        when {
            androidx.core.content.ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                try {
                    val uri = viewModel.prepareCameraUri(context, sec, itemId)
                    cameraLauncher.launch(uri)
                } catch (e: Exception) {
                    Toast.makeText(context, "Camera error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            else -> permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    val sectionItemsList = InspectionSections.items[section] ?: emptyList()
    val sectionName = InspectionSections.sectionNames[section] ?: section

    if (showAddCustomDialog) {
        AddCustomItemDialog(
            onConfirm = { title ->
                customItemCounter++
                val newId = "custom_${section}_$customItemCounter"
                customItems = customItems + CustomChecklistItem(
                    id = newId,
                    title = title,
                    section = section
                )
                showAddCustomDialog = false
            },
            onDismiss = { showAddCustomDialog = false }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── Overview photos card ──
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(
                        "$sectionName — Overview Photos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Navy
                    )
                    Spacer(Modifier.height(10.dp))
                    PhotoStrip(
                        photos = photos.filter { it.section == section && it.itemId == null },
                        onCameraClick = { launchCamera(section, null) },
                        onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, null) },
                        onDeletePhoto = { photo -> viewModel.deletePhoto(photo) }
                    )
                }
            }
        }

        // ── Problem Panel Identifier (electrical only) ──
        if (section == "electrical") {
            item {
                ProblemPanelCard(
                    onPanelSelected = { narrative ->
                        // Auto-fill el2 (Main Electrical Panel) narrative
                        val existing = itemsMap["el2"]?.narrative ?: ""
                        val updated = if (existing.isBlank()) narrative else "$existing\n\n$narrative"
                        viewModel.setItemNarrative("el2", "electrical", updated)
                        viewModel.setItemRating("el2", "electrical", Rating.SAFETY)
                    }
                )
            }
        }

        // ── Checklist header ──
        item {
            Text(
                "InterNACHI Checklist",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
        }

        // ── Standard checklist items ──
        items(sectionItemsList) { checklistItem ->
            val itemState = itemsMap[checklistItem.id]
            ChecklistItemCard(
                item = checklistItem,
                rating = itemState?.rating ?: Rating.NOT_RATED,
                narrative = itemState?.narrative ?: "",
                photos = photos.filter { it.itemId == checklistItem.id },
                onRatingChanged = { rating -> viewModel.setItemRating(checklistItem.id, section, rating) },
                onNarrativeChanged = { text -> viewModel.setItemNarrative(checklistItem.id, section, text) },
                onCameraClick = { launchCamera(section, checklistItem.id) },
                onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, checklistItem.id) },
                onDeletePhoto = { photo -> viewModel.deletePhoto(photo) },
                apiKey = settings.anthropicApiKey
            )
        }

        // ── Custom checklist items ──
        items(customItems) { customItem ->
            CustomItemCard(
                item = customItem,
                rating = customItemRatings[customItem.id] ?: Rating.NOT_RATED,
                narrative = customItemNarratives[customItem.id] ?: "",
                photos = photos.filter { it.itemId == customItem.id },
                onRatingChanged = { rating ->
                    customItemRatings = customItemRatings + (customItem.id to rating)
                },
                onNarrativeChanged = { text ->
                    customItemNarratives = customItemNarratives + (customItem.id to text)
                },
                onCameraClick = { launchCamera(section, customItem.id) },
                onGalleryPick = { uri -> viewModel.addPhotoFromGallery(context, uri, section, customItem.id) },
                onDeletePhoto = { photo -> viewModel.deletePhoto(photo) },
                onDeleteItem = {
                    customItems = customItems.filter { it.id != customItem.id }
                    customItemRatings = customItemRatings - customItem.id
                    customItemNarratives = customItemNarratives - customItem.id
                },
                apiKey = settings.anthropicApiKey
            )
        }

        // ── Add Custom Item button ──
        item {
            OutlinedButton(
                onClick = { showAddCustomDialog = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.5.dp, Gold.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Gold.copy(alpha = 0.05f)
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "+ Add Custom Item",
                    color = Gold,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }

        // ── Overall section narrative ──
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

        // ── Agreement card ──
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
                            if (report?.signedAgreementPath?.isNotBlank() == true)
                                Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            null,
                            tint = if (report?.signedAgreementPath?.isNotBlank() == true)
                                RatingGreen else Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (report?.signedAgreementPath?.isNotBlank() == true)
                                "Signed agreement on file" else "No signed agreement yet",
                            fontSize = 12.sp,
                            color = if (report?.signedAgreementPath?.isNotBlank() == true)
                                RatingGreen else Color(0xFF9CA3AF)
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
                        Text(
                            "Select & Send Agreement to Client",
                            color = Gold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedButton(
                        onClick = { signedLauncher.launch("application/pdf") },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, Navy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Upload, null, tint = Navy, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Upload Signed Agreement",
                            color = Navy,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(20.dp)) }
    }
}

// ─── Property Info Screen ──────────────────────────────────────────────────────
@Composable
fun PropertyInfoScreen(viewModel: InspectionViewModel) {
    val report by viewModel.currentReport.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        viewModel.onPhotoCaptured(success)
    }

    if (report == null) return

LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Property Information", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    FormField(
                        label = "Property Address",
                        value = report?.propertyAddress ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(propertyAddress = v)) } }
                    )
                    FormField(
                        label = "City, State, ZIP",
                        value = report?.propertyCity ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(propertyCity = v)) } }
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FormField(
                            label = "Year Built",
                            value = report?.yearBuilt ?: "",
                            onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(yearBuilt = v)) } },
                            modifier = Modifier.weight(1f)
                        )
                        FormField(
                            label = "Sq Ft",
                            value = report?.squareFootage ?: "",
                            onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(squareFootage = v)) } },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FormField(
                            label = "Inspection Date",
                            value = report?.inspectionDate ?: "",
                            onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectionDate = v)) } },
                            modifier = Modifier.weight(1f)
                        )
                        FormField(
                            label = "Weather",
                            value = report?.weatherConditions ?: "",
                            onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(weatherConditions = v)) } },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Client & Inspector", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    FormField(
                        label = "Client Name",
                        value = report?.clientName ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(clientName = v)) } }
                    )
                    FormField(
                        label = "Client Email",
                        value = report?.clientEmail ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(clientEmail = v)) } }
                    )
                    FormField(
                        label = "Inspector Name",
                        value = report?.inspectorName ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorName = v)) } }
                    )
                    FormField(
                        label = "InterNACHI Cert #",
                        value = report?.inspectorCert ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorCert = v)) } }
                    )
                    FormField(
                        label = "Company",
                        value = report?.inspectorCompany ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorCompany = v)) } }
                    )
                    FormField(
                        label = "Phone",
                        value = report?.inspectorPhone ?: "",
                        onValueChange = { v -> report?.let { viewModel.saveReport(it.copy(inspectorPhone = v)) } }
                    )
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Property Photos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    PhotoStrip(
                        photos = photos.filter { it.section == "info" && it.itemId == null },
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

// ─── Summary Screen ────────────────────────────────────────────────────────────
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
        .filter { it.rating == Rating.SAFETY || it.rating == Rating.MAJOR || it.rating == Rating.MONITOR }
        .sortedWith(compareBy { item ->
            when (item.rating) { Rating.SAFETY -> 0; Rating.MAJOR -> 1; else -> 2 }
        })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy, RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text("Inspection Summary", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = GoldLight)
                    Text(
                        report?.propertyAddress?.ifBlank { "Address not set" } ?: "",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        "Client: ${report?.clientName?.ifBlank { "—" } ?: "—"}",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
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
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(icon, fontSize = 18.sp)
                            Text(count.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
                        }
                    }
                }
            }
        }

        if (findings.isNotEmpty()) {
            item {
                Text("Priority Findings", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
            }
            items(findings) { finding ->
                val checklistItem = InspectionSections.allItems.find { it.id == finding.itemId }
                val color = when (finding.rating) {
                    Rating.SAFETY -> RatingRed
                    Rating.MAJOR -> RatingOrange
                    else -> RatingYellow
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(Modifier.padding(12.dp)) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(color))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                finding.section.replaceFirstChar { it.uppercase() },
                                fontSize = 10.sp,
                                color = Color(0xFF9CA3AF),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                checklistItem?.title ?: finding.itemId,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            if (finding.narrative.isNotBlank()) {
                                Text(finding.narrative, fontSize = 12.sp, color = Color(0xFF6B7280))
                            }
                        }
                        Surface(color = color, shape = RoundedCornerShape(4.dp)) {
                            Text(
                                finding.rating.short,
                                Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
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
                                PdfGenerator.generate(context, r, items.values.toList(), photos, settings)
                            }
                            val uri = FileProvider.getUriForFile(
                                context, "${context.packageName}.fileprovider", file
                            )
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
                            val uri = FileProvider.getUriForFile(
                                context, "${context.packageName}.fileprovider", file
                            )
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                putExtra(Intent.EXTRA_SUBJECT, "Home Inspection Report — ${r.propertyAddress}")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(r.clientEmail))
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Please find your home inspection report attached.\n\nInspector: ${r.inspectorName}\nDate: ${r.inspectionDate}"
                                )
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
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Please review and sign the attached pre-inspection agreement.\n\nInspector: ${r.inspectorName}\nProperty: ${r.propertyAddress}\nDate: ${r.inspectionDate}"
                        )
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
                            if (report?.signedAgreementPath?.isNotBlank() == true)
                                Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            null,
                            tint = if (report?.signedAgreementPath?.isNotBlank() == true)
                                RatingGreen else Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (report?.signedAgreementPath?.isNotBlank() == true)
                                "Signed agreement on file" else "No signed agreement yet",
                            fontSize = 12.sp,
                            color = if (report?.signedAgreementPath?.isNotBlank() == true)
                                RatingGreen else Color(0xFF9CA3AF)
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
                        Text(
                            "Select & Send Agreement to Client",
                            color = Gold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedButton(
                        onClick = { signedLauncher.launch("application/pdf") },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, Navy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Upload, null, tint = Navy, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Upload Signed Agreement",
                            color = Navy,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(20.dp)) }
    }
}
