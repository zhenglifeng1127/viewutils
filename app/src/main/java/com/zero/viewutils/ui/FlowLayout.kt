package com.zero.viewutils.ui

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.collection.ArrayMap
import androidx.core.content.ContextCompat
import com.zero.viewutils.R
import com.zero.viewutils.entity.ValueBean
import com.zero.viewutils.utils.extends.setColorDrawable
import com.zero.viewutils.utils.extends.setResColor

/**
 * 浮动标签，占满一行自动换行
 */
class FlowLayout : RadioGroup {


    private var listener: OnClickTextListener? = null

    private var radioListener: OnRadioClickTextListener? = null

    private var backgroundRes: Int = R.drawable.shape_corners_dp16_ffedeff2

    private var orientationPadding: Int = 28

    private var verticalPadding: Int = 5

    private var orientationSpace: Int = 10

    private var verticalSpace: Int = 5

    private var textSize: Float = 14f

    private var checkId: Int = -1

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setAttrs(attrs)
    }


    private fun setAttrs(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        backgroundRes = a.getResourceId(
            R.styleable.FlowLayout_backgroundRes,
            R.drawable.shape_corners_dp16_ffedeff2
        )

        orientationPadding =
            a.getDimensionPixelOffset(R.styleable.FlowLayout_orientationPadding, orientationPadding)

        verticalPadding =
            a.getDimensionPixelOffset(R.styleable.FlowLayout_verticalPadding, verticalPadding)

        textSize = a.getDimension(R.styleable.FlowLayout_textSize, textSize)

        orientationSpace =
            a.getDimensionPixelOffset(R.styleable.FlowLayout_orientationSpace, orientationSpace)

        verticalSpace =
            a.getDimensionPixelOffset(R.styleable.FlowLayout_verticalSpace, verticalSpace)

        a.recycle()
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val rect = getChildAt(i).tag as Rect
            child.layout(rect.left, rect.top, rect.right, rect.bottom)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var measuredWidth = 0
        var measuredHeight = 0

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //由于计算子view所占宽度，这里传值需要自身减去PaddingRight宽度，PaddingLeft会在接下来计算子元素位置时加上
        val compute = compute(widthSize - paddingRight)

        //EXACTLY模式：对应于给定大小或者match_parent情况
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize
            //AT_MOS模式：对应wrap-content（需要手动计算大小，否则相当于match_parent）
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = compute["allChildWidth"]!!
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = compute["allChildHeight"]!!
        }
        //设置flow的宽高
        setMeasuredDimension(measuredWidth, measuredHeight)
    }


    /**
     * 测量过程
     *
     * @param flowWidth 该view的宽度
     * @return 返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
     */
    private fun compute(flowWidth: Int): ArrayMap<String, Int> {
        //是否是单行
        var aRow = true
        var marginParams: MarginLayoutParams//子元素margin
        var rowsWidth = paddingLeft//当前行已占宽度(注意需要加上paddingLeft)
        var columnHeight = paddingTop//当前行顶部已占高度(注意需要加上paddingTop)
        var rowsMaxHeight = 0//当前行所有子元素的最大高度（用于换行累加高度）

        for (i in 0 until childCount) {

            val child = getChildAt(i)
            //获取元素测量宽度和高度
            val measuredWidth = child.measuredWidth
            val measuredHeight = child.measuredHeight

            //获取元素的margin
            marginParams = child.layoutParams as MarginLayoutParams
            //子元素所占宽度 = MarginLeft+ child.getMeasuredWidth+MarginRight  注意此时不能child.getWidth,因为界面没有绘制完成，此时wdith为0
            val childWidth = marginParams.leftMargin + marginParams.rightMargin + measuredWidth
            val childHeight = marginParams.topMargin + marginParams.bottomMargin + measuredHeight
            //判断是否换行： 该行已占大小+该元素大小>父容器宽度  则换行
            rowsMaxHeight = rowsMaxHeight.coerceAtLeast(childHeight)
            //换行
            if (rowsWidth + childWidth > flowWidth) {
                //重置行宽度
                rowsWidth = paddingLeft + paddingRight
                //累加上该行子元素最大高度
                columnHeight += rowsMaxHeight
                //重置该行最大高度
                rowsMaxHeight = childHeight
                aRow = false
            }
            //累加上该行子元素宽度
            rowsWidth += childWidth
            //判断时占的宽段时加上margin计算，设置顶点位置时不包括margin位置，不然margin会不起作用，这是给View设置tag,在onlayout给子元素设置位置再遍历取出
            child.tag = Rect(
                rowsWidth - childWidth + marginParams.leftMargin,
                columnHeight + marginParams.topMargin,
                rowsWidth - marginParams.rightMargin,
                columnHeight + childHeight - marginParams.bottomMargin
            )
        }

        //返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
        val flowMap = ArrayMap<String, Int>()
        //单行
        if (aRow) {
            flowMap["allChildWidth"] = rowsWidth
        } else {
            //多行
            flowMap["allChildWidth"] = flowWidth
        }
        //FlowLayout测量高度 = 当前行顶部已占高度 +当前行内子元素最大高度+FlowLayout的PaddingBottom
        flowMap["allChildHeight"] = columnHeight + rowsMaxHeight + paddingBottom
        return flowMap
    }


    fun setCheckId(id: Int) {
        this.checkId = id
    }


    fun init(data: MutableList<String>, @ColorRes color: Int) {
        val layoutParams = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(orientationSpace, verticalSpace, orientationSpace, verticalSpace)
        if (this.childCount > 0) {
            removeAllViews()
        }
        for (i in data.indices) {
            if (data[i].isNotEmpty()) {

                val tv = TextView(context)
                tv.setResColor(color)
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                tv.setPadding(
                    orientationPadding,
                    verticalPadding,
                    orientationPadding,
                    verticalPadding
                )
                tv.text = data[i]
                tv.maxEms = 10
                tv.setSingleLine()
                tv.setBackgroundResource(backgroundRes)
                tv.layoutParams = layoutParams
                tv.setOnClickListener {
                    listener?.clickData(data[i])
                }
                addView(tv, layoutParams)
            }
        }
        invalidate()
    }

    fun initRadio(data: MutableList<ValueBean>, @ColorRes color: Int) {
        val layoutParams = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(orientationSpace, verticalSpace, orientationSpace, verticalSpace)
        if (this.childCount > 0) {
            removeAllViews()
        }
        for (i in data.indices) {
            if (!data[i].name.isNullOrEmpty()) {

                val tv = RadioButton(context)
                tv.setColorDrawable(color)
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                tv.setPadding(
                    orientationPadding,
                    verticalPadding,
                    orientationPadding,
                    verticalPadding
                )
                tv.id = data[i].id
                tv.text = data[i].name
                if (data[i].id == checkId)
                    tv.isChecked = true
                tv.setSingleLine()
                tv.setBackgroundResource(backgroundRes)
                tv.buttonDrawable = null
                tv.layoutParams = layoutParams
                tv.setOnClickListener {
                    radioListener?.clickData(data[i])
                }
                addView(tv, layoutParams)

            }
        }
        invalidate()
    }

    fun init(data: List<String>, background: Int, color: Int) {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 5, 10, 5)
        if (this.childCount > 0) {
            removeAllViews()
        }
        for (i in data.indices) {
            if (!TextUtils.isEmpty(data[i])) {
                val tv = TextView(context)
                tv.setTextColor(ContextCompat.getColor(context,color))
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                tv.setPadding(28, 5, 28, 5)
                tv.text = data[i]
                tv.maxEms = 10
                tv.setSingleLine()
                tv.setBackgroundResource(background)
                tv.layoutParams = layoutParams
                addView(tv, layoutParams)
            }
        }
    }


    fun setOnClickTextListener(listener: OnClickTextListener) {
        this.listener = listener
    }

    fun setOnRadioClickTextListener(listener: OnRadioClickTextListener) {
        this.radioListener = listener
    }

    interface OnClickTextListener {
        fun clickData(text: String)
    }

    interface OnRadioClickTextListener {
        fun clickData(text: ValueBean)
    }
}