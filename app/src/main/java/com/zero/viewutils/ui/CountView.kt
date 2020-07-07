package com.zero.viewutils.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.zero.viewutils.R
import com.zero.viewutils.utils.extends.inflate
import kotlinx.android.synthetic.main.item_count_view_style_01.view.*

class CountView : LinearLayout {

    var listener: OnSizeListener? = null

    var layout:Int = R.layout.item_count_view_style_01

    private var baseSize = 1
    private var size = 1
    private var max = 99999999


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initArgs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initArgs(attrs)
    }

    private fun initArgs(attrs: AttributeSet?){
        val a = context.obtainStyledAttributes(attrs, R.styleable.CountView)
        layout = a.getResourceId(
            R.styleable.CountView_countStyle,
            R.layout.item_count_view_style_01
        )
        val v = context.inflate(layout)
        addView(v)
        v.countUp.setOnClickListener {
            if (size < max) {
                size += 1
                v.countSize.text = size.toString()
                listener?.onSize(size, 1)
            }
        }
        v.countDown.setOnClickListener {
            if (size > baseSize) {
                size -= 1
                v.countSize.text = size.toString()
                listener?.onSize(size, -1)
            }
        }
        a.recycle()
    }



//    init {
//        val v = context.inflate(layout)
//        addView(v)
//        v.countUp.setOnClickListener {
//            if (size < max) {
//                size += 1
//                v.countSize.text = size.toString()
//                listener?.onSize(size, 1)
//            }
//        }
//        v.countDown.setOnClickListener {
//            if (size > baseSize) {
//                size -= 1
//                v.countSize.text = size.toString()
//                listener?.onSize(size, -1)
//            }
//        }
//    }

    fun setMax(max: Int) {
        this.max = max
    }

    fun getSize(): Int {
        return size
    }

    fun setSize(size: Int) {
        this.size = size
        val et = (getChildAt(0) as LinearLayout).getChildAt(1) as TextView
        et.text = size.toString()
    }

    fun setMinSize(baseSize: Int) {
        this.baseSize = baseSize
        if (size < baseSize) {
            setSize(baseSize)
        }
    }

    fun setOnSizeListener(listener: OnSizeListener) {
        this.listener = listener
    }

    interface OnSizeListener {
        fun onSize(size: Int, increment: Int)
    }
}