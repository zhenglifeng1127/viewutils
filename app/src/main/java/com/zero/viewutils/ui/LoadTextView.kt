package com.zero.viewutils.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.zero.viewutils.R
import com.zero.viewutils.utils.extends.getDimenPx

/**
 * 带loading的textview
 */
class LoadTextView : AppCompatTextView {

    var isLoad = false

    private val mProgressDrawable by lazy {
        CircularProgressDrawable(context)
    }

    private val bounds by lazy { mProgressDrawable.copyBounds() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    init {
        mProgressDrawable.setColorSchemeColors(textColors.defaultColor)
        mProgressDrawable.setBounds(0, 0, context.getDimenPx(R.dimen.dp_14), context.getDimenPx(R.dimen.dp_14))
        setCompoundDrawables(mProgressDrawable, null, null, null)
        mProgressDrawable.strokeWidth = 4f
    }

    fun start() {
        isLoad = true
        mProgressDrawable.start()
    }

    fun stop() {
        isLoad = false
        mProgressDrawable.stop()
    }

    private fun calcOffsetX(): Float {
        //getCompoundPaddingStart()  = paddingStart + drawableWidth + drawablePadding
        return (width - (compoundPaddingStart + getTextWidth())) / 2
    }

    //计算文字的长度
    private fun getTextWidth(): Float {
        return paint.measureText(text.toString())
    }

    override fun onDraw(canvas: Canvas) {
        val offsetX = calcOffsetX().toInt()
        mProgressDrawable.setBounds(offsetX, bounds.top, bounds.right + offsetX, bounds.bottom)
        val tranX: Int = (bounds.width() + compoundDrawablePadding) / 2
        canvas.translate(-tranX.toFloat(), 0f)
        super.onDraw(canvas)
    }
}