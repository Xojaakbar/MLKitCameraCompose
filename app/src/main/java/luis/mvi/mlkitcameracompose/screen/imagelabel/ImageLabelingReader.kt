package luis.mvi.mlkitcameracompose.screen.imagelabel

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.label.ImageLabel
import luis.mvi.mlkitcameracompose.components.CameraView
import luis.mvi.mlkitcameracompose.components.analizer.ImageLabelingAnalyzer
import luis.mvi.mlkitcameracompose.navigation.MyScreen

class ImageLabelingReader : MyScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val imageLabels = remember { mutableStateListOf<ImageLabel>() }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                analyzer = ImageLabelingAnalyzer {
                    imageLabels.clear()
                    imageLabels.addAll(it)
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
                        .height(150.dp),
                ) {
                    Text(
                        text = imageLabels.joinToString(separator = "\n") { it.text },
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }
    }
}