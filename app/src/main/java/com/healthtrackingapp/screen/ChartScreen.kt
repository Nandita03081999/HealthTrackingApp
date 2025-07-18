package com.healthtrackingapp.screen

import HealthViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.healthtrackingapp.ui.theme.appBarColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(navController: NavHostController, viewModel: HealthViewModel) {
    val chartOptions = listOf("Heart Rate", "Steps", "Water Intake")
    var expanded by remember { mutableStateOf(false) }
    var selectedChartType by rememberSaveable { mutableStateOf(chartOptions.first()) }

    val filteredMetrics by viewModel.filteredMetrics.observeAsState(emptyList())
    val chartMetrics by viewModel.chartMetrics.collectAsState()

    // Apply filter when screen first opens
    LaunchedEffect(Unit) {
        viewModel.applyFilterForTimeRange("All") // or you can pass today's time range
    }

    // Trigger chart update when dropdown selection or filtered data changes
    LaunchedEffect(selectedChartType, filteredMetrics) {
        viewModel.updateChartMetrics(selectedChartType)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chart Health Data", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Dropdown selector
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select Metric:", modifier = Modifier.weight(1f), color = Color.Black)

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(selectedChartType)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        chartOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedChartType = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Line chart
            if (chartMetrics.isNotEmpty()) {
                Text(
                    text = "$selectedChartType Trend (Last 24 hrs)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                MetricLineChart(
                    data = chartMetrics,
                    label = selectedChartType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Text(
                    text = "No data available for $selectedChartType",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


