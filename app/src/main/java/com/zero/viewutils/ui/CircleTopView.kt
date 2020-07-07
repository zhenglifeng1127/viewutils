package com.zero.viewutils.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.zero.viewutils.R

/**
 * 圆形进度指示器
 */
class CircleTopView : View {

    private var paint: Paint? = null
    private var tag = 0
    private var menu: Array<String>? = null
    private var radius: Float = 0.toFloat()
    private var line: Float = 0.toFloat()
    private var lineDp2: Float = 0.toFloat()


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleTopView)
        tag = array.getInteger(R.styleable.CircleTopView_circleTag, 0)
        val menuText = array.getString(R.styleable.CircleTopView_circleMenu)
        if (!menuText.isNullOrEmpty()) {
            if (menuText.contains(",")) {
                menu = menuText.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
        }
        array.recycle()
        init()
    }

    internal fun init() {
        paint = Paint()
        paint?.let {
            it.color = ContextCompat.getColor(context, R.color.colorPrimary)
            it.style = Paint.Style.FILL
            it.isAntiAlias = true
            it.textSize = context.resources.getDimension(R.dimen.sp_12)
        }
        radius = context.resources.getDimension(R.dimen.dp_8)
        line = context.resources.getDimension(R.dimen.dp_12)
        lineDp2 = context.resources.getDimension(R.dimen.dp_2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        menu?.let { list ->
            for (i in list.indices) {
                paint?.let {
                    if (i > tag) {
                        it.color = ContextCompat.getColor(context, R.color.colorStatusBlue)
                    } else {
                        it.color = ContextCompat.getColor(context, R.color.colorPrimary)
                    }
                    canvas.drawCircle(
                        ((2 * i + 1) * width / (list.size * 2)).toFloat(),
                        (height / 3).toFloat(),
                        radius,
                        it
                    )
                    if (i + 1 != menu!!.size) {
                        it.strokeWidth = lineDp2
                        canvas.drawLine(
                            ((2 * i + 1) * width / (list.size * 2)).toFloat() + line,
                            (height / 3).toFloat(),
                            ((2 * i + 3) * width / (list.size * 2)).toFloat() - line,
                            (height / 3).toFloat(),
                            it
                        )
                    }
                    it.color = ContextCompat.getColor(context, R.color.tc1)
                    val textWidth = it.measureText(list[i])
                    canvas.drawText(
                        list[i],
                        ((2 * i + 1) * width / (list.size * 2)).toFloat() - textWidth / 2,
                        (height * 7 / 10).toFloat(),
                        it
                    )
                    it.color = ContextCompat.getColor(context, R.color.colorPrimary)
                }
            }
        }


    }

    fun setMenu(menu: Array<String>?) {
        if (menu == null)
            return
        this.menu = menu
        if (tag > menu.size) {
            tag = menu.size
        }
        postInvalidate()
    }


    fun setTag(tag: Int) {
        if (menu == null)
            return
        if (tag <= menu!!.size) {
            this.tag = tag

        } else {
            this.tag = menu!!.size
        }
        postInvalidate()
    }


}
