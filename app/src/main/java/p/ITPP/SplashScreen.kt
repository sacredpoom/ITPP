package p.ITPP

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import p.ITPP.ui.theme.*

@Composable
fun SplashScreen(navController: NavController, onThemeChange: () -> Unit) {
    val bangersFontFamily = FontFamily(
        Font(R.font.bangers_regular)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.secondary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "The Itinerant Traveller's Pocket Pokédex",
            fontSize = 48.sp,
            fontFamily = bangersFontFamily,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                lineHeight = 70.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 50.dp)
        )

        Spacer(modifier = Modifier.height(350.dp))

        Button(
            onClick = { navController.navigate("main") },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(), // Make the button wide
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            Text("Enter", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onThemeChange() },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            Text("Styles", fontSize = 20.sp, color = Color.White)
        }
    }
}