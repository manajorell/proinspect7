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

@Composable
// Inside your @Composable function
val scope = rememberCoroutineScope()
val context = LocalContext.current

val serialGalleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    uri?.let { selectedUri ->
        scope.launch {
            isDecoding = true
            try {
                val result = withContext(Dispatchers.IO) {
                    // 1. Get bytes safely
                    val bytes = context.contentResolver.openInputStream(selectedUri)?.use { it.readBytes() }
                        ?: throw Exception("File not found")
                    val base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)

                    // 2. Build Body (ensure model and max_tokens are present)
                    val jsonBody = JSONObject().apply {
                        put("model", "claude-3-5-sonnet-20241022")
                        put("max_tokens", 1024)
                        put("messages", JSONArray().put(JSONObject().apply {
                            put("role", "user")
                            put("content", JSONArray()
                                .put(JSONObject().apply {
                                    put("type", "image")
                                    put("source", JSONObject().apply {
                                        put("type", "base64")
                                        put("media_type", "image/jpeg")
                                        put("data", base64Image)
                                    })
                                })
                                .put(JSONObject().apply {
                                    put("type", "text")
                                    put("text", "Extract the serial number from this image.")
                                })
                            )
                        }))
                    }

                    val body = jsonBody.toString().toRequestBody("application/json".toMediaType())
val request = okhttp3.Request.Builder()
    .url("https://api.anthropic.com/v1/messages") // FIX: Full endpoint path
    .addHeader("x-api-key", "YOUR_ACTUAL_API_KEY") // FIX: Your real key
    .addHeader("anthropic-version", "2023-06-01")
    .post(body)
    .build()

client.newCall(request).execute().use { resp ->
    val bodyString = resp.body?.string() ?: ""
    if (!resp.isSuccessful) {
        // Detailed error for debugging (e.g., 400 Bad Request if JSON is malformed)
        throw Exception("API Error ${resp.code}: $bodyString")
    }
    
    val respJson = JSONObject(bodyString)
    // Extraction from Anthropic's standard message response structure
    respJson.getJSONArray("content").getJSONObject(0).getString("text")
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
    label: String = "📝 Inspector Narrative",
    placeholder: String = "Add narrative notes...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFDF9F2))
            .border(1.5.dp, Gold, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            color = Gold, modifier = Modifier.padding(bottom = 6.dp)
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
    apiKey: String = "",
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

                // The "Templates" label stays INSIDE the Row
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

                // The IconButton stays INSIDE the Row
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
        } 
    } 
} 

            val serialGalleryLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.GetContent()
) { uri ->
    uri?.let {
        isDecoding = true
        scope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@launch
                inputStream.close()
                val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                
                val result = kotlinx.coroutines.withContext(Dispatchers.IO) {
                    val client = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                        .build()
                        
                    val json = org.json.JSONObject().apply {
                        put("model", "claude-opus-4-5-20251101")
                        put("max_tokens", 300)
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
                                        put("text", "This is a serial number plate from a $equipmentName. Extract details...")
                                    })
                                }) // End Content Array
                            } // End Message Object
                        )) // End Messages Array
                    } // End Root JSON Object
                    
                    // TODO: Add your network request call here (e.g., client.newCall(request).execute())
                    "Success" 
                }
                decodedResult = result
            } catch (e: Exception) {
                decodedResult = "Error: ${e.localizedMessage}"
            } finally {
                isDecoding = false
            }
        }
    }
}

                                        // 1. Build the correct JSON body
val json = JSONObject().apply {
    put("model", "claude-3-5-sonnet-20241022")
    put("max_tokens", 1024)
    put("messages", JSONArray().put(JSONObject().apply {
        put("role", "user")
        put("content", JSONArray().apply {
            put(JSONObject().apply {
                put("type", "image")
                put("source", JSONObject().apply {
                    put("type", "base64")
                    put("media_type", "image/jpeg") // or image/png
                    put("data", base64ImageData) // Your base64 string here
                })
            })
            put(JSONObject().apply {
                put("type", "text")
                put("text", "Extract the serial number from this image.")
            })
        })
    }))
}

// 2. Execute on IO thread
val result = withContext(Dispatchers.IO) {
    val body = json.toString().toRequestBody("application/json".toMediaType())
    val request = okhttp3.Request.Builder()
        .url("https://api.anthropic.com/v1/messages")
        .addHeader("x-api-key", "YOUR_API_KEY")
        .addHeader("anthropic-version", "2023-06-01")
        .post(body)
        .build()

    client.newCall(request).execute().use { resp ->
        if (!resp.isSuccessful) throw Exception("HTTP ${resp.code}: ${resp.message}")
        val respJson = JSONObject(resp.body!!.string())
        respJson.getJSONArray("content").getJSONObject(0).getString("text")
    }
}

val context = LocalContext.current
val scope = rememberCoroutineScope()

val serialGalleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    uri?.let {
        scope.launch {
            isDecoding = true
            try {
                val result = withContext(Dispatchers.IO) {
                    // 1. Convert Image to Base64 (Required for vision)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes() ?: throw Exception("Failed to read image")
                    val base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)

                    // 2. Build the correct Anthropic JSON structure
                    val json = org.json.JSONObject().apply {
                        put("model", "claude-3-5-sonnet-20241022")
                        put("max_tokens", 1024)
                        put("messages", org.json.JSONArray().put(org.json.JSONObject().apply {
                            put("role", "user")
                            put("content", org.json.JSONArray().put(org.json.JSONObject().apply {
                                put("type", "image")
                                put("source", org.json.JSONObject().apply {
                                    put("type", "base64")
                                    put("media_type", "image/jpeg")
                                    put("data", base64Image)
                                })
                            }).put(org.json.JSONObject().apply {
                                put("type", "text")
                                put("text", "Extract the serial number from this image.")
                            }))
                        }))
                    }

                    // 3. The OkHttp call
                    val body = json.toString().toRequestBody("application/json".toMediaType())
                    val request = okhttp3.Request.Builder()
                        .url("https://api.anthropic.com")
                        .addHeader("x-api-key", "YOUR_API_KEY") // Replace with actual key
                        .addHeader("anthropic-version", "2023-06-01")
                        .post(body)
                        .build()

                    client.newCall(request).execute().use { resp ->
                        if (!resp.isSuccessful) throw Exception("Error: ${resp.code}")
                        val respJson = org.json.JSONObject(resp.body!!.string())
                        respJson.getJSONArray("content").getJSONObject(0).getString("text")
                    }
                }
                decodedResult = result
            } catch (e: Exception) {
                decodedResult = "Error: ${e.localizedMessage}"
            } finally {
                isDecoding = false
            }
        }
    }
}
