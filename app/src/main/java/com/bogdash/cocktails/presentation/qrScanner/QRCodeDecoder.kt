package com.bogdash.cocktails.presentation.qrScanner

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.bogdash.cocktails.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

/**
 * QRCodeEncoder is responsible for encoding a given string into a QR code bitmap.
 *
 * @param context the context which the encoder is running
 */
class QRCodeEncoder(context: Context) {
    private val qrColor = ContextCompat.getColor(context, R.color.black)
    private val transparent = ContextCompat.getColor(context, R.color.white)

    /**
     * Encodes the specified content into a QR code bitmap.
     *
     * @param contentsToEncode the content to encode into the QR code
     * @param size the width and height of the resulting QR code bitmap
     * @return the generated QR code bitmap, or null if encoding fails
     * @throws WriterException if an error occurs during encoding
     */
    @Throws(WriterException::class)
    fun encodeAsBitmap(contentsToEncode: String, size: Int): Bitmap {
        val result: BitMatrix = MultiFormatWriter().encode(
            contentsToEncode,
            BarcodeFormat.QR_CODE,
            size,
            size,
            null
        )

        val pixels = IntArray(size * size) { index ->
            val x = index % size
            val y = index / size
            if (result[x, y]) qrColor else transparent
        }

        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, size, 0, 0, size, size)
        }
    }
}
