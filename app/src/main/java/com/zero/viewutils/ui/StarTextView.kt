package com.zero.viewutils.ui

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.zero.viewutils.R

/**
 * 简易带星号文本控件
 */
class StarTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setText(text: CharSequence?, type: BufferType?) {
        val str = "$text*"
        val spannableString = SpannableString(str)
        val span = ForegroundColorSpan(ContextCompat.getColor(this.context, R.color.price))
        spannableString.setSpan(span,str.length-1,str.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        super.setText(spannableString, type)
    }

}