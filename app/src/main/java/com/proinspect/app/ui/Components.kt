package com.proinspect.app.ui

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.graphics.Color as AndroidColor
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.proinspect.app.data.DefectLibrary
import com.proinspect.app.data.*
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.graphics.Bitmap
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
    Box(
        modifier = modifier
            .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
            .border(
                1.5.dp,
                if (value.isNotBlank()) Gold.copy(alpha = 0.5f) else Color(0xFFE5E7EB),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                EditText(ctx).apply {
                    background = null
                    textSize = 14f
                    setTextColor(AndroidColor.parseColor("#1F2937"))
                    setHintTextColor(AndroidColor.parseColor("#9CA3AF"))
                    hint = placeholder
                    isSingleLine = singleLine
                    if (!singleLine) setLines(minLines)
                    setPadding(0, 8, 0, 8)
                    setText(value)
                    setSelection(value.length)
                    addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        override fun afterTextChanged(s: Editable?) {
                            val newText = s?.toString() ?: ""
                            if (newText != value) onValueChange(newText)
                        }
                    })
                }
            },
            update = { editText ->
                if (!editText.isFocused && editText.text.toString() != value) {
                    editText.setText(value)
                    editText.setSelection(value.length)
                }
            },
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
                        model = File(photo.filePath),
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
    onVoiceInput: (() -> Unit)? = null  // FIXED: Removed duplicate parameter
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Navy
            )
            
            // Voice input button (if callback provided)
            if (onVoiceInput != null) {
                IconButton(
                    onClick = onVoiceInput,
                    modifier = Modifier.size(32.dp)
                ) {
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
            value = value,
            onValueChange = onValueChange,
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
            } catch (e: Exception) {
                // Local OCR failed
            }
            
            if (localResult != null) {
                return@withContext "✅ Local Decode:\n$localResult"
            }
            
            if (apiKey.isBlank()) {
                return@withContext "⚠️ Local decode failed. API key needed for AI decode."
            }
            
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
            
            if (!resp.isSuccessful) {
                throw Exception("API Error ${resp.code}: $responseBody")
            }
            
            val respJson = org.json.JSONObject(responseBody)
            val content = respJson.getJSONArray("content")
            if (content.length() == 0) {
                throw Exception("No content in API response")
            }
            
            val text = content.getJSONObject(0).getString("text")
            return@withContext "🤖 AI Decode:\n$text"
            
        } catch (e: Exception) {
            return@withContext "❌ Error: ${e.localizedMessage ?: "Unknown error occurred"}"
        }
    }
}

fun parseSerialPlateText(text: String, equipmentName: String): String {
    val lines = text.lines().map { it.trim() }
    
    var manufacturer = ""
    var model = ""
    var serial = ""
    var year = ""
    var capacity = ""
    
    val mfgPatterns = listOf(
        "rheem", "ruud", "carrier", "trane", "lennox", "goodman", "amana", 
        "york", "american standard", "bryant", "payne", "bradford white",
        "a.o. smith", "ao smith", "state", "whirlpool", "ge", "frigidaire",
        "mitsubishi", "daikin", "fujitsu", "lg", "samsung", "coleman", "heil"
    )
    
    for (line in lines) {
        val lower = line.lowercase()
        val cleaned = lower
            .replace("0", "o")
            .replace("|", "i")
            .replace("1", "l")
        
        if (manufacturer.isEmpty()) {
            for (mfg in mfgPatterns) {
                if (cleaned.contains(mfg)) {
                    manufacturer = mfg.split(" ").joinToString(" ") { 
                        it.replaceFirstChar { c -> c.uppercase() } 
                    }
                    break
                }
            }
        }
        
        if (model.isEmpty() && (cleaned.contains("model") || cleaned.contains("mod") || cleaned.contains("m/n") || cleaned.contains("m.n"))) {
            model = line.replace(Regex("(?i)(model|mod|m/n|m\\.n)[:\\s]*"), "").trim()
        }
        
        if (serial.isEmpty() && (cleaned.contains("serial") || cleaned.contains("ser") || cleaned.contains("s/n") || cleaned.contains("s.n"))) {
            serial = line.replace(Regex("(?i)(serial|ser|s/n|s\\.n)[:\\s]*"), "").trim()
        }
        
        if (year.isEmpty()) {
            val yearMatch = Regex("\\b(19|20)\\d{2}\\b").find(line)
            if (yearMatch != null) {
                year = yearMatch.value
            } 
            else if (cleaned.contains("mfg") || cleaned.contains("mfr") || cleaned.contains("mer") || 
                     cleaned.contains("date") || cleaned.contains("manufactured") || cleaned.contains("manuf")) {
                val dateText = line.replace(Regex("(?i)(mfg|mfr|mer|date|manufactured|manuf)[:\\s]*"), "").trim()
                val yearInDate = Regex("(19|20)\\d{2}").find(dateText)
                if (yearInDate != null) {
                    year = yearInDate.value
                } else if (dateText.isNotBlank() && dateText.length <= 10) {
                    year = dateText
                }
            }
            else if (cleaned.contains("year") || cleaned.contains("yr")) {
                year = line.replace(Regex("(?i)(year|yr)[:\\s]*"), "").trim()
            }
        }
        
        if (capacity.isEmpty()) {
            val btuMatch = Regex("\\d+[,\\s]?\\d*\\s*(btu|btuh|8tu|biu|btlj|bti|8ti|btii)", RegexOption.IGNORE_CASE).find(line)
            val galMatch = Regex("\\d+\\s*(gal|gallon|gallons|ga1|gai)", RegexOption.IGNORE_CASE).find(line)
            val tonMatch = Regex("\\d+\\.?\\d*\\s*(ton|tons|t0n|t0ns)", RegexOption.IGNORE_CASE).find(line)
            val kwMatch = Regex("\\d+\\.?\\d*\\s*(kw|kilowatt|kw/h)", RegexOption.IGNORE_CASE).find(line)
            
            capacity = btuMatch?.value ?: galMatch?.value ?: tonMatch?.value ?: kwMatch?.value ?: ""
            
            if (capacity.isNotEmpty()) {
                capacity = capacity
                    .replace(Regex("\\s+"), " ")
                    .replace("8tu", "BTU", ignoreCase = true)
                    .replace("biu", "BTU", ignoreCase = true)
                    .replace("btlj", "BTU", ignoreCase = true)
                    .replace("8ti", "BTU", ignoreCase = true)
                    .replace("t0n", "ton", ignoreCase = true)
                    .replace("ga1", "gal", ignoreCase = true)
                    .trim()
            }
        }
    }
    
    if ((year.isEmpty() || year.length < 4 || year.matches(Regex(".*[a-zA-Z]{3,}.*"))) && serial.isNotEmpty()) {
        val decodedYear = decodeYearFromSerial(serial, manufacturer)
        if (decodedYear.isNotEmpty()) {
            year = decodedYear
        }
    }
    
    val foundCount = listOf(manufacturer, model, serial, year, capacity).count { it.isNotEmpty() }
    
    if (foundCount < 3) {
        return ""
    }
    
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
    
    val cleanSerial = serial.replace(Regex("[\\s-]"), "").uppercase()
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    
    return when (manufacturer.lowercase()) {
        "rheem", "ruud" -> {
            if (cleanSerial.length >= 6) {
                val yearDigits = cleanSerial.substring(4, 6)
                val year = yearDigits.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year <= 50) 2000 + year else 1900 + year
                    if (fullYear <= currentYear) "$fullYear (from serial)" else ""
                } else ""
            } else ""
        }
        "carrier", "bryant", "payne" -> {
            if (cleanSerial.length >= 4) {
                val yearChar = cleanSerial[3]
                val yearDigit = yearChar.toString().toIntOrNull()
                if (yearDigit != null) {
                    var year = 2010 + yearDigit
                    while (year > currentYear) {
                        year -= 10
                    }
                    "$year (from serial)"
                } else ""
            } else ""
        }
        "trane", "american standard" -> {
            if (cleanSerial.length >= 4) {
                val yearChars = cleanSerial.substring(2, 4)
                val year = yearChars.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year <= 50) 2000 + year else 1900 + year
                    if (fullYear <= currentYear) "$fullYear (from serial)" else ""
                } else ""
            } else ""
        }
        "lennox" -> {
            if (cleanSerial.length >= 3) {
                val yearDigits = cleanSerial.substring(1, 3)
                val year = yearDigits.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year <= 50) 2000 + year else 1900 + year
                    if (fullYear <= currentYear) "$fullYear (from serial)" else ""
                } else ""
            } else ""
        }
        "goodman", "amana" -> {
            if (cleanSerial.length >= 4) {
                val yearDigits = cleanSerial.substring(2, 4)
                val year = yearDigits.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year <= 50) 2000 + year else 1900 + year
                    if (fullYear <= currentYear) "$fullYear (from serial)" else ""
                } else ""
            } else ""
        }
        "york" -> {
            if (cleanSerial.isNotEmpty() && cleanSerial[0].isLetter()) {
                val letter = cleanSerial[0]
                val year = 2004 + (letter - 'A')
                if (year in 2004..currentYear) "$year (from serial)" else ""
            } else ""
        }
        "bradford white", "a.o. smith", "ao smith", "state" -> {
            val yearMatch = Regex("(19|20)\\d{2}").find(cleanSerial)
            if (yearMatch != null) {
                val year = yearMatch.value.toInt()
                if (year in 1980..currentYear) "${yearMatch.value} (from serial)" else ""
            } else ""
        }
        else -> {
            val yearMatch = Regex("(19|20)\\d{2}").find(cleanSerial)
            if (yearMatch != null) {
                val year = yearMatch.value.toInt()
                if (year in 1980..currentYear) "${yearMatch.value} (from serial)" else ""
            } else ""
        }
    }
}

@Composable
fun ChecklistItemCard(
    item: ChecklistItem,
    rating: Rating,
    narrative: String,
    photos: List<InspectionPhoto>,
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
    val hasDefects = DefectLibrary.getDefectsForItem(item.id).isNotEmpty()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                    item.title, fontSize = 13.sp,
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
                    placeholder = "Describe findings for: ${item.title}...",
                    onVoiceInput = onVoiceInput  // FIXED: Now properly passing the parameter
                )

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

                    val serialGalleryLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.GetContent()
                    ) { uri ->
                        uri?.let {
                            isDecoding = true
                            decodedResult = null
                            scope.launch {
                                val result = decodeSerialNumber(context, uri, equipmentName, apiKey)
                                decodedResult = result
                                isDecoding = false
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { serialGalleryLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Gold),
                        enabled = !isDecoding
                    ) {
                        if (isDecoding) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp), 
                                strokeWidth = 2.dp, 
                                color = Gold
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Decoding (trying local first)...", color = Gold, fontSize = 12.sp)
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
                            border = BorderStroke(
                                1.dp, 
                                when {
                                    result.startsWith("❌") -> Color(0xFFEF4444)
                                    result.startsWith("⚠️") -> Color(0xFFF59E0B)
                                    else -> Gold
                                }
                            )
                        ) {
                            Column(
                                Modifier.padding(12.dp), 
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    when {
                                        result.startsWith("✅") -> "📋 Decoded Locally"
                                        result.startsWith("🤖") -> "📋 Decoded via AI"
                                        result.startsWith("⚠️") -> "⚠️ Partial Result"
                                        else -> "❌ Decode Failed"
                                    },
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 13.sp, 
                                    color = Navy
                                )
                                Text(
                                    result, 
                                    fontSize = 12.sp, 
                                    color = Color(0xFF374151)
                                )
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
                                        ) {
                                            Text("Copy to Notes", fontSize = 12.sp)
                                        }
                                        OutlinedButton(
                                            onClick = { decodedResult = null },
                                            modifier = Modifier.weight(1f),
                                            border = BorderStroke(1.dp, Color(0xFF9CA3AF))
                                        ) {
                                            Text("Clear", fontSize = 12.sp, color = Color(0xFF6B7280))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
