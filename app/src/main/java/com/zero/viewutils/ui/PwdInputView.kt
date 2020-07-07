package com.zero.viewutils.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zero.viewutils.R
import com.zero.viewutils.utils.extends.tvString
import kotlinx.android.synthetic.main.ui_pwd_input_view.view.*

/**
 * 密码输入框控件支持自定义布局 model 0*样式，1常规
 */
class PwdInputView : FrameLayout {
    private var layoutRes = R.layout.ui_pwd_input_view

    private var listener: ClickNextListener? = null

    private var model = 0

    private val viewBody by lazy { LayoutInflater.from(context).inflate(layoutRes, null, false) }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(attrs)
    }

    private fun initAttr(attrs: AttributeSet?) {
        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdInputView)
        layoutRes = mTypedArray.getResourceId(R.styleable.PwdInputView_passView, layoutRes)
        model = mTypedArray.getInteger(R.styleable.PwdInputView_inputMode, model)
        mTypedArray.recycle()
        viewBody.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(viewBody)
        when (layoutRes) {
            R.layout.ui_pwd_input_view -> {
                viewBody.itemContent.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable) {
                        viewBody.itemOne.text = ""
                        viewBody.itemTwo.text = ""
                        viewBody.itemFour.text = ""
                        viewBody.itemThree.text = ""
                        viewBody.itemFive.text = ""
                        viewBody.itemSix.text = ""
                        when (model) {
                            0 -> {
                                when (s.length) {
                                    6 -> {
                                        viewBody.itemSix.text = "*"
                                        viewBody.itemFive.text = "*"
                                        viewBody.itemFour.text = "*"
                                        viewBody.itemThree.text = "*"
                                        viewBody.itemTwo.text = "*"
                                        viewBody.itemOne.text = "*"
                                    }
                                    5 -> {
                                        viewBody.itemFive.text = "*"
                                        viewBody.itemFour.text = "*"
                                        viewBody.itemThree.text = "*"
                                        viewBody.itemTwo.text = "*"
                                        viewBody.itemOne.text = "*"
                                    }
                                    4 -> {
                                        viewBody.itemFour.text = "*"
                                        viewBody.itemThree.text = "*"
                                        viewBody.itemTwo.text = "*"
                                        viewBody.itemOne.text = "*"
                                    }
                                    3 -> {
                                        viewBody.itemThree.text = "*"
                                        viewBody.itemTwo.text = "*"
                                        viewBody.itemOne.text = "*"
                                    }
                                    2 -> {
                                        viewBody.itemTwo.text = "*"
                                        viewBody.itemOne.text = "*"
                                    }
                                    1 -> viewBody.itemOne.text = "*"
                                    else -> {
                                    }
                                }
                            }
                            1 -> {
                                when (s.length) {
                                    6 -> {
                                        viewBody.itemSix.text = s.subSequence(5, 6)
                                        viewBody.itemFive.text = s.subSequence(4, 5)
                                        viewBody.itemFour.text = s.subSequence(3, 4)
                                        viewBody.itemThree.text = s.subSequence(2, 3)
                                        viewBody.itemTwo.text = s.subSequence(1, 2)
                                        viewBody.itemOne.text = s.subSequence(0, 1)
                                    }
                                    5 -> {
                                        viewBody.itemFive.text = s.subSequence(4, 5)
                                        viewBody.itemFour.text = s.subSequence(3, 4)
                                        viewBody.itemThree.text = s.subSequence(2, 3)
                                        viewBody.itemTwo.text = s.subSequence(1, 2)
                                        viewBody.itemOne.text = s.subSequence(0, 1)
                                    }
                                    4 -> {
                                        viewBody.itemFour.text = s.subSequence(3, 4)
                                        viewBody.itemThree.text = s.subSequence(2, 3)
                                        viewBody.itemTwo.text = s.subSequence(1, 2)
                                        viewBody.itemOne.text = s.subSequence(0, 1)
                                    }
                                    3 -> {
                                        viewBody.itemThree.text = s.subSequence(2, 3)
                                        viewBody.itemTwo.text = s.subSequence(1, 2)
                                        viewBody.itemOne.text = s.subSequence(0, 1)
                                    }
                                    2 -> {
                                        viewBody.itemTwo.text = s.subSequence(1, 2)
                                        viewBody.itemOne.text = s.subSequence(0, 1)
                                    }
                                    1 -> viewBody.itemOne.text = s.subSequence(0, 1)
                                    else -> {
                                    }
                                }
                            }
                        }

                        if (s.length == 6) {
                            listener?.onClickNext(s.toString().trim())
                        }
                    }

                })
            }
        }
    }

    fun getInput():String{
        return viewBody.itemContent.tvString()
    }

    fun edit(bol:Boolean){
        viewBody.itemContent.isEnabled = bol
    }

    fun clear() {
        viewBody.itemOne.text = ""
        viewBody.itemTwo.text = ""
        viewBody.itemFour.text = ""
        viewBody.itemThree.text = ""
        viewBody.itemFive.text = ""
        viewBody.itemSix.text = ""
        viewBody.itemContent.setText("")
    }


    fun setClickNextListener(listener: ClickNextListener) {
        this.listener = listener
    }

    interface ClickNextListener {
        fun onClickNext(text: String)
    }
}