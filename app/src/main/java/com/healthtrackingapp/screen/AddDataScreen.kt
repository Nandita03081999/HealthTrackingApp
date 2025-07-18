import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.healthtrackingapp.data.HealthMetric
import com.healthtrackingapp.ui.theme.appBarColor
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen(navController: NavHostController, viewModel: HealthViewModel,editId: Int = -1) {
    val context = LocalContext.current
    var heartRate by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var waterIntake by remember { mutableStateOf("") }
    var editableType  by remember { mutableStateOf("") }

    LaunchedEffect(editId) {
        if (editId != -1) {
            val existing = viewModel.getMetricById(editId)
            existing?.let {
                when (it.type) {
                    "Heart Rate" -> heartRate = it.value.toString()
                    "Steps" -> steps = it.value.toString()
                    "Water Intake" -> waterIntake = it.value.toString()
                }
                editableType = it.type
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editId != -1) "Edit Data" else "Add Health Data", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    if (heartRate.isNotEmpty() && (editId == -1 || editableType == "Heart Rate")) {
                        viewModel.addMetric(
                            HealthMetric(
                                id = if (editId != -1 && editableType == "Heart Rate") editId else 0,
                                type = "Heart Rate",
                                value = heartRate.toFloat(),
                                timestamp = currentTime
                            )
                        )
                    }

                    if (steps.isNotEmpty() && (editId == -1 || editableType == "Steps")) {
                        viewModel.addMetric(
                            HealthMetric(
                                id = if (editId != -1 && editableType == "Steps") editId else 0,
                                type = "Steps",
                                value = steps.toFloat(),
                                timestamp = currentTime
                            )
                        )
                    }

                    if (waterIntake.isNotEmpty() && (editId == -1 || editableType == "Water Intake")) {
                        viewModel.addMetric(
                            HealthMetric(
                                id = if (editId != -1 && editableType == "Water Intake") editId else 0,
                                type = "Water Intake",
                                value = waterIntake.toFloat(),
                                timestamp = currentTime
                            )
                        )
                    }


                    navController.popBackStack()
                    Toast.makeText(context, if (editId == -1) "Data saved" else "Data updated", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = appBarColor),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(if (editId != -1) "Update" else "Submit", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = heartRate,
                onValueChange = { heartRate = it },
                label = { Text("Heart Rate (bpm)") },
                enabled = editableType.isEmpty() || editableType == "Heart Rate",
                leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text("Steps") },
                enabled = editableType.isEmpty() || editableType == "Steps",
                leadingIcon = { Icon(Icons.AutoMirrored.Rounded.DirectionsWalk, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            OutlinedTextField(
                value = waterIntake,
                onValueChange = { waterIntake = it },
                label = { Text("Water Intake (ml)") },
                enabled = editableType.isEmpty() || editableType == "Water Intake",
                leadingIcon = { Icon(Icons.Rounded.WaterDrop, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )
        }
    }


}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.Black,
    unfocusedBorderColor = Color.Black,
    cursorColor = Color.Black,
    focusedLabelColor = Color.Black,
    unfocusedLabelColor = Color.Black,
    focusedLeadingIconColor = Color.Black,
    unfocusedLeadingIconColor = Color.Black,
    disabledBorderColor = Color.Black,
    errorBorderColor = Color.Red,
    unfocusedTextColor = Color.Black,
    focusedTextColor = Color.Black
)

