package com.proinspect.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proinspect.app.data.DefectLibrary
import com.proinspect.app.data.Rating
import java.io.File

@Composable
fun RatingRow(
    current: Rating,
    onRatingSelected: (Rating) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Rating.values().forEach { r ->
            val selected = current == r
            val color = ratingColor(r)
            OutlinedButton(
                onClick = { onRatingSelected(r) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected) color else Color.Transparent,
                    contentColor = if (selected) Color.White else color
                ),
                border = BorderStroke(1.5.dp, color),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(r.short, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minLines: Int = 1,
    singleLine: Boolean = true
) {
    val textStyle = TextStyle(
        fontSize = 14.sp,
        color = Color(0xFF1F2937)
    )
    Box(
        modifier = modifier
            .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
            .border(1.5.dp, if (value.isNotBlank()) Gold.copy(alpha = 0.5f) else Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        if (value.isEmpty()) {
            Text(placeholder, style = textStyle.copy(color = Color(0xFF9CA3AF)))
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            minLines = if (singleLine) 1 else minLines,
            cursorBrush = SolidColor(Gold),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                autoCorrect = true
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefectDropdown(
    itemId: String,
    onDefectSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = DefectLibrary.getDefectsForItem(itemId)
    if (options.isEmpty()) return

    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(
            "📋 Quick Defect",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Gold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .background(Color(0xFFFDF9F2), RoundedCornerShape(8.dp))
                    .border(1.5.dp, Gold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        selectedLabel.ifBlank { "Select a defect description..." },
                        fontSize = 13.sp,
                        color = if (selectedLabel.isBlank()) Color(0xFF9CA3AF) else Color(0xFF1F2937),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Gold
                    )
                }
            }
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(option.label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(
                                    option.description.take(80) + "...",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280),
                                    maxLines = 2
                                )
                            }
                        },
                        onClick = {
                            selectedLabel = option.label
                            onDefectSelected(option.description)
                            expanded = false
                        },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text("✏️ Write custom note...", fontSize = 13.sp, color = Navy, fontWeight = FontWeight.SemiBold)
                    },
                    onClick = {
                        selectedLabel = ""
                        onDefectSelected("")
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoStrip(
    photos: List<com.proinspect.app.data.InspectionPhoto>,
    onCameraClick: () -> Unit,
    onGalleryPick: (Uri) -> Unit,
    onDeletePhoto: (Long) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onGalleryPick(it) } }
    val size = if (compact) 72.dp else 100.dp

    Column(modifier = modifier) {
        if (!compact) {
            Text(
                "📷 Photos", fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6B7280), modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(size).clip(RoundedCornerShape(8.dp))
                    .background(Gold.copy(alpha = 0.12f))
                    .border(1.5.dp, Gold, RoundedCornerShape(8.dp))
                    .clickable { onCameraClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Gold,
                        modifier = Modifier.size(if (compact) 20.dp else 28.dp))
                    if (!compact) Text("Camera", fontSize = 10.sp, color = Gold, fontWeight = FontWeight.SemiBold)
                }
            }
            Box(
                modifier = Modifier.size(size).clip(RoundedCornerShape(8.dp))
                    .background(NavyLight.copy(alpha = 0.08f))
                    .border(1.5.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = Color(0xFF6B7280),
                        modifier = Modifier.size(if (compact) 20.dp else 28.dp))
                    if (!compact) Text("Gallery", fontSize = 10.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.SemiBold)
                }
            }
            photos.forEach { photo ->
                Box(modifier = Modifier.size(size)) {
                    AsyncImage(
                        model = File(photo.filePath),
                        contentDescription = "Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                    )
                    IconButton(
                        onClick = { onDeletePhoto(photo.id) },
                        modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Cancel, contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                                .background(RatingRed.copy(alpha = 0.85f), RoundedCornerShape(50))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NarrativeBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "📝 Inspector Narrative",
    placeholder: String = "Add narrative notes...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFDF9F2))
            .border(1.5.dp, Gold, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        ProTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            singleLine = false,
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ProTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = label,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ChecklistItemCard(
    item: com.proinspect.app.data.ChecklistItem,
    rating: Rating,
    narrative: String,
    photos: List<com.proinspect.app.data.InspectionPhoto>,
    onRatingChanged: (Rating) -> Unit,
    onNarrativeChanged: (String) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryPick: (Uri) -> Unit,
    onDeletePhoto: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rColor = ratingColor(rating)
    val hasDefects = DefectLibrary.getDefectsForItem(item.id).isNotEmpty()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(50)).background(rColor))
                Spacer(Modifier.width(8.dp))
                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                if (hasDefects) {
                    Surface(
                        color = Gold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            "Templates", fontSize = 9.sp, color = Gold, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = if (photos.isNotEmpty() || narrative.isNotBlank()) Gold else Color(0xFF9CA3AF)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            RatingRow(current = rating, onRatingSelected = { r ->
                onRatingChanged(r)
                if (r != Rating.NOT_RATED && r != Rating.GOOD) expanded = true
            })
            if (expanded) {
                Spacer(Modifier.height(12.dp))
                if (hasDefects) {
                    DefectDropdown(itemId = item.id, onDefectSelected = { description ->
                        if (description.isNotBlank()) {
                            val newNarrative = if (narrative.isBlank()) description else "$narrative\n\n$description"
                            onNarrativeChanged(newNarrative)
                        }
                    })
                    Spacer(Modifier.height(10.dp))
                }
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
