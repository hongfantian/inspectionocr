package com.inspect.ocr

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View


class CustomRectView : View {
    constructor( context:Context, rect: Rect):super( context ){
/*        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf( 0.0f, 0.0f, 20.0f, 0.0f, 20.0f, 20.0f, 0.0f, 20.0f )
        shape.setStroke(1, 0x000000FF )
        shape.setColor(0x000000FF);
        this.setBackground(shape)*/
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor( Color.WHITE)
        shape.setStroke(2, Color.BLACK)
        setBackground(shape)
    }
}