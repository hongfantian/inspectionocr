package com.inspect.ocr

import android.graphics.Bitmap
import android.graphics.Matrix

class ImageProcess {
    companion object{
        // Extension function to rotate a bitmap
        public fun rotate( bitmap:Bitmap, degree:Int): Bitmap {
            // Initialize a new matrix
            val matrix = Matrix()

            // Rotate the bitmap
            matrix.postRotate(degree.toFloat())

            // Resize the bitmap
/*            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                bitmap.width,
                bitmap.height,
                true
            )*/
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            // Create and return the rotated bitmap
/*            val afterBitmap:Bitmap =  Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )*/
/*
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle()
            }
            return afterBitmap
*/
        }
    }
}