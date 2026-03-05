package com.proinspect.app.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proinspect.app.data.Report

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsListScreen(
    reports: List<Report>,
    onNewReport: () -> Unit,
    onOpenReport: (Long) -> Unit,
    onDeleteReport: (Report) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ProInspect", fontWeight = FontWeight.Bold, color = GoldLight)
                        Text("InterNACHI Standards", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewReport,
                containerColor = Gold,
                contentColor = Navy,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("New Inspection", fontWeight = FontWeight.Bold) }
            )
        },
        containerColor = Cream
    ) { padding ->
        if (reports.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("🏠", fontSize = 64.sp)
                    Text("No Inspections Yet", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Text("Tap + to start a new inspection report", color = Color(0xFF6B7280))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports) { report ->
                    ReportCard(report = report, onOpen = { onOpenReport(report.id) }, onDelete = { onDeleteReport(report) })
                }
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }
}

@Composable
fun ReportCard(report: Report, onOpen: () -> Unit, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onOpen() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(Navy, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center) {
                Text("🏠", fontSize = 22.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(report.propertyAddress.ifBlank { "New Inspection" }, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(report.clientName.ifBlank { "No client" }, fontSize = 13.sp, color = Color(0xFF6B7280))
                Text(report.reportNumber, fontSize = 11.sp, color = Gold, fontWeight = FontWeight.SemiBold)
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, null, tint = Color(0xFFEF4444))
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Report?") },
            text = { Text("This will permanently delete this inspection report.") },
            confirmButton = { TextButton(onClick = { onDelete(); showDeleteDialog = false }) { Text("Delete", color = RatingRed) } },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    report: Report?,
    currentTab: Int,
    onTabChange: (Int) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val tabs = listOf("Info","Roof","Exterior","Structure","Electrical","HVAC","Plumbing","Interior","Insulation","Garage","Summary")
    val tabIcons = listOf("📋","🏠","🧱","🏗","⚡","🌡","🔧","🪟","🌿","🚗","📊")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(report?.propertyAddress?.ifBlank { "New Inspection" } ?: "Inspection",
                                color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(report?.reportNumber ?: "", fontSize = 11.sp, color = GoldLight)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy)
                )
                ScrollableTabRow(
                    selectedTabIndex = currentTab,
                    containerColor = Color(0xFF243358),
                    contentColor = GoldLight,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab]),
                            color = Gold
                        )
                    }
                ) {
                    tabs.forEachIndexed { i, tab ->
                        Tab(
                            selected = currentTab == i,
                            onClick = { onTabChange(i) },
                            text = { Text("${tabIcons[i]} $tab", fontSize = 11.sp,
                                fontWeight = if (currentTab == i) FontWeight.Bold else FontWeight.Normal) },
                            selectedContentColor = GoldLight,
                            unselectedContentColor = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        },
        containerColor = Cream
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) { content() }
    }
}
