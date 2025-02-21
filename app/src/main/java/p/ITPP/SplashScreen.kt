package p.ITPP

import android.graphics.Bitmap
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import kotlin.random.Random
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import java.io.InputStream
import android.graphics.BitmapFactory
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.imageResource

@Composable
fun SplashScreen(navController: NavController, onThemeChange: () -> Unit) {
    val bangersFontFamily = FontFamily(
        Font(R.font.bangers_regular)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        AnimatedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
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
}

@Composable
fun AnimatedBackground() {
    val spacing = 100f // Distance between circles

    val infiniteTransition = rememberInfiniteTransition()

    // Animate movement in both X and Y directions
    val animatedOffset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = spacing,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Load pokeball image
    val pokeballImage: ImageBitmap = loadImageFromAssets("pokeball.png")
    val pokeballSize = 50f
    val scaledBitmap = Bitmap.createScaledBitmap(pokeballImage.asAndroidBitmap(), pokeballSize.toInt(), pokeballSize.toInt(), false)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val screenWidth = size.width
        val screenHeight = size.height

        // Dynamically calculate required number of columns and rows
        val cols = (screenWidth / spacing).toInt() + 1
        val rows = (screenHeight / spacing).toInt() + 1

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = (col * spacing + animatedOffset.value) % (screenWidth + spacing)
                val y = (row * spacing + animatedOffset.value) % (screenHeight + spacing)

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawBitmap(scaledBitmap, x, y, null)
                }
            }
        }
    }
}

@Composable
fun loadImageFromAssets(fileName: String): ImageBitmap {
    val context = LocalContext.current
    val assetManager = context.assets
    val inputStream: InputStream = assetManager.open(fileName)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    // Wrap the ImageBitmap inside a BitmapPainter, which implements Painter
    return bitmap.asImageBitmap()
}