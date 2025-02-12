package p.ITPP

// Displays detailed pokemon information to user, returns to HomeScreen

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import p.ITPP.loadPokemonData
import p.ITPP.sanitizePokemonName
import p.ITPP.ui.theme.*
import p.ITPP.utils.capitalizeFirstLetter

@Composable
fun CustomTopAppBar(
    title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.secondary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Text(
            text = title.capitalizeFirstLetter(),
            color = colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun DetailsScreen(pokemonId: Int, navController: NavController) {
    val context = LocalContext.current
    val pokemonList = remember { loadPokemonData(context) } // Load Pokémon list
    val pokemon = pokemonList.find { it.id == pokemonId } // Find Pokémon by ID

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "${pokemon?.name ?: "Unknown"} Details") {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        pokemon?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorScheme.primary)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pokémon Image
                val spriteId = context.resources.getIdentifier(
                    sanitizePokemonName(it.name),
                    "drawable",
                    context.packageName
                )
                if (spriteId != 0) {
                    Image(
                        painter = painterResource(id = spriteId),
                        contentDescription = "${it.name} sprite",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(colorScheme.surfaceDim)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pokémon Information
                Text(text = "Species: ${it.species}", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                Text(text = "Type: ${it.types.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                Text(text = "Height: ${it.height / 10.0} m", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                Text(text = "Weight: ${it.weight / 10.0} kg", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                Text(text = "Base Experience: ${it.base_experience}", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)

                Spacer(modifier = Modifier.height(16.dp))

                // Abilities & Evolutions
                Text(text = "Abilities: ${it.abilities.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                it.previous_evolution?.let { prevEvo ->
                    Text(text = "Previous Evolution: $prevEvo", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                }
                it.next_evolution?.joinToString(", ")?.let { nextEvo ->
                    Text(text = "Next Evolution: $nextEvo", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats
                Text(text = "Base Stats:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = colorScheme.onSecondary)
                it.stats.forEach { (statName, statValue) ->
                    Text(text = "$statName: $statValue", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSecondary)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Play Cry Button
                Button(onClick = {
                    val mediaPlayer = MediaPlayer().apply {
                        val resourceId = context.resources.getIdentifier(it.cry_file, "raw", context.packageName)
                        if (resourceId != 0) {
                            setDataSource(context, Uri.parse("android.resource://${context.packageName}/$resourceId"))
                            prepare()
                            start()
                            Log.d("PokemonCry", "Playing cry: ${it.cry_file}.ogg")
                        } else {
                            Log.e("PokemonCry", "Cry file not found: ${it.cry_file}")
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
                ) {
                    Text("Play Cry", color = colorScheme.onPrimary)
                }
            }
        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Pokémon not found", style = MaterialTheme.typography.headlineMedium, color = Color.Red)
            }
        }
    }
}
