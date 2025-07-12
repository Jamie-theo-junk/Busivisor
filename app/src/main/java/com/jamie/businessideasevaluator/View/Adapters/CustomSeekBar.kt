package com.jamie.businessideasevaluator.View.Adapters

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar

class CustomSeekBar(context: Context, attrs: AttributeSet) : AppCompatSeekBar(context, attrs) {

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = 22f  // adjust size if needed
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val availableWidth = width - paddingStart - paddingEnd
        val thumbX = paddingStart + (progress / max.toFloat()) * availableWidth

        val thumbY = height / 2f

        val text = "$progress"
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val textHeight = bounds.height()

        val textY = thumbY + textHeight / 2f

        canvas.drawText(text, thumbX, textY, paint)
    }
}