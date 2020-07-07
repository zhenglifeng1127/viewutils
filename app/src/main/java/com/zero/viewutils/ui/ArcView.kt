package com.zero.viewutils.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zero.viewutils.R

/**
 * 圆弧形背景VIEW
 */
class ArcView : View {

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mArcHeight: Int //圆弧的高度
    private var mBgColor: Int   //背景颜色
    private var lgColor: Int    //变化的最终颜色
    private var mPaint: Paint  //画笔
    private var rect = Rect(0, 0, 0, 0)//普通的矩形
    private var path = Path()//用来绘制曲面

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ArcView)
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcView_arcHeight, 0)
        mBgColor = typedArray.getColor(R.styleable.ArcView_bgColor, Color.parseColor("#303F9F"))
        lgColor = typedArray.getColor(R.styleable.ArcView_lgColor, mBgColor)
        mPaint = Paint()
        mPaint.isAntiAlias = true
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val linearGradient = LinearGradient(
            0f, 0f, measuredWidth.toFloat(), 0f,
            mBgColor, lgColor, Shader.TileMode.CLAMP
        )
        mPaint.shader = linearGradient
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //设置成填充
        mPaint.style = Paint.Style.FILL
        mPaint.color = mBgColor

        //绘制矩形
        rect.set(0, 0, mWidth, mHeight - mArcHeight)
        canvas.drawRect(rect, mPaint)

        //绘制路径
        path.moveTo(0f, (mHeight - mArcHeight).toFloat())
        path.quadTo(
            (mWidth shr 1).toFloat(),
            mHeight.toFloat(),
            mWidth.toFloat(),
            (mHeight - mArcHeight).toFloat()
        )
        canvas.drawPath(path, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        }
        setMeasuredDimension(mWidth, mHeight)
    }

}