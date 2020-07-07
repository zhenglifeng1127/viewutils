package com.zero.viewutils.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

/**
 * 在滑动布局中使用的webView，兼容5.0BUG
 */
class ScrollWebView :WebView{

    constructor(context: Context) : super( getFixedContext(context))

    constructor(context: Context, attributeSet: AttributeSet?) : this(getFixedContext(context), attributeSet, 0){
        getFixedContext(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        getFixedContext(context),
        attributeSet,
        defStyleAttr
    )

    companion object{
        fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT in 21..22) context.createConfigurationContext(
                Configuration()
            ) else context
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpc = MeasureSpec.makeMeasureSpec(
            Integer.MAX_VALUE shr 2,
            MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, expandSpc)
    }

}