import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.healthtrackingapp.data.HealthDatabase
import com.healthtrackingapp.screen.ChartScreen
import com.healthtrackingapp.screen.HomeScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val db = HealthDatabase.getDatabase(context)
    val repository = HealthRepository(db.metricDao())
    val viewModel = remember { HealthViewModel(repository) }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController,viewModel) }
        composable(
            route = "add?editId={editId}",
            arguments = listOf(
                navArgument("editId") {
                    type = NavType.IntType
                    defaultValue = -1 // -1 means "Add" mode
                }
            )
        ) { backStackEntry ->
            val editId = backStackEntry.arguments?.getInt("editId") ?: -1
            AddDataScreen(navController = navController, viewModel = viewModel, editId = editId)
        }
//        composable("add") {
//            AddDataScreen(
//                navController,
//                viewModel
//            )
//        }
        composable("view") { ViewDataScreen(navController,viewModel) }
        composable("chart") { ChartScreen(navController,viewModel) }
    }
}