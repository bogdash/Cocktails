package com.bogdash.cocktails.presentation.detail.qrCode

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.bogdash.cocktails.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class QRCodeEncoder(context: Context) {
    private val qrColor = ContextCompat.getColor(context, R.color.black)
    private val transparent = ContextCompat.getColor(context, R.color.transparent)

    @Throws(WriterException::class)
    fun encodeAsBitmap(contentsToEncode: String?, size: Int): Bitmap? {
        if (contentsToEncode == null) {
            return null
        }
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(
                contentsToEncode,
                BarcodeFormat.QR_CODE,
                size,
                size,
                null
            )
        } catch (iae: IllegalArgumentException) {
            return null
        }

        val pixels = IntArray(size * size)
        for (y in 0 until size) {
            val offset = y * size
            for (x in 0 until size) {
                pixels[offset + x] = if (result[x, y]) qrColor else transparent
            }
        }

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
        return bitmap
    }
}