package luis.mvi.mlkitcameracompose.utils

import luis.mvi.mlkitcameracompose.navigation.MyScreen
import luis.mvi.mlkitcameracompose.screen.barcodereader.BarQrReader
import luis.mvi.mlkitcameracompose.screen.facedetector.FaceDetector
import luis.mvi.mlkitcameracompose.screen.facereader.FaceReader
import luis.mvi.mlkitcameracompose.screen.imagelabel.ImageLabelingReader
import luis.mvi.mlkitcameracompose.screen.objectreader.ObjectReader
import luis.mvi.mlkitcameracompose.screen.textreader.TextRecognize

 enum class Screens(val value:String, val screen:MyScreen){
    TEXT(value = "Text Recognize", screen = TextRecognize()),
    FACE("Face Mesh", FaceReader()),
    OBJECT("Object Reader", ObjectReader()),
    BARQR("QR Reader", BarQrReader()),
    IMAGELABELING("Image Labeling", ImageLabelingReader()),
    FACE2("Face Detector", FaceDetector()),
}