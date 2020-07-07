package com.zero.viewutils.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.zero.viewutils.R
import com.zero.viewutils.utils.extends.getDimen
import com.zero.viewutils.utils.extends.listOf

/**
 * 侧边指示器
 */
class LetterView : View {


    private var alphabet = context.listOf(R.array.letterMenu)
    private var onLetterTouchedChangeListener: OnLetterTouchedChangeListener? = null

    // 画笔
    private val mPaint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    public fun setList(data:ArrayList<String>){
        alphabet = data
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 获得控件高度
        val viewHeight = height
        // 获得控件宽度
        val viewWidth = width
        // 控件高度除以索引字母个数得到每个索引字母的高度
        val heightPerAlphabet = viewHeight / alphabet.size

        //通过循环每个索引字母，并绘制出来
        for (i in alphabet.indices) {

            // 设置画笔颜色、画笔绘制文字粗细和大小，设置抗锯齿
            mPaint.color = ContextCompat.getColor(context, R.color.mainColor)
//            mPaint.typeface = Typeface.DEFAULT_BOLD
            // 索引字母绘制大小
            val alphabetTextSize = context.getDimen(R.dimen.sp_14)
            mPaint.textSize = alphabetTextSize
            mPaint.isAntiAlias = true

            // 索引字母的相对于控件的x坐标，此处算法结果为居中
            val xPos = viewWidth / 2 - mPaint.measureText(alphabet[i]) / 2
            // 索引字母的相对于控件的y坐标，索引字母的高度乘以索引字母下标+1即为y坐标
            val yPos = (heightPerAlphabet * i + heightPerAlphabet).toFloat()
            // 绘制索引字母
            canvas.drawText(alphabet[i], xPos, yPos, mPaint)
            // 重置画笔，为绘制下一个索引字母做准备
            mPaint.reset()

        }
    }

    fun setOnLetterTouchedChangeListener(onLetterTouchedChangeListener: OnLetterTouchedChangeListener) {
        this.onLetterTouchedChangeListener = onLetterTouchedChangeListener
    }

    interface OnLetterTouchedChangeListener {

        fun onTouchedLetterChange(letterTouched: String)

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        // 获得触摸后的动作
        val action = event.action
        // 获得触摸点的Y轴坐标
        val touchYPos = event.y

        // 控件高度除以索引字母的个数得到每个索引字母的高度（这里进行int强转），触摸点的Y轴坐标除以每个索引字母的高度就得到触摸到的索引字母的下标
        val currentTouchIndex = (touchYPos / height * alphabet.size).toInt()
        var currentChoosenAlphabetIndex = -1

        when (action) {
            // 当触摸的动作为按下或者按下移动时
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {

                currentChoosenAlphabetIndex = currentTouchIndex
                // 如果接口存在和索引下标值合法，执行接口方法，传入当前触摸的索引字母，供外部调用接收
                if (onLetterTouchedChangeListener != null && currentTouchIndex < alphabet.size && currentTouchIndex > -1) {
                    onLetterTouchedChangeListener!!.onTouchedLetterChange(alphabet[currentTouchIndex])
                }

                // 重新绘制控件，即重新执行onDraw函数
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

                setBackgroundResource(R.color.transport)
                currentChoosenAlphabetIndex = -1
                invalidate()
            }
            else -> {
            }
        }

        // 返回true表明该触摸事件处理完毕不分发出去
        return true
    }

}
