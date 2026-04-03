package com.proinspect.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.proinspect.app.data.*
import java.io.File

// ══════════════════════════════════════════════════════════════════════════════
// CHECKLIST ITEM CARD
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun ChecklistItemCard(
    item: ChecklistItem,
    rating: Rating,
    narrative: String,
    photos: List<InspectionPhoto>,
    ircVersion: String,
    onRatingChanged: (Rating) -> Unit,
    onNarrativeChanged: (String) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryPick: (Uri) -> Unit,
    onDeletePhoto: (InspectionPhoto) -> Unit,
    onVoiceInput: (() -> Unit)? = null,
    apiKey: String = ""
) {
    var showNarrativeDialog by remember { mutableStateOf(false) }
    var showIrcDialog by remember { mutableStateOf(false) }
    var ircContent by remember { mutableStateOf("") }
    var isLoadingIrc by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onGalleryPick(it) } }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    item.label,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Navy,
                    modifier = Modifier.weight(1f)
                )

                if (rating != Rating.NOT_RATED) {
                    Box(
                        modifier = Modifier
                            .background(
                                ratingColor(rating).copy(alpha = 0.15f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            rating.short,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ratingColor(rating)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Rating Buttons ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RatingButton(
                    label = "Safety",
                    color = RatingRed,
                    selected = rating == Rating.SAFETY,
                    onClick = { onRatingChanged(Rating.SAFETY) },
                    modifier = Modifier.weight(1f)
                )
                RatingButton(
                    label = "Major",
                    color = RatingOrange,
                    selected = rating == Rating.MAJOR,
                    onClick = { onRatingChanged(Rating.MAJOR) },
                    modifier = Modifier.weight(1f)
                )
                RatingButton(
                    label = "Monitor",
                    color = RatingYellow,
                    selected = rating == Rating.MONITOR,
                    onClick = { onRatingChanged(Rating.MONITOR) },
                    modifier = Modifier.weight(1f)
                )
                RatingButton(
                    label = "Good",
                    color = RatingGreen,
                    selected = rating == Rating.GOOD,
                    onClick = { onRatingChanged(Rating.GOOD) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))

            // N/A Button
            RatingButton(
                label = "Not Present / N/A",
                color = RatingGray,
                selected = rating == Rating.NOT_PRESENT,
                onClick = { onRatingChanged(Rating.NOT_PRESENT) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // ── Action Buttons ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Camera
                OutlinedButton(
                    onClick = onCameraClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                    border = BorderStroke(1.dp, Navy)
                ) {
                    Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Camera", fontSize = 12.sp)
                }

                // Gallery
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                    border = BorderStroke(1.dp, Navy)
                ) {
                    Icon(Icons.Default.Image, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Gallery", fontSize = 12.sp)
                }

                // IRC Code
                OutlinedButton(
                    onClick = {
                        isLoadingIrc = true
                        showIrcDialog = true
                        ircContent = IrcCodes.getCodeForItem(item.section, item.id, ircVersion)
                        isLoadingIrc = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold),
                    border = BorderStroke(1.dp, Gold)
                ) {
                    Icon(Icons.Default.MenuBook, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("IRC", fontSize = 12.sp)
                }
            }

            // ── Photos ────────────────────────────────────────────────────────
            if (photos.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    photos.forEach { photo ->
                        PhotoThumbnail(photo = photo, onDelete = { onDeletePhoto(photo) })
                    }
                }
            }

            // ── Narrative ─────────────────────────────────────────────────────
            if (narrative.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                        .clickable { showNarrativeDialog = true }
                ) {
                    Text(
                        narrative,
                        fontSize = 13.sp,
                        color = Color(0xFF374151),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { showNarrativeDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Add Narrative", fontSize = 13.sp)
                }
            }
        }
    }

    // ── Narrative Dialog ──────────────────────────────────────────────────────
    if (showNarrativeDialog) {
        var editText by remember { mutableStateOf(narrative) }
        AlertDialog(
            onDismissRequest = { showNarrativeDialog = false },
            title = { Text("Narrative: ${item.label}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        placeholder = { Text("Enter detailed observations...") },
                        maxLines = 10
                    )
                    if (onVoiceInput != null) {
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onVoiceInput,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Mic, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Voice Input")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onNarrativeChanged(editText)
                    showNarrativeDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNarrativeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ── IRC Code Dialog ───────────────────────────────────────────────────────
    if (showIrcDialog) {
        AlertDialog(
            onDismissRequest = { showIrcDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("IRC Code", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(item.label, fontSize = 13.sp, color = Color.Gray)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (isLoadingIrc) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Gold)
                        }
                    } else {
                        Text(
                            ircContent,
                            fontSize = 13.sp,
                            color = Color(0xFF374151),
                            lineHeight = 19.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showIrcDialog = false }) {
                    Text("Close", color = Navy, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// PHOTO THUMBNAIL
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun PhotoThumbnail(photo: InspectionPhoto, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(File(photo.photoPath))
                .size(300)
                .crossfade(true)
                .build(),
            contentDescription = "Photo",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                .clickable { showDeleteDialog = true },
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Photo?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// RATING BUTTON
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun RatingButton(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(38.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) color else Color.White,
            contentColor = if (selected) Color.White else color
        ),
        border = BorderStroke(1.5.dp, color),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION SCREENS
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun GenericSectionScreen(
    section: String,
    checklist: List<ChecklistItem>,
    items: Map<String, InspectionItem>,
    photos: List<InspectionPhoto>,
    ircVersion: String,
    onRatingChange: (String, Rating) -> Unit,
    onNarrativeChange: (String, String) -> Unit,
    onCameraClick: (String) -> Unit,
    onGalleryPick: (String, Uri) -> Unit,
    onDeletePhoto: (InspectionPhoto) -> Unit
) {
    var showIrcSection by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        // Section IRC Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Gold.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showIrcSection = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "IRC Code Reference",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Navy
                    )
                    Text(
                        "View $section inspection codes",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Gold
                )
            }
        }

        // Checklist Items
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(checklist) { checklistItem ->
                val itemData = items[checklistItem.id]
                val itemPhotos = photos.filter { it.itemId == checklistItem.id }

                ChecklistItemCard(
                    item = checklistItem,
                    rating = itemData?.rating ?: Rating.NOT_RATED,
                    narrative = itemData?.narrative ?: "",
                    photos = itemPhotos,
                    ircVersion = ircVersion,
                    onRatingChanged = { onRatingChange(checklistItem.id, it) },
                    onNarrativeChanged = { onNarrativeChange(checklistItem.id, it) },
                    onCameraClick = { onCameraClick(checklistItem.id) },
                    onGalleryPick = { uri -> onGalleryPick(checklistItem.id, uri) },
                    onDeletePhoto = onDeletePhoto
                )
            }
        }
    }

    // Section IRC Dialog
    if (showIrcSection) {
        val sectionCodes = IrcCodes.getCodesForSection(section, ircVersion)
        AlertDialog(
            onDismissRequest = { showIrcSection = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("$section - IRC Codes")
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        sectionCodes,
                        fontSize = 13.sp,
                        color = Color(0xFF374151),
                        lineHeight = 19.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showIrcSection = false }) {
                    Text("Close", color = Navy, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// IRC CODE BUTTON (Standalone)
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun IrcCodeButton(section: String, ircVersion: String, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { showDialog = true },
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold),
        border = BorderStroke(1.5.dp, Gold)
    ) {
        Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("View IRC Codes", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }

    if (showDialog) {
        val codes = IrcCodes.getCodesForSection(section, ircVersion)
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, null, tint = Gold, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("$section IRC Codes")
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(codes, fontSize = 13.sp, lineHeight = 19.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close", color = Navy)
                }
            }
        )
    }
}
