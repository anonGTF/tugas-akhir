package com.galih.makhrajulhuruf.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2"

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun String.toDate(pattern: String = "yyyy-MM-dd"): Date? =
    try {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun Int?.orZero() = this ?: 0

fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false

fun String.withImageUrl() = BASE_IMAGE_URL + this

fun String.trim(maxLength: Int) =
    if (this.length > maxLength)
        "${this.take(maxLength - 3)}..."
    else
        this

fun View.visible() = run { this.visibility = View.VISIBLE }

fun View.gone() = run { this.visibility = View.GONE }

fun ViewGroup.inflate(@LayoutRes layoutResId: Int): View = LayoutInflater.from(context)
    .inflate(layoutResId, this, false)

fun RecyclerView.removeItemDecorations() {
    while (this.itemDecorationCount > 0) {
        this.removeItemDecorationAt(0)
    }
}

fun Bitmap.toGrayscale(): Bitmap {
    val grayscaleBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(grayscaleBitmap)
    val paint = Paint()
    val matrix = ColorMatrix()
    matrix.setSaturation(0f) // Converts the image to grayscale
    val filter = ColorMatrixColorFilter(matrix)
    paint.colorFilter = filter
    canvas.drawBitmap(this, 0f, 0f, paint)
    return grayscaleBitmap
}

fun Bitmap.toBinary(threshold: Int = 128): Bitmap {
    val binaryBitmap = Bitmap.createBitmap(
        this.width,
        this.height,
        Bitmap.Config.ARGB_8888
    )
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            val pixel: Int = this.getPixel(x, y)
            val gray: Int = Color.red(pixel) // Assuming grayscale, so red channel is used
            val binaryColor: Int = if (gray < threshold) Color.BLACK else Color.WHITE
            binaryBitmap.setPixel(x, y, binaryColor)
        }
    }
    return binaryBitmap
}

fun Bitmap.toBinaryArray(): Array<BooleanArray> {
    val width = this.width
    val height = this.height
    val binaryArray = Array(height) { BooleanArray(width) }

    for (y in 0 until height) {
        for (x in 0 until width) {
            val pixel = this.getPixel(x, y)
            binaryArray[y][x] = pixel == Color.BLACK
        }
    }

    return binaryArray
}

fun Array<BooleanArray>.toBitmap(width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    for (y in 0 until height) {
        for (x in 0 until width) {
            bitmap.setPixel(x, y, if (this[y][x]) Color.BLACK else Color.WHITE)
        }
    }

    return bitmap
}

fun zhangSuenThinning(binaryArray: Array<BooleanArray>): Array<BooleanArray> {
    val width = binaryArray[0].size
    val height = binaryArray.size

    val resultArray = Array(height) { BooleanArray(width) }
    for (y in 0 until height) {
        for (x in 0 until width) {
            resultArray[y][x] = binaryArray[y][x]
        }
    }

    var hasChanged: Boolean
    val neighbors = arrayOfNulls<Boolean>(8)

    do {
        hasChanged = false

        // Step 1
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                if (resultArray[y][x]) {
                    getNeighbors(binaryArray, neighbors, x, y)

                    val p2 = neighbors[0] ?: false
                    val p3 = neighbors[1] ?: false
                    val p4 = neighbors[2] ?: false
                    val p5 = neighbors[3] ?: false
                    val p6 = neighbors[4] ?: false
                    val p7 = neighbors[5] ?: false
                    val p8 = neighbors[6] ?: false
                    val p9 = neighbors[7] ?: false

                    val A = (!p2 && (p3 || p4)).toInt() + (!p4 && (p5 || p6)).toInt() +
                            (!p6 && (p7 || p8)).toInt() + (!p8 && (p9 || p2)).toInt()
                    val B = p2.toInt() + p3.toInt() + p4.toInt() + p5.toInt() +
                            p6.toInt() + p7.toInt() + p8.toInt() + p9.toInt()

                    if (A == 1 && (B in 2..6)) {
                        if (!p2 || !p4 || !p6) {
                            resultArray[y][x] = false
                            hasChanged = true
                        }
                    }
                }
            }
        }

        // Step 2
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                if (resultArray[y][x]) {
                    getNeighbors(binaryArray, neighbors, x, y)

                    val p2 = neighbors[0] ?: false
                    val p3 = neighbors[1] ?: false
                    val p4 = neighbors[2] ?: false
                    val p5 = neighbors[3] ?: false
                    val p6 = neighbors[4] ?: false
                    val p7 = neighbors[5] ?: false
                    val p8 = neighbors[6] ?: false
                    val p9 = neighbors[7] ?: false

                    val A = (!p2 && (p3 || p4)).toInt() + (!p4 && (p5 || p6)).toInt() +
                            (!p6 && (p7 || p8)).toInt() + (!p8 && (p9 || p2)).toInt()
                    val B = p2.toInt() + p3.toInt() + p4.toInt() + p5.toInt() +
                            p6.toInt() + p7.toInt() + p8.toInt() + p9.toInt()

                    if (A == 1 && (B in 2..6)) {
                        if (!p2 || !p4 || !p8) {
                            resultArray[y][x] = false
                            hasChanged = true
                        }
                    }
                }
            }
        }
    } while (hasChanged)

    return resultArray
}

fun getNeighbors(binaryArray: Array<BooleanArray>, neighbors: Array<Boolean?>, x: Int, y: Int) {
    neighbors[0] = binaryArray[y - 1][x]
    neighbors[1] = binaryArray[y - 1][x + 1]
    neighbors[2] = binaryArray[y][x + 1]
    neighbors[3] = binaryArray[y + 1][x + 1]
    neighbors[4] = binaryArray[y + 1][x]
    neighbors[5] = binaryArray[y + 1][x - 1]
    neighbors[6] = binaryArray[y][x - 1]
    neighbors[7] = binaryArray[y - 1][x - 1]
}

