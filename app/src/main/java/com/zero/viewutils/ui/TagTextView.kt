package com.zero.viewutils.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.zero.viewutils.R
import com.zero.viewutils.utils.extends.inflate
import kotlinx.android.synthetic.main.ui_tag_item.view.*
import java.util.*

/**
 * 头部带样式标签的文本
 */
class TagTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    private fun setContentAndTag(content: String, tags: MutableList<String>) {
        val contentBuffer = StringBuffer()
//        for (item in tags) {//将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
//            contentBuffer.append(item)
//        }
        contentBuffer.append(content)
        val spannableString = SpannableString("    $contentBuffer")
        for (i in tags.indices) {
            val item = tags[i]
            val view =context.inflate(R.layout.ui_tag_item)
            val tvTag = view.tvTag
            tvTag.text = item
            val bitmap = convertViewToBitmap(view)
            val d = BitmapDrawable(context.resources, bitmap)
            d.setBounds(0, 0, tvTag.width, tvTag.height)//缺少这句的话，不会报错，但是图片不回显示
            val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM)//图片将对齐底部边线
            val startIndex: Int = getLastLength(tags, i)
            val endIndex: Int
            endIndex = startIndex + item.length
            spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = spannableString
        gravity = Gravity.CENTER_VERTICAL
    }

    private fun convertViewToBitmap(view: View): Bitmap {
        view.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        return view.drawingCache
    }


    private fun getLastLength(list: List<String>, maxLength: Int): Int {
        var length = 0
        for (i in 0 until maxLength) {
            length += list[i].length
        }
        return length
    }

    fun addTag(content: String) {
        val list = ArrayList<String>()
        list.add("标签")
        setContentAndTag(content, list)
    }
}