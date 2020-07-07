package com.zero.viewutils.ui

import android.content.Context
import android.graphics.Canvas
import android.text.StaticLayout
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

/**
 * 两端对齐文本，需要一个VIEW做参照物
 */
class JustifyTextView:AppCompatTextView{


    private var mLinY: Int = 0
    private var mViewWidth = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        val paint = paint
        paint.color = currentTextColor
        paint.drawableState = drawableState
        val text = text as String
        mLinY = 0
        mLinY += textSize.toInt()
        val width = StaticLayout.getDesiredWidth(text, 0, text.length, getPaint())
        drawScaleText(canvas, text, width)
        val fm = paint.fontMetrics
        val textHeight = fm.top.toInt()
        mLinY += textHeight

    }

    private fun drawScaleText(canvas: Canvas, text: String, width: Float) {
        var x = 0f
        val d = (mViewWidth - width) / (text.length - 1)

        for (element in text) {
            val c = element.toString()
            val cw = StaticLayout.getDesiredWidth(c, paint)
            canvas.drawText(c, x, mLinY.toFloat(), paint)
            x += cw + d
        }
    }

    fun setGravity(tv: TextView) {
        val text = tv.text as String
        mViewWidth = StaticLayout.getDesiredWidth(text, 0, text.length, tv.paint)
        width = mViewWidth.toInt()
        invalidate()
    }
}