package com.zero.viewutils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

/**
 * 指示器
 */
class SelectRadioGroup : RadioGroup {

    private var pager: ViewPager? = null
    private var background: Int = 0
    private var checkId: Int = 0
    private var unCheckId: Int = 0
    private var isOpen: Boolean = false
    private val size: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    fun pager(pager: ViewPager): SelectRadioGroup {
        this.pager = pager
        return this
    }

    fun drawChild(checkId: Int, unCheckId: Int): SelectRadioGroup {
        this.checkId = checkId
        this.unCheckId = unCheckId
        isOpen = true
        return this
    }


    fun drawable(@DrawableRes id: Int): SelectRadioGroup {
        this.background = id
        return this
    }


    fun show() {
        removeAllViews()
        pager?.let {
            if (Objects.requireNonNull<PagerAdapter>(it.adapter).count > 1) {
                for (i in 0 until it.adapter!!.count) {
                    val rb = RadioButton(context)
                    rb.gravity = Gravity.CENTER
                    rb.id = i

                    rb.setButtonDrawable(background)
                    if (i == 0) {
                        rb.isChecked = true
                    }
                    addView(rb)
                    val lp1 = rb.layoutParams as LayoutParams
                    lp1.setMargins(5, 0, 5, 0)
                    rb.layoutParams = lp1
                    if (isOpen) {
                        rb.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                rb.setButtonDrawable(checkId)
                            } else {
                                rb.setButtonDrawable(unCheckId)
                            }
                        }
                    }
                }

                setOnCheckedChangeListener { _, checkedId -> it.currentItem = checkedId }

                it.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                    }

                    override fun onPageSelected(position: Int) {
                        (getChildAt(position) as RadioButton).isChecked = true
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
            }

        }
    }
}
