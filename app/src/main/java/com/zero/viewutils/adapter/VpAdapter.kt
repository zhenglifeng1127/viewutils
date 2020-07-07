package com.zero.viewutils.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class VpAdapter(viewList:MutableList<View>) :PagerAdapter(){

    private val list = viewList


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(list[position])
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(list[position], 0)//添加页卡
        return list[position]
    }


    override fun getCount(): Int {
       return list.size
    }

}