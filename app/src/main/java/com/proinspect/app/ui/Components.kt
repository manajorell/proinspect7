package com.proinspect.app.ui

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proinspect.app.data.DefectLibrary
import com.proinspect.app.data.*
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.graphics.BitmapFactory
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer

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
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, fontSize = 14.sp, color = Color(0xFF9CA3AF)) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else minLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedContainerColor = Color(0xFFF3F4F6),
            unfocusedContainerColor = Color(0xFFF3F4F6)
        ),
        shape = RoundedCornerShape(8.dp),
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 14.sp,
            color = Color(0xFF1F2937)
        )
    )
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
                        Text(
                            "✏️ Write custom note...",
                            fontSize = 13.sp,
                            color = Navy,
                            fontWeight = FontWeight.SemiBold
                        )
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
    photos: List<InspectionPhoto>,
    onCameraClick: () -> Unit,
    onGalleryPick: (Uri) -> Unit,
    onDeletePhoto: (InspectionPhoto) -> Unit,
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
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Gold.copy(alpha = 0.12f))
                    .border(1.5.dp, Gold, RoundedCornerShape(8.dp))
                    .clickable { onCameraClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.CameraAlt, contentDescription = "Camera", tint = Gold,
                        modifier = Modifier.size(if (compact) 20.dp else 28.dp)
                    )
                    if (!compact) Text("Camera", fontSize = 10.sp, color = Gold, fontWeight = FontWeight.SemiBold)
                }
            }
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyLight.copy(alpha = 0.08f))
                    .border(1.5.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.PhotoLibrary, contentDescription = "Gallery",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(if (compact) 20.dp else 28.dp)
                    )
                    if (!compact) Text("Gallery", fontSize = 10.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.SemiBold)
                }
            }
            photos.forEach { photo ->
                Box(modifier = Modifier.size(size)) {
                    AsyncImage(
                        model = File(photo.photoPath),
                        contentDescription = "Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                    )
                    IconButton(
                        onClick = { onDeletePhoto(photo) },
                        modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Cancel, contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
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
    label: String = "📝 Notes",
    placeholder: String = "Add notes...",
    modifier: Modifier = Modifier,
    onVoiceInput: (() -> Unit)? = null
) {
    var localValue by remember(value) { mutableStateOf(value) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Navy)
            if (onVoiceInput != null) {
                IconButton(onClick = onVoiceInput, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice input",
                        tint = Gold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = localValue,
            onValueChange = {
                localValue = it
                onValueChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp, color = Color(0xFF9CA3AF)) },
            minLines = 3,
            maxLines = 8,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Color(0xFFD1D5DB)
            ),
            shape = RoundedCornerShape(8.dp)
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
            label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF6B7280), modifier = Modifier.padding(bottom = 4.dp)
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

suspend fun decodeSerialNumber(
    context: android.content.Context,
    uri: Uri,
    equipmentName: String,
    apiKey: String
): String {
    return withContext(Dispatchers.IO) {
        try {
            var localResult: String? = null
            try {
                val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
                if (bitmap != null) {
                    val textRecognizer = TextRecognizer.Builder(context).build()
                    if (textRecognizer.isOperational) {
                        val frame = Frame.Builder().setBitmap(bitmap).build()
                        val textBlocks = textRecognizer.detect(frame)
                        val extractedText = StringBuilder()
                        for (i in 0 until textBlocks.size()) {
                            val textBlock = textBlocks.valueAt(i)
                            extractedText.append(textBlock.value).append("\n")
                        }
                        textRecognizer.release()
                        val parsedData = parseSerialPlateText(extractedText.toString(), equipmentName)
                        if (parsedData.isNotEmpty() && parsedData.contains("Manufacturer:")) {
                            localResult = parsedData
                        }
                    }
                }
            } catch (e: Exception) {}

            if (localResult != null) return@withContext "✅ Local Decode:\n$localResult"
            if (apiKey.isBlank()) return@withContext "⚠️ Local decode failed. API key needed for AI decode."

            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw Exception("Failed to read image")
            val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)

            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val json = org.json.JSONObject().apply {
                put("model", "claude-3-5-sonnet-20241022")
                put("max_tokens", 1024)
                put("messages", org.json.JSONArray().put(
                    org.json.JSONObject().apply {
                        put("role", "user")
                        put("content", org.json.JSONArray().apply {
                            put(org.json.JSONObject().apply {
                                put("type", "image")
                                put("source", org.json.JSONObject().apply {
                                    put("type", "base64")
                                    put("media_type", "image/jpeg")
                                    put("data", base64)
                                })
                            })
                            put(org.json.JSONObject().apply {
                                put("type", "text")
                                put("text", "This is a serial number plate from a $equipmentName. Extract: 1) Manufacturer, 2) Model number, 3) Serial number, 4) Manufacture date or age, 5) Capacity (BTU, gallons, or tons). Reply in this exact format:\nManufacturer: \nModel: \nSerial: \nYear/Age: \nCapacity: ")
                            })
                        })
                    }
                ))
            }

            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = okhttp3.Request.Builder()
                .url("https://api.anthropic.com/v1/messages")
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("content-type", "application/json")
                .post(body)
                .build()

            val resp = client.newCall(request).execute()
            val responseBody = resp.body?.string() ?: throw Exception("Empty response")
            if (!resp.isSuccessful) throw Exception("API Error ${resp.code}: $responseBody")

            val respJson = org.json.JSONObject(responseBody)
            val content = respJson.getJSONArray("content")
            if (content.length() == 0) throw Exception("No content in API response")
            return@withContext "🤖 AI Decode:\n${content.getJSONObject(0).getString("text")}"

        } catch (e: Exception) {
            return@withContext "❌ Error: ${e.localizedMessage ?: "Unknown error occurred"}"
        }
    }
}

fun parseSerialPlateText(text: String, equipmentName: String): String {
    val lines = text.lines().map { it.trim() }
    var manufacturer = ""; var model = ""; var serial = ""; var year = ""; var capacity = ""
    val mfgPatterns = listOf("rheem","ruud","carrier","trane","lennox","goodman","amana","york",
        "american standard","bryant","payne","bradford white","a.o. smith","ao smith","state",
        "whirlpool","ge","frigidaire","mitsubishi","daikin","fujitsu","lg","samsung","coleman","heil")
    for (line in lines) {
        val lower = line.lowercase()
        val cleaned = lower.replace("0","o").replace("|","i").replace("1","l")
        if (manufacturer.isEmpty()) for (mfg in mfgPatterns) if (cleaned.contains(mfg)) {
            manufacturer = mfg.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }; break
        }
        if (model.isEmpty() && (cleaned.contains("model")||cleaned.contains("mod")||cleaned.contains("m/n")||cleaned.contains("m.n")))
            model = line.replace(Regex("(?i)(model|mod|m/n|m\\.n)[:\\s]*"),"").trim()
        if (serial.isEmpty() && (cleaned.contains("serial")||cleaned.contains("ser")||cleaned.contains("s/n")||cleaned.contains("s.n")))
            serial = line.replace(Regex("(?i)(serial|ser|s/n|s\\.n)[:\\s]*"),"").trim()
        if (year.isEmpty()) {
            val yearMatch = Regex("\\b(19|20)\\d{2}\\b").find(line)
            if (yearMatch != null) year = yearMatch.value
            else if (cleaned.contains("mfg")||cleaned.contains("mfr")||cleaned.contains("date")||cleaned.contains("manufactured")) {
                val dateText = line.replace(Regex("(?i)(mfg|mfr|mer|date|manufactured|manuf)[:\\s]*"),"").trim()
                val y = Regex("(19|20)\\d{2}").find(dateText)
                if (y != null) year = y.value else if (dateText.isNotBlank() && dateText.length <= 10) year = dateText
            } else if (cleaned.contains("year")||cleaned.contains("yr"))
                year = line.replace(Regex("(?i)(year|yr)[:\\s]*"),"").trim()
        }
        if (capacity.isEmpty()) {
            capacity = (Regex("\\d+[,\\s]?\\d*\\s*(btu|btuh)",RegexOption.IGNORE_CASE).find(line)?.value
                ?: Regex("\\d+\\s*(gal|gallon|gallons)",RegexOption.IGNORE_CASE).find(line)?.value
                ?: Regex("\\d+\\.?\\d*\\s*(ton|tons)",RegexOption.IGNORE_CASE).find(line)?.value
                ?: Regex("\\d+\\.?\\d*\\s*(kw|kilowatt)",RegexOption.IGNORE_CASE).find(line)?.value ?: "")
                .replace(Regex("\\s+")," ").trim()
        }
    }
    if ((year.isEmpty()||year.length<4) && serial.isNotEmpty()) {
        val decoded = decodeYearFromSerial(serial, manufacturer)
        if (decoded.isNotEmpty()) year = decoded
    }
    if (listOf(manufacturer,model,serial,year,capacity).count { it.isNotEmpty() } < 3) return ""
    return buildString {
        if (manufacturer.isNotEmpty()) appendLine("Manufacturer: $manufacturer")
        if (model.isNotEmpty()) appendLine("Model: $model")
        if (serial.isNotEmpty()) appendLine("Serial: $serial")
        if (year.isNotEmpty()) appendLine("Year/Age: $year")
        if (capacity.isNotEmpty()) appendLine("Capacity: $capacity")
    }.trim()
}

fun decodeYearFromSerial(serial: String, manufacturer: String): String {
    if (serial.length < 4) return ""
    val cleanSerial = serial.replace(Regex("[\\s-]"),"").uppercase()
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    return when (manufacturer.lowercase()) {
        "rheem","ruud" -> if (cleanSerial.length >= 6) {
            val y = cleanSerial.substring(4,6).toIntOrNull()
            if (y != null) { val fy = if (y<=50) 2000+y else 1900+y; if (fy<=currentYear) "$fy (from serial)" else "" } else ""
        } else ""
        "carrier","bryant","payne" -> if (cleanSerial.length >= 4) {
            val y = cleanSerial[3].toString().toIntOrNull()
            if (y != null) { var yr = 2010+y; while (yr>currentYear) yr-=10; "$yr (from serial)" } else ""
        } else ""
        "trane","american standard" -> if (cleanSerial.length >= 4) {
            val y = cleanSerial.substring(2,4).toIntOrNull()
            if (y != null) { val fy = if (y<=50) 2000+y else 1900+y; if (fy<=currentYear) "$fy (from serial)" else "" } else ""
        } else ""
        "lennox" -> if (cleanSerial.length >= 3) {
            val y = cleanSerial.substring(1,3).toIntOrNull()
            if (y != null) { val fy = if (y<=50) 2000+y else 1900+y; if (fy<=currentYear) "$fy (from serial)" else "" } else ""
        } else ""
        "goodman","amana" -> if (cleanSerial.length >= 4) {
            val y = cleanSerial.substring(2,4).toIntOrNull()
            if (y != null) { val fy = if (y<=50) 2000+y else 1900+y; if (fy<=currentYear) "$fy (from serial)" else "" } else ""
        } else ""
        "york" -> if (cleanSerial.isNotEmpty() && cleanSerial[0].isLetter()) {
            val yr = 2004 + (cleanSerial[0] - 'A'); if (yr in 2004..currentYear) "$yr (from serial)" else ""
        } else ""
        else -> {
            val y = Regex("(19|20)\\d{2}").find(cleanSerial)
            if (y != null) { val yr = y.value.toInt(); if (yr in 1980..currentYear) "${y.value} (from serial)" else "" } else ""
        }
    }
}

@Composable
fun ChecklistItemCard(
    item: ChecklistItem,
    rating: Rating,
    narrative: String,
    photos: List<InspectionPhoto>,
    ircVersion: String = "",
    onRatingChanged: (Rating) -> Unit,
    onNarrativeChanged: (String) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryPick: (Uri) -> Unit,
    onDeletePhoto: (InspectionPhoto) -> Unit,
    onVoiceInput: (() -> Unit)? = null,
    apiKey: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val rColor = ratingColor(rating)
    var showIrcDialog by remember { mutableStateOf(false) }
    var ircContent by remember { mutableStateOf("") }
    val hasDefects = DefectLibrary.getDefectsForItem(item.id).isNotEmpty()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onGalleryPick(it) } }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp)
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
                    item.label, fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                if (hasDefects) {
                    Surface(
                        color = Gold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            "Templates", fontSize = 9.sp, color = Gold,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(28.dp)
                ) {
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

                // ── Camera / Gallery / IRC buttons ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = onCameraClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                        border = BorderStroke(1.dp, Navy),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Camera", fontSize = 11.sp)
                    }
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                        border = BorderStroke(1.dp, Navy),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Image, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Gallery", fontSize = 11.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            ircContent = IrcCodes.getCodeForItem(item.section, item.id, ircVersion)
                            showIrcDialog = true
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold),
                        border = BorderStroke(1.dp, Gold),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.MenuBook, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("IRC", fontSize = 11.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))

                // ── Photos display ──
                if (photos.isNotEmpty()) {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        photos.forEach { photo ->
                            Box(modifier = Modifier.size(72.dp)) {
                                AsyncImage(
                                    model = File(photo.photoPath),
                                    contentDescription = "Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                                )
                                IconButton(
                                    onClick = { onDeletePhoto(photo) },
                                    modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Cancel, contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(RatingRed.copy(alpha = 0.85f), RoundedCornerShape(50))
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                // ── Quick Defect ──
                if (hasDefects) {
                    DefectDropdown(
                        itemId = item.id,
                        onDefectSelected = { description ->
                            if (description.isNotBlank()) {
                                val newNarrative = if (narrative.isBlank()) description
                                else "$narrative\n\n$description"
                                onNarrativeChanged(newNarrative)
                            }
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                // ── Narrative ──
                NarrativeBox(
                    value = narrative,
                    onValueChange = onNarrativeChanged,
                    label = "📝 Item Notes",
                    placeholder = "Describe findings for: ${item.label}...",
                    onVoiceInput = onVoiceInput
                )

                // ── Serial Decoder (HVAC/Plumbing/Electrical) ──
                if (item.id in listOf("pl3", "hv1", "hv2", "el2")) {
                    val equipmentName = when (item.id) {
                        "pl3" -> "Water Heater"
                        "hv1" -> "Furnace / Air Handler"
                        "hv2" -> "AC Condenser"
                        "el2" -> "Electrical Panel"
                        else -> "Equipment"
                    }
                    var isDecoding by remember { mutableStateOf(false) }
                    var decodedResult by remember { mutableStateOf<String?>(null) }

                    val photoUri = remember {
                        val photoFile = File(context.cacheDir, "serial_decode_${System.currentTimeMillis()}.jpg")
                        androidx.core.content.FileProvider.getUriForFile(
                            context, "${context.packageName}.fileprovider", photoFile
                        )
                    }

                    val serialCameraLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.TakePicture()
                    ) { success ->
                        if (success) {
                            android.widget.Toast.makeText(context, "Photo captured, decoding...", android.widget.Toast.LENGTH_SHORT).show()
                            isDecoding = true
                            decodedResult = null
                            scope.launch {
                                val result = decodeSerialNumber(context, photoUri, equipmentName, apiKey)
                                decodedResult = result
                                isDecoding = false
                            }
                        } else {
                            android.widget.Toast.makeText(context, "Photo capture cancelled", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }

                    val cameraPermissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        if (isGranted) serialCameraLauncher.launch(photoUri)
                        else android.widget.Toast.makeText(context, "Camera permission denied", android.widget.Toast.LENGTH_SHORT).show()
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            val permission = android.Manifest.permission.CAMERA
                            if (androidx.core.content.ContextCompat.checkSelfPermission(context, permission) ==
                                android.content.pm.PackageManager.PERMISSION_GRANTED)
                                serialCameraLauncher.launch(photoUri)
                            else cameraPermissionLauncher.launch(permission)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Gold),
                        enabled = !isDecoding
                    ) {
                        if (isDecoding) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Gold)
                            Spacer(Modifier.width(8.dp))
                            Text("Decoding...", color = Gold, fontSize = 12.sp)
                        } else {
                            Text("📷 Decode Serial Number", color = Gold)
                        }
                    }

                    decodedResult?.let { result ->
                        Spacer(Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    result.startsWith("✅") -> Color(0xFFECFDF5)
                                    result.startsWith("🤖") -> Color(0xFFFFFBF0)
                                    result.startsWith("⚠️") -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFFEE2E2)
                                }
                            ),
                            border = BorderStroke(1.dp, when {
                                result.startsWith("❌") -> Color(0xFFEF4444)
                                result.startsWith("⚠️") -> Color(0xFFF59E0B)
                                else -> Gold
                            })
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    when {
                                        result.startsWith("✅") -> "📋 Decoded Locally"
                                        result.startsWith("🤖") -> "📋 Decoded via AI"
                                        result.startsWith("⚠️") -> "⚠️ Partial Result"
                                        else -> "❌ Decode Failed"
                                    },
                                    fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy
                                )
                                Text(result, fontSize = 12.sp, color = Color(0xFF374151))
                                if (!result.startsWith("❌")) {
                                    Spacer(Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                val cleanResult = result
                                                    .replace("✅ Local Decode:\n", "")
                                                    .replace("🤖 AI Decode:\n", "")
                                                    .replace("⚠️ ", "")
                                                onNarrativeChanged(
                                                    if (narrative.isBlank()) cleanResult
                                                    else "$narrative\n\n$cleanResult"
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Navy),
                                            modifier = Modifier.weight(1f)
                                        ) { Text("Copy to Notes", fontSize = 12.sp) }
                                        OutlinedButton(
                                            onClick = { decodedResult = null },
                                            modifier = Modifier.weight(1f),
                                            border = BorderStroke(1.dp, Color(0xFF9CA3AF))
                                        ) { Text("Clear", fontSize = 12.sp, color = Color(0xFF6B7280)) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ── IRC Code Dialog ───────────────────────────────────────────────────────
    if (showIrcDialog) {
        AlertDialog(
            onDismissRequest = { showIrcDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, null, tint = Gold, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("IRC Code Reference", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    Text(ircContent, fontSize = 13.sp, color = Color(0xFF374151), lineHeight = 19.sp)
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
