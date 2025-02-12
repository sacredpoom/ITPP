package p.ITPP

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import p.ITPP.ui.theme.ITPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var themeIndex by rememberSaveable { mutableStateOf(0) } // Stores the selected theme

            ITPPTheme(themeIndex) { // Pass the theme index
                NavGraph(
                    themeIndex = themeIndex,
                    onThemeChange = { themeIndex = (themeIndex + 1) % 3 })
            }
        }
    }
}
