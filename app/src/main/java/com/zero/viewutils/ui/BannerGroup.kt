package com.zero.viewutils.ui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.zero.viewutils.R
import com.zero.viewutils.adapter.VpAdapter
import com.zero.viewutils.utils.extends.centerFit
import com.zero.viewutils.utils.extends.clickDelay
import com.zero.viewutils.utils.extends.getDimenPx

/**
 * 带数量指示器组合viewpager布局控件
 */
class BannerGroup : FrameLayout {

    private var listener:ImageListener?=null

    private val pager by lazy {
        ViewPager(context)
    }

    private val tvCount by lazy { TextView(context) }

    private var tvBackground = R.drawable.shape_corners_dp7_cdd0d7

    private var bannerList: MutableList<View>? = null


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val dp5 = context.getDimenPx(R.dimen.dp_5)
        val dp12 = context.getDimenPx(R.dimen.dp_12)

        pager.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT,context.getDimenPx(R.dimen.dp_16),Gravity.END or Gravity.BOTTOM)
        lp.marginEnd = dp12
        lp.bottomMargin = dp12
        tvCount.layoutParams = lp
        tvCount.text = "0/0"
        tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        tvCount.gravity = Gravity.CENTER
        tvCount.setPadding(dp5,0,dp5,0)
        tvCount.setBackgroundResource(tvBackground)


        addView(pager,0)
        addView(tvCount,1)
    }

    fun create(data:MutableList<String>){
        bannerList = ArrayList()
        for(i in data.indices){
            val imageView = ImageView(context)
            imageView.layoutParams = pager.layoutParams
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.centerFit(data[i])
            imageView.clickDelay {
                listener?.clickImage(pager,imageView,i)
            }
            bannerList?.add(imageView)
        }

        bannerList?.let {
            tvCount.text = ("1/"+it.size)
            pager.adapter = VpAdapter(it)
            pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    tvCount.text =((position + 1).toString() + "/" + it.size)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            invalidate()
        }
    }


    fun setImageListener(listener:ImageListener){
        this.listener = listener
    }

    interface ImageListener{
        fun clickImage(pager: ViewPager,img:ImageView,i:Int)
    }


}