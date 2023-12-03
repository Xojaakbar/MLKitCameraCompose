package luis.mvi.mlkitcameracompose.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import luis.mvi.mlkitcameracompose.R
import luis.mvi.mlkitcameracompose.utils.GlobalState
import luis.mvi.mlkitcameracompose.utils.showLog
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun CameraView(
    context: Context,
    analyzer: ImageAnalysis.Analyzer,
    lifecycleOwner: LifecycleOwner
){
    fun getOutputDirectory(): File{
        val mediadir = context.externalMediaDirs.firstOrNull()?.let {
            File(it,context.resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return mediadir ?: context.filesDir
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val executor = ContextCompat.getMainExecutor(context)
    val cameraProvider = cameraProviderFuture.get()
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var changeSelector by remember { mutableStateOf(true) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val outputDirectory: File = getOutputDirectory()
    var shouldShowCamera by remember { mutableStateOf(false) }

    if (changeSelector){
        AndroidView(modifier = Modifier.fillMaxSize(),factory = {cntxt->
            val previewView = PreviewView(cntxt)
            cameraProviderFuture.addListener({
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply { setAnalyzer(cameraExecutor, analyzer) }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                    imageAnalysis
                )
            }, executor)
            preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
            previewView
        }
        )
    } else {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            factory = { cntxt ->
                val previewView = PreviewView(cntxt)
                cameraProviderFuture.addListener(
                    {
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .apply { setAnalyzer(cameraExecutor, analyzer) }

                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview,
                            imageCapture,
                            imageAnalysis
                        )
                    }, executor
                )
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )
    }

    fun handleImageCapture(uri: Uri) {
        Log.i("kilo", "Image captured: $uri")
        shouldShowCamera = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceAround) {
            IconButton(onClick = {  }) {
                Icon(painter = painterResource(id = R.drawable.video), contentDescription = null)
            }
            if (GlobalState.TAKE_PIC.collectAsState().value){
                takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = executor,
                    onImageCaptured = ::handleImageCapture,
                    onError = {
                        showLog(it.message.toString())
                    },
                    context
                )
            }
            IconButton(onClick = {
                takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = executor,
                    onImageCaptured = ::handleImageCapture,
                    onError = {
                        showLog(it.message.toString())
                    },
                    context
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                changeSelector = !changeSelector
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.change),
                    contentDescription = null
                )
            }
        }
    }
}

fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    context: Context
) {
    showLog("ISHLADI")
    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)

                // Galeriye ekleme işlemi
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = savedUri
                context.sendBroadcast(mediaScanIntent)

                onImageCaptured(savedUri)
            }
        }
    )
}

