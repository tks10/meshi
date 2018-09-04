package com.takashi.meshi.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.math.ceil
import kotlin.math.max

class ImageConverter{
    companion object {
        fun convertToBase64(image: Bitmap, maxSize: Float = 256f): String{
            val resizeRate = max(image.width, image.height) / maxSize
            val resizedWidth = ceil(image.width / resizeRate).toInt()
            val resizedHeight = ceil(image.height / resizeRate).toInt()
            val resizedImage = Bitmap.createScaledBitmap(image, resizedWidth, resizedHeight, true)
            val bos = ByteArrayOutputStream()

            // The quality setting will be ignored because the format is lossless(PNG).
            resizedImage.compress(Bitmap.CompressFormat.PNG, 100, bos)
            return bos.toByteArray().let {
                Base64.encodeToString(it, Base64.DEFAULT).replace("\n", "")
            }
        }

        fun convertToBitmap(base64: String): Bitmap {
            val decodedBytes = Base64.decode(
                    base64.substring(base64.indexOf(",") + 1),
                    Base64.DEFAULT
            )

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}
