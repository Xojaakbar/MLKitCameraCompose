package luis.mvi.mlkitcameracompose.screen.facereader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.mlkit.vision.facemesh.FaceMesh
import luis.mvi.mlkitcameracompose.components.CameraView
import luis.mvi.mlkitcameracompose.navigation.MyScreen
import luis.mvi.mlkitcameracompose.components.DrawFaces
import luis.mvi.mlkitcameracompose.components.analizer.FaceMeshDetectionAnalyzer

class FaceReader : MyScreen() {
    @Composable
    override fun Content() {
        FaceDetector()
    }
    @Composable
    fun FaceDetector(){
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val faces = remember { mutableStateListOf<FaceMesh>() }

        val screenWidth = remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
        val screenHeight = remember { mutableStateOf(context.resources.displayMetrics.heightPixels) }

        val imageWidth = remember { mutableStateOf(0) }
        val imageHeight = remember { mutableStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                analyzer = FaceMeshDetectionAnalyzer { meshes, width, height ->
                    faces.clear()
                    faces.addAll(meshes)
                    imageWidth.value = width
                    imageHeight.value = height
                }
            )
            DrawFaces(faces = faces, imageHeight.value, imageWidth.value, screenWidth.value, screenHeight.value)
        }
    }



}