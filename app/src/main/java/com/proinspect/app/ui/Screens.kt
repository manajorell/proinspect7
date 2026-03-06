package com.proinspect.app.ui

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proinspect.app.data.*
import java.io.File

// ── Reports List ──────────────────────────────────────────────────────────────

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    "ProInspect",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = GoldLight,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                IconButton(
                    onClick = onSettings,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = GoldLight)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewReport,
                containerColor = Gold,
                contentColor = Navy
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Report")
            }
        }
    ) { padding ->
        if (reports.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏠", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("No inspections yet", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)
                    Text("Tap + to start a new report", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reports) { report ->
                    var showDelete by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(3.dp),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { onOpenReport(report.id) }
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Navy, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🏠", fontSize = 22.sp)
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    report.propertyAddress.ifBlank { "New Inspection" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Navy,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    report.clientName.ifBlank { "No client" },
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    report.inspectionDate,
                                    fontSize = 12.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                            IconButton(onClick = { showDelete = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                    if (showDelete) {
                        AlertDialog(
                            onDismissRequest = { showDelete = false },
                            title = { Text("Delete Inspection?") },
                            text = { Text("This will permanently delete the report and all photos.") },
                            confirmButton = {
                                TextButton(onClick = { onDeleteReport(report); showDelete = false }) {
                                    Text("Delete", color = Color.Red)
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

// ── Report Screen (tabs) ──────────────────────────────────────────────────────

@Composable
fun ReportScreen(
    report: Report?,
    currentTab: Int,
    onTabChange: (Int) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val tabs = listOf("Info","Roof","Exterior","Structure","Electrical","HVAC","Plumbing","Interior","Insulation","Garage","Summary")
    val tabIcons = listOf("📋","🏠","🏡","🏗","⚡","🌡","🚿","🛋","🌿","🚗","📊")

    Scaffold(
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Navy)
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = GoldLight)
                    }
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            report?.propertyAddress?.ifBlank { "New Inspection" } ?: "New Inspection",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            report?.inspectionDate ?: "",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
                ScrollableTabRow(
                    selectedTabIndex = currentTab,
                    containerColor = Color(0xFF1A2744),
                    contentColor = GoldLight,
                    edgePadding = 4.dp
                ) {
                    tabs.forEachIndexed { i, tab ->
                        Tab(
                            selected = currentTab == i,
                            onClick = { onTabChange(i) },
                            selectedContentColor = GoldLight,
                            unselectedContentColor = Color.White.copy(alpha = 0.5f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(tabIcons[i], fontSize = 16.sp)
                                Text(
                                    tab,
                                    fontSize = 10.sp,
                                    fontWeight = if (currentTab == i) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) { content() }
    }
}

// ── Settings Screen ───────────────────────────────────────────────────────────

@Composable
fun SettingsScreen(viewModel: InspectionViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val settings by viewModel.appSettings.collectAsState()

    val logoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.saveCompanyLogo(context, it) }
    }
    val badge1Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.saveBadge(context, it, 1) }
    }
    val badge2Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.saveBadge(context, it, 2) }
    }
    val badge3Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.saveBadge(context, it, 3) }
    }
    val badge4Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.saveBadge(context, it, 4) }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy)
                    .padding(horizontal = 8.dp, vertical = 14.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = GoldLight)
                }
                Text(
                    "Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GoldLight,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Company Logo
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Company Logo", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                        Text("Appears on the PDF cover page", fontSize = 13.sp, color = Color.Gray)
                        if (settings.companyLogoPath.isNotBlank() && File(settings.companyLogoPath).exists()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF5F5F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = File(settings.companyLogoPath),
                                    contentDescription = "Company Logo",
                                    modifier = Modifier.fillMaxSize().padding(8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { logoLauncher.launch("image/*") },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Replace") }
                                OutlinedButton(
                                    onClick = { viewModel.saveSettings(settings.copy(companyLogoPath = "")) },
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(1.dp, Color.Red)
                                ) { Text("Remove", color = Color.Red) }
                            }
                        } else {
                            Button(
                                onClick = { logoLauncher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Navy)
                            ) {
                                Icon(Icons.Default.Add, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Upload Company Logo")
                            }
                        }
                    }
                }
            }

            // Certification Badges
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Certification Badges", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                        Text("Displayed in the bottom third of your PDF report", fontSize = 13.sp, color = Color.Gray)
                        val badgePaths = listOf(
                            settings.badge1Path, settings.badge2Path,
                            settings.badge3Path, settings.badge4Path
                        )
                        val badgeLaunchers = listOf(badge1Launcher, badge2Launcher, badge3Launcher, badge4Launcher)
                        val badgeLabels = listOf("Badge 1", "Badge 2", "Badge 3", "Badge 4")
                        badgePaths.forEachIndexed { index, path ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF5F5F5))
                                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (path.isNotBlank() && File(path).exists()) {
                                        AsyncImage(
                                            model = File(path),
                                            contentDescription = badgeLabels[index],
                                            modifier = Modifier.fillMaxSize().padding(4.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        Text("🏅", fontSize = 28.sp)
                                    }
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(badgeLabels[index], fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    if (path.isNotBlank()) {
                                        Text("Tap to replace", fontSize = 12.sp, color = Color.Gray)
                                    } else {
                                        Text("Not set", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                if (path.isNotBlank()) {
                                    IconButton(onClick = { viewModel.clearBadge(index + 1) }) {
                                        Icon(Icons.Default.Delete, null, tint = Color(0xFFEF4444))
                                    }
                                }
                                IconButton(onClick = { badgeLaunchers[index].launch("image/*") }) {
                                    Icon(
                                        if (path.isNotBlank()) Icons.Default.Edit else Icons.Default.Add,
                                        null, tint = Navy
                                    )
                                }
                            }
                            if (index < 3) Divider(color = Color(0xFFEEEEEE))
                        }
                    }
                }
            }
        }
    }
}
