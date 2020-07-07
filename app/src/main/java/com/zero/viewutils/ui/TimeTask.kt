package com.zero.viewutils.ui

import android.os.CountDownTimer
import android.text.SpannableString
import android.widget.TextView
import com.zero.viewutils.utils.extends.timeSpan

class TimeTask(all: Long, wait: Long) : CountDownTimer(all, wait) {

    private var tv: TextView? = null
    private var type = 0
    private var end:Long = 0L

    private var listener:OnEndListener? =null


    constructor(all: Long, wait: Long, tv: TextView, type: Int) : this(
        all,
        wait
    ) {
        this.tv = tv
        this.type = type
    }

    constructor(all: Long, wait: Long, tv: TextView, type: Int,end :Long) : this(
        all,
        wait
    ) {
        this.tv = tv
        this.type = type
        this.end = end
    }


    override fun onTick(l: Long) {

        if (type == 0) {
            val day = l / (1000 * 60 * 60 * 24)
            val hour = (l - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) //单位时
            val minute =
                (l - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60) //单位分
            val second =
                (l - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000//单位秒
            tv?.let {
                val d = day.toString()
                val h = String.format("%02d", hour)
                val m = String.format("%02d", minute)
                val s = String.format("%02d", second)
                val str = SpannableString("距结束 $d  天 $h  : $m  : $s ")
                str.timeSpan(4, 4 + d.length)
                str.timeSpan(8 + d.length, 8 + d.length + h.length)
                str.timeSpan(12 + d.length + h.length, 12 + d.length + h.length + m.length)
                str.timeSpan(16 + d.length + h.length + m.length,str.length)
                it.text = str
            }
        }
    }

    override fun onFinish() {
        if (type == 0) {
            tv?.let {
                it.text = "已结束"
            }
        }else{
            listener?.isFinish()
        }
    }

    fun setOnEndListener(listener: OnEndListener){
        this.listener = listener
    }


    interface OnEndListener{
        fun isFinish()
    }
}