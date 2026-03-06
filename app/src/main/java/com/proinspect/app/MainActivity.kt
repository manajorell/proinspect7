package com.proinspect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proinspect.app.data.InspectionSections
import com.proinspect.app.ui.*

class MainActivity : ComponentActivity() {
    private val viewModel: InspectionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ProInspectTheme { ProInspectApp(viewModel) } }
    }
}

@Composable
fun ProInspectApp(viewModel: InspectionViewModel) {
    val navController = rememberNavController()
    val reports by viewModel.allReports.collectAsState()
    var currentTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.navigateToReport.collect {
            currentTab = 0
            navController.navigate("report")
        }
    }

    NavHost(navController = navController, startDestination = "reports") {
        composable("reports") {
            ReportsListScreen(
                reports = reports,
                onNewReport = { viewModel.createNewReport() },
                onOpenReport = { id ->
                    viewModel.loadReport(id)
                    currentTab = 0
                    navController.navigate("report")
                },
                onDeleteReport = { viewModel.deleteReport(it) }
            )
        }
        composable("report") {
            val report by viewModel.currentReport.collectAsState()
            val tabSections = listOf("info") + InspectionSections.sections + listOf("summary")
            ReportScreen(
                report = report,
                currentTab = currentTab,
                onTabChange = { currentTab = it },
                onBack = { navController.popBackStack() }
            ) {
                when (tabSections.getOrNull(currentTab)) {
                    "info"    -> PropertyInfoScreen(viewModel)
                    "summary" -> SummaryScreen(viewModel)
                    null      -> PropertyInfoScreen(viewModel)
                    else      -> InspectionSectionScreen(
                        section = tabSections[currentTab],
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
