package p.ITPP.data

// helper object for loading JSON from assets folder

import android.content.Context
import java.io.IOException

object JsonLoader {
    fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}