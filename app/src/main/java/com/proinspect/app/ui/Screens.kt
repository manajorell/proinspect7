package com.proinspect.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.proinspect.app.data.InspectionSections
import com.proinspect.app.data.Report

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    report: Report?,
    currentTab: Int,
    onTabChange: (Int) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val tabSections = listOf("info") + InspectionSections.sections + listOf("summary")
    val tabLabels = listOf("Info") +
        InspectionSections.sections.map {
            InspectionSections.sectionNames[it] ?: it
        } + listOf("Summary")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        report?.propertyAddress?.ifBlank { "New Inspection" } ?: "Inspection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy)
            )
        },
        bottomBar = {
            ScrollableTabRow(
                selectedTabIndex = currentTab,
                containerColor = Navy,
                contentColor = Gold,
                edgePadding = 0.dp
            ) {
                tabLabels.forEachIndexed { index, label ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { onTabChange(index) },
                        text = {
                            Text(
                                label,
                                fontSize = 11.sp,
                                color = if (currentTab == index) Gold else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            content()
        }
    }
}
