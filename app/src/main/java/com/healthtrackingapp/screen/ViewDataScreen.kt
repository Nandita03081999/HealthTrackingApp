import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.healthtrackingapp.screen.MetricLineChart
import com.healthtrackingapp.ui.theme.appBarColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewDataScreen(navController: NavHostController, viewModel: HealthViewModel) {
    val allMetrics by viewModel.filteredMetrics.observeAsState(emptyList())
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Morning", "Afternoon", "Evening")
    val isLoading by viewModel.isLoading.observeAsState(false)
    var isAscending by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.applyFilterForTimeRange("All")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "View Health Data",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor
                )
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
            // Heading
            Text(
                text = "Filter by Time of Day",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(Modifier.height(12.dp))

            // Filter Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Button(
                        onClick = {
                            selectedFilter = filter
                            viewModel.applyFilterForTimeRange(filter)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) appBarColor else Color.LightGray,
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(filter)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text("Sort by Value: ", color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))
                IconButton(onClick = { isAscending = !isAscending }) {
                    Icon(
                        imageVector = if (isAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = "Toggle Sort Order",
                        tint = appBarColor
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> {
                    // Show loader while fetching data
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = appBarColor)
                    }
                }

                allMetrics.isEmpty() -> {
                    // Show empty state if no data
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No data available",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                            color = Color.Gray
                        )
                    }
                }

                else -> {
                    val sortedMetrics = if (isAscending) {
                        allMetrics.sortedBy { it.value }
                    } else {
                        allMetrics.sortedByDescending { it.value }
                    }
                    // Show data list
                    LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                        items(sortedMetrics) { metric ->
                            val icon = when (metric.type) {
                                "Heart Rate" -> Icons.Default.Favorite
                                "Steps" -> Icons.AutoMirrored.Rounded.DirectionsWalk
                                "Water Intake" -> Icons.Rounded.WaterDrop
                                else -> Icons.Default.Info
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = appBarColor,
                                        modifier = Modifier.size(32.dp)
                                    )

                                    Column {
                                        Text(
                                            text = metric.type,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color.Black
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row {
                                            Text(
                                                text = "Value",
                                                modifier = Modifier.width(50.dp),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp,
                                                    color = Color.Black
                                                )
                                            )
                                            Text(
                                                text = ": ${metric.value}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp,
                                                    color = Color.Black
                                                )
                                            )
                                        }

                                        Row {
                                            Text(
                                                text = "Time",
                                                modifier = Modifier.width(50.dp),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp,
                                                    color = Color.Black
                                                )
                                            )
                                            Text(
                                                text = ": ${formatTimestamp(metric.timestamp)}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp,
                                                    color = Color.Black
                                                )
                                            )
                                        }
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        IconButton(onClick = {
                                            navController.navigate("add?editId=${metric.id}")
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
                                        }

                                        IconButton(onClick = {
                                            viewModel.deleteMetric(metric)
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
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

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
