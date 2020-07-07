package com.zero.viewutils.utils.extends

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.zero.viewutils.utils.NumberFilter

/***
 *  TextView 及其子类拓展类工具
 */


enum class DrawableType {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM
}

/**
 * 设置drawable，枚举为方向
 */
fun TextView.drawable(@DrawableRes id: Int, type: DrawableType) {
    val dra = ContextCompat.getDrawable(this.context, id)
    dra?.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
    when (type) {
        DrawableType.TOP -> this.setCompoundDrawables(null, dra, null, null)
        DrawableType.LEFT -> this.setCompoundDrawables(dra, null, null, null)
        DrawableType.RIGHT -> this.setCompoundDrawables(null, null, dra, null)
        DrawableType.BOTTOM -> this.setCompoundDrawables(null, null, null, dra)
    }
}

/**
 * 清空drawable
 */
fun TextView.drawableNull() {
    setCompoundDrawables(
        null,
        null,
        null,
        null
    )
}

/**
 * 设置drawable型color
 */
fun TextView.setColorDrawable(@ColorRes id: Int) {
    if (Build.VERSION.SDK_INT > 22)
        this.setTextColor(resources.getColorStateList(id, null))
    else
        this.setTextColor(resources.getColorStateList(id))
}

/**
 * 设置文本颜色，通过id
 */
fun TextView.setResColor(@ColorRes id: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, id))
}

/**
 * 设置text文本过滤空指针类型
 */
fun TextView.setAnyText(any: Any?) {
    this.text = any?.toString() ?: ""
}

/**
 * 设置text文本大小变化
 */
fun TextView.textSizeSpan(text: String, start: Int, end: Int, size: Int) {
    val spannableString = SpannableString(text)
    val absoluteSizeSpan = AbsoluteSizeSpan(size, true)
    spannableString.setSpan(absoluteSizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    this.text = spannableString
}

/**
 * 设置text文本部分大小变化,支持组，size必须统一
 */
fun TextView.textSizeSpan(text: String, start: IntArray, end: IntArray, size: IntArray) {
    if (start.size == end.size && start.size == size.size) {
        val spannableString = SpannableString(text)
        for (i in start.indices) {
            val absoluteSizeSpan = AbsoluteSizeSpan(size[i], true)
            spannableString.setSpan(
                absoluteSizeSpan,
                start[i],
                end[i],
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        this.text = spannableString
    }
}

/**
 * 设置text文本部分颜色变化
 */
fun TextView.textColorSpan(text: String, start: Int, end: Int, color: Int) {
    val spannableString = SpannableString(text)
    val span = ForegroundColorSpan(ContextCompat.getColor(this.context, color))
    spannableString.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    this.text = spannableString
}

/**
 * toString并去除空格
 */
fun TextView.tvString(): String {
    return if (this.text == null) "" else this.text.toString().trim()
}

/**
 * 获取文本double值，空与不符合规则取0.0
 */
fun TextView.tvDouble(): Double {
    val str: String = text.toString()
    if (str.isEmpty()) {
        return 0.0
    }
    if (!Regex("^(([1-9]\\d*)|0)(\\.\\d{1,3})?$").matches(str)) {
        return 0.0
    }
    return this.text.toString().trim().toDouble()
}



/**
 * 显示或着隐藏文本
 */
fun EditText.hide(isChecked: Boolean) {
    this.transformationMethod =
        if (isChecked) PasswordTransformationMethod.getInstance() else HideReturnsTransformationMethod.getInstance()
    this.setSelection(this.length())
}

/**
 * 获取焦点，并打开键盘
 */
fun EditText.showInput() {
    requestFocus()
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 禁止复制
 */
fun EditText.closeCopy() {
    isLongClickable = false

    customSelectionActionModeCallback = object : ActionMode.Callback {

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {

        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return false
        }
    }
    imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
}

fun EditText.setNumMode() {
    val filters = arrayOf<InputFilter>(NumberFilter())
    this.filters = filters
}