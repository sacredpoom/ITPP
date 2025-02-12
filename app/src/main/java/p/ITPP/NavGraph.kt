package p.ITPP

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(themeIndex: Int, onThemeChange: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController, onThemeChange) }
        composable("main") { MainScreen(navController) }
        // Pass the Pokemon ID as an argument
        composable("pokemon_details/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull() ?: 0
            DetailsScreen(pokemonId, navController)
        }
    }
}
