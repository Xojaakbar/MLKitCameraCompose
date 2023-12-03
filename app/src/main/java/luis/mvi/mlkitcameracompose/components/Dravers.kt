package luis.mvi.mlkitcameracompose.components

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.facemesh.FaceMesh
import com.google.mlkit.vision.facemesh.FaceMeshPoint
import com.google.mlkit.vision.objects.DetectedObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawFaces(
    faces: List<FaceMesh>,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int
) {
    val textMeasure = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxSize()) {
        faces.forEach { face ->
            val boundingBox = face.boundingBox.toComposeRect()
            val topLeft = adjustPoint(
                PointF(boundingBox.topLeft.x, boundingBox.topLeft.y),
                imageWidth,
                imageHeight,
                screenWidth,
                screenHeight
            )
            val size =
                adjustSize(boundingBox.size, imageWidth, imageHeight, screenWidth, screenHeight)
            drawBounds(topLeft, size, Color.White, 8f)
            drawTxt(textMeasure, face)

            face.allPoints.forEach {
                val landmark = adjustPoint(
                    PointF(it.position.x, it.position.y),
                    imageWidth,
                    imageHeight,
                    screenWidth,
                    screenHeight
                )
                drawLandmark(landmark, Color.White, 8f)
            }

            face.allTriangles.forEach { triangle ->
                val points = triangle.allPoints.map {
                    adjustPoint(
                        PointF(it.position.x, it.position.y),
                        imageWidth,
                        imageHeight,
                        screenWidth,
                        screenHeight
                    )
                }
                drawTriangle(points, Color.Cyan, 5f)
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawFaces2(
    faces: List<Face>,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int,
    listenLookCamera: () -> Unit
) {
    val textMeasure = rememberTextMeasurer()
    var text: String
    Canvas(modifier = Modifier.fillMaxSize()) {
        faces.forEach { face ->
            val boundingBox = face.boundingBox.toComposeRect()
            val topLeft = adjustPoint(
                PointF(boundingBox.topLeft.x, boundingBox.topLeft.y),
                imageWidth,
                imageHeight,
                screenWidth,
                screenHeight
            )
            val size =
                adjustSize(boundingBox.size, imageWidth, imageHeight, screenWidth, screenHeight)
            val scope = CoroutineScope(Dispatchers.IO)
            drawBounds(topLeft, size, Color.White, 8f)

            face.allLandmarks.forEach {
                val landmark = adjustPoint(
                    PointF(it.position.x, it.position.y),
                    imageWidth,
                    imageHeight,
                    screenWidth,
                    screenHeight
                )
                drawLandmark(landmark, Color.White, 8f)
            }
            text = if (abs(face.headEulerAngleY) > 7 || abs(face.headEulerAngleX) > 10) {
                "KAMERAGA QARA..."
            } else {
                listenLookCamera.invoke()
                "YAXSHI..."
            }
            drawText(
                textMeasure,
                text,
                Offset(100f, 10f),
                TextStyle(
                    color = Color.White,
                    background = Color.Black,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            face.allContours.forEach { countour ->
                countour.points.forEach {
                    val landmark =
                        adjustPoint(it, imageWidth, imageHeight, screenWidth, screenHeight)
                    drawLandmark(landmark, Color.Red, 8f)
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawDetectedObjects(
    objects: List<DetectedObject>,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxSize()) {
        objects.forEach {
            val boundingBox = it.boundingBox.toComposeRect()
            val topLeft = adjustPoint(
                PointF(boundingBox.topLeft.x, boundingBox.topLeft.y),
                imageWidth,
                imageHeight,
                screenWidth,
                screenHeight
            )
            val size =
                adjustSize(boundingBox.size, imageWidth, imageHeight, screenWidth, screenHeight)
            drawText(
                textMeasurer,
                it.boundingBox.toShortString(),
                Offset(10f, 10f),
                TextStyle(
                    color = Color.Green,
                    fontSize = 24.sp,
                    FontWeight.Bold,
                    background = Color.Black
                )
            )
            drawBounds(topLeft, size, Color.Yellow, 10f)
        }
    }
}

@Composable
fun DrawBarcode(
    barcodes: List<Barcode>,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        barcodes.forEach { barcode ->
            barcode.boundingBox?.toComposeRect()?.let {
                val topLeft = adjustPoint(
                    PointF(it.topLeft.x, it.topLeft.y),
                    imageWidth,
                    imageHeight,
                    screenWidth,
                    screenHeight
                )
                val size = adjustSize(it.size, imageWidth, imageHeight, screenWidth, screenHeight)
                drawBounds(topLeft, size, Color.Yellow, 10f)
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawTxt(textMeasure: TextMeasurer, face: FaceMesh) {
    val text: String = if (calculateEyes(
            face.allPoints[145],
            face.allPoints[159]
        ) > 5 && calculateEyes(face.allPoints[386], face.allPoints[374]) > 5
    ) {
        "Ko'zingni yop "
    } else {
        "Ko'zingni och"
    }
    drawText(
        textMeasure,
        text,
        topLeft = Offset(10f, 10f),
        TextStyle.Default.copy(
            color = Color.Green,
            23.sp,
            FontWeight.Bold,
            background = Color.Black.copy(alpha = 0.8f)
        )
    )
}

fun calculateEyes(point1: FaceMeshPoint, point2: FaceMeshPoint): Double {
    return sqrt(
        ((point1.position.x - point2.position.x).toDouble()).pow(2.0) + ((point1.position.y - point2.position.y).toDouble()).pow(
            2.0
        )
    )
}

fun adjustPoint(
    point: PointF,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int
): PointF {
    val x = point.x / imageWidth * screenWidth
    val y = point.y / imageHeight * screenHeight
    return PointF(x, y)
}

fun adjustSize(
    size: Size,
    imageWidth: Int,
    imageHeight: Int,
    screenWidth: Int,
    screenHeight: Int
): Size {
    val width = size.width / imageWidth * screenWidth
    val height = size.height / imageHeight * screenHeight
    return Size(width, height)
}

fun DrawScope.drawLandmark(landmark: PointF, color: Color, radius: Float) {
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(landmark.x, landmark.y),
    )
}

fun DrawScope.drawBounds(topLeft: PointF, size: Size, color: Color, stroke: Float) {
    drawRect(
        color = color,
        size = size,
        topLeft = Offset(topLeft.x, topLeft.y),
        style = Stroke(width = stroke)
    )
}

fun DrawScope.drawTriangle(points: List<PointF>, color: Color, stroke: Float) {
    if (points.size < 3) return
    drawPath(
        path = Path().apply {
            moveTo(points[0].x, points[0].y)
            lineTo(points[1].x, points[1].y)
            lineTo(points[2].x, points[2].y)
            close()
        },
        style = Stroke(width = stroke),
        color = color
    )
}