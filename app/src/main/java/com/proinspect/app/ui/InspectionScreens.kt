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
// GENERIC SECTION SCREEN
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
                    onRatingChanged = { onRatingChange(checklistItem.id, it) },
                    onNarrativeChanged = { onNarrativeChange(checklistItem.id, it) },
                    onCameraClick = { onCameraClick(checklistItem.id) },
                    onGalleryPick = { uri -> onGalleryPick(checklistItem.id, uri) },
                    onDeletePhoto = onDeletePhoto
                )
            }
        }
    }

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
                        text = sectionCodes,
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
                    Text(
                        text = codes,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
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
