package com.Derick.Ideagauge.View.Adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.view.View

class TutorialOverlay(context: Context) : View(context) {

    private var targetRect: RectF? = null
    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#CC008080")
    }
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun highlightView(view: View) {
        val location = IntArray(2)
        view.getLocationInWindow(location)

        targetRect = RectF(
            location[0].toFloat(),
            location[1].toFloat(),
            (location[0] + view.width).toFloat(),
            (location[1] + view.height).toFloat()
        )
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Save layer to draw over it
        val saved = canvas.saveLayer(null, null)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

        targetRect?.let {
            canvas.drawRoundRect(it, 24f, 24f, clearPaint)
        }

        canvas.restoreToCount(saved)
    }
}