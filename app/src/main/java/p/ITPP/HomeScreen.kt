package p.ITPP

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import p.ITPP.data.Pokemon
import p.ITPP.utils.capitalizeFirstLetter
import java.io.IOException

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val pokemonList = remember { loadPokemonData(context) }  // Load data from JSON

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.primary).padding(16.dp)) {
        Text(text = "Pokédex", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(pokemonList) { pokemon ->
                PokemonItem(pokemon, context, navController)
            }
        }
    }
}

// Function to load Pokémon data from assets
fun loadPokemonData(context: Context): List<Pokemon> {
    return try {
        val json = context.assets.open("pokemon_data.json").bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<Pokemon>>() {}.type
        Gson().fromJson(json, listType)
    } catch (e: IOException) {
        emptyList() // Return empty list on failure
    }
}

// fix for sprite names with hyphens
fun sanitizePokemonName(name: String): String {
    return when (name.lowercase()) {
        "mr. mime" -> "mr_mime"
        "nidoran♀" -> "nidoran_f"
        "nidoran♂" -> "nidoran_m"
        else -> name.lowercase().replace("-", "_").replace(" ", "_")
    }
}

@Composable
fun PokemonItem(pokemon: Pokemon, context: Context, navController: NavController) {
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)) {
                Text(
                    text = "${pokemon.id}. ${pokemon.name.capitalizeFirstLetter()}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onPrimary,
                )
                Text(text = "Type: ${pokemon.types.joinToString(", ")}",color = colorScheme.onPrimary,)


                Row {
                    Button(onClick = {
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer().apply {
                            val resourceId = context.resources.getIdentifier(
                                pokemon.cry_file,
                                "raw",
                                context.packageName
                            )
                            if (resourceId != 0) {
                                setDataSource(
                                    context,
                                    Uri.parse("android.resource://${context.packageName}/$resourceId")
                                )
                                prepare()
                                start()
                                Log.d("PokemonCry", "Playing cry: ${pokemon.cry_file}.ogg")
                            } else {
                                Log.e("PokemonCry", "Cry file not found: ${pokemon.cry_file}")
                            }
                        }
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.onPrimaryContainer),
                        modifier = Modifier.widthIn(40.dp)
                    ) {
                        Text("Cry", color = colorScheme.onPrimary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        navController.navigate("pokemon_details/${pokemon.id}")
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.onPrimaryContainer),
                        modifier = Modifier.widthIn(80.dp)
                    ) {
                        Text("Details", color = colorScheme.onPrimary)
                    }
                }
            }

            val spriteId = context.resources.getIdentifier(sanitizePokemonName(pokemon.name), "drawable", context.packageName)
            if (spriteId != 0) {
                Image(
                    painter = painterResource(id = spriteId),
                    contentDescription = "${pokemon.name} sprite",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.surfaceDim)
                )
            }
        }
    }
}
