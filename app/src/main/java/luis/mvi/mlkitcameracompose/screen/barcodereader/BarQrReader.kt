package luis.mvi.mlkitcameracompose.screen.barcodereader

import BarcodeScanningAnalyzer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.barcode.common.Barcode
import luis.mvi.mlkitcameracompose.components.CameraView
import luis.mvi.mlkitcameracompose.components.DrawBarcode
import luis.mvi.mlkitcameracompose.navigation.MyScreen

class BarQrReader : MyScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val detectedBarcode = remember { mutableStateListOf<Barcode>() }

        val screenWidth = remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
        val screenHeight = remember { mutableStateOf(context.resources.displayMetrics.heightPixels) }

        val imageWidth = remember { mutableStateOf(0) }
        val imageHeight = remember { mutableStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                analyzer = BarcodeScanningAnalyzer { barcodes, width, height ->
                    detectedBarcode.clear()
                    detectedBarcode.addAll(barcodes)
                    imageWidth.value = width
                    imageHeight.value = height
                }
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight()
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 10.dp),
                ) {
                    Text(
                        text = detectedBarcode.joinToString(separator = "\n") { it.displayValue.toString() },
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            DrawBarcode(
                barcodes = detectedBarcode,
                imageWidth = imageWidth.value,
                imageHeight = imageHeight.value,
                screenWidth = screenWidth.value,
                screenHeight = screenHeight.value
            )
        }
    }
}