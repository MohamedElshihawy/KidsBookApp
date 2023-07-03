package com.example.book_v2.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.widget.AppCompatImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.book_v2.R
import com.example.book_v2.data.models.ColoredImageDetails
import com.example.book_v2.services.dpToPx
import kotlin.math.roundToInt

val TAG = "load image"
fun AppCompatImageView.loadImageIntoView(
    myUrl: String, cache: Boolean = true,
    errorImg: Int = R.drawable.error,
) {

    if (myUrl.lowercase().endsWith("svg")) {
        val imageLoader = ImageLoader.Builder(this.context)
            .components {
                add(SvgDecoder.Factory())
            }.build()

        val request = ImageRequest.Builder(this.context).apply {
            error(errorImg)
            placeholder(errorImg)
            data(myUrl)
        }.target(this).build()

        imageLoader.enqueue(request)
    } else {
        val imageLoader = ImageLoader(context)

        val request = ImageRequest.Builder(context).apply {
            if (cache) {
                memoryCachePolicy(CachePolicy.ENABLED)
            } else {
                memoryCachePolicy(CachePolicy.DISABLED)
            }
            error(errorImg)
            placeholder(errorImg)
            data(myUrl)
        }.target(this).build()

        imageLoader.enqueue(request)
    }

}


suspend fun uriToBitmap(
    context: Context,
    uri: String,
    width: Int = 0,
    height: Int = 0,
): Bitmap {
    val bitmap: Bitmap

    val imageLoader = if (uri.lowercase().endsWith("svg")) {
        ImageLoader.Builder(context = context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    } else {
        ImageLoader.Builder(context = context).build()
    }
    val request = ImageRequest.Builder(context)
        .data(uri)
        .allowHardware(false) // Disable hardware bitmaps.
        .build()

    val result = (imageLoader.execute(request) as SuccessResult).drawable
    bitmap = (result as BitmapDrawable).bitmap

    if (height > 0 && width > 0) {
        // return resizeBitmap(bitmap!!, width, height)
        return Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            true
        )
    }
    return bitmap
}


fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    val imageWidth = bitmap.width
    val imageHeight = bitmap.height
    val ratioBitmap = imageWidth.toFloat() / imageHeight.toFloat()
    val ratioMax = width.toFloat() / height.toFloat()
    var finalWidth = width
    var finalHeight = height
    if (ratioMax > 1) {
        finalWidth = (height.toFloat() * ratioBitmap).roundToInt()
    } else {
        finalHeight = (width.toFloat() / ratioBitmap).roundToInt()
    }
    return Bitmap.createScaledBitmap(
        bitmap,
        finalWidth.dpToPx,
        finalHeight.dpToPx,
        true
    )
}


suspend fun calculateImageAccuracy(image1Path: String, image2Path: String): ColoredImageDetails {
    var matchingPixels = 0

    // suppose this is the image colored by the user
    val image1 = BitmapFactory.decodeFile(image1Path)
    val image2 = BitmapFactory.decodeFile(image2Path)
    val image1Pixels = IntArray(image1.width * image1.height)
    val image2Pixels = IntArray(image2.width * image2.height)
    image1.getPixels(image1Pixels, 0, image1.width, 0, 0, image1.width, image1.height)
    image2.getPixels(image2Pixels, 0, image2.width, 0, 0, image2.width, image2.height)

    image1Pixels.forEachIndexed { index, i ->
        if (i == image2Pixels[index]) {
            matchingPixels++
        } else {
            image1Pixels[index] = Color.RED
        }
    }
    val finalImage = image1.copy(image1.config, true)
    finalImage.setPixels(image1Pixels, 0, image1.width, 0, 0, image1.width, image1.height)
    val matchingPercentage = (matchingPixels.toFloat().div(image1Pixels.size.toFloat()))
    val similarity = (matchingPercentage * 100)

    return ColoredImageDetails(finalImage, similarity)
}

