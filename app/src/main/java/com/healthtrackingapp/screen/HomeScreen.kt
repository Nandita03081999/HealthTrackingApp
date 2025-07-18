package com.healthtrackingapp.screen

import HealthViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.healthtrackingapp.ui.theme.appBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HealthViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("HealthMate", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add") },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true, // 🔥 Enables the cutout curve
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape, // 🔥 This creates the curve around FAB
                backgroundColor = Color.White,
                elevation = 8.dp
            ) {
                // Chart Button (left)
                TextButton(
                    onClick = { navController.navigate("chart") },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.ShowChart, contentDescription = "Chart", tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chart", color = Color.Black)
                }

                Spacer(modifier = Modifier.weight(1f)) // space around FAB

                // View Button (right)
                TextButton(
                    onClick = { navController.navigate("view") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View", color = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Visibility, contentDescription = "View", tint = Color.Black)
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome to HealthMate 👋",
                )
            }
        }
    )
}
