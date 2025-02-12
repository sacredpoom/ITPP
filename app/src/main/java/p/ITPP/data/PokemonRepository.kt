package p.ITPP.data

// File for loading JSON Data into app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PokemonRepository {
    fun getPokemonList(context: Context): List<Pokemon> {
        val json = JsonLoader.loadJsonFromAssets(context, "pokemon_data.json")
        return if (json != null) {
            val listType = object : TypeToken<List<Pokemon>>() {}.type
            Gson().fromJson(json, listType)
        } else {
            emptyList()
        }
    }
}