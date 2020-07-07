package com.zero.viewutils.utils.extends

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zero.viewutils.utils.other.CustomLinearLayoutManager


private fun manager(con: Context, orientation: Int): LinearLayoutManager {
    val manager = LinearLayoutManager(con)
    manager.orientation = orientation
    return manager
}

private fun managerHeight(con: Context, orientation: Int): LinearLayoutManager {
    val manager = object : LinearLayoutManager(con) {
        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
    manager.orientation = orientation
    return manager
}

/**
 * 管理是否可滑动
 */
fun RecyclerView.rvScroll(orientation: Int){
    val manager = CustomLinearLayoutManager(context)
    manager.orientation = orientation
    this.layoutManager = manager
}


/**
 * 常规线性使用
 */
fun RecyclerView.rvArgs(
    orientation: Int,
    decoration: RecyclerView.ItemDecoration?=null
) {

    this.layoutManager = manager(this.context, orientation)
    decoration?.let {
        if (this.itemDecorationCount > 0) {
            this.removeItemDecorationAt(0)
        }
        this.addItemDecoration(decoration)
    }
}


/**
 * 处理嵌套rv
 */
fun RecyclerView.rvArgsHeight(orientation: Int) {
    this.layoutManager = managerHeight(this.context, orientation)
}


/**
 * 多行不同Item时使用，设置adapter后设置
 */
fun RecyclerView.rvGrid(
    size: Int,
    spanSizeLookup: GridLayoutManager.SpanSizeLookup,
    decoration: RecyclerView.ItemDecoration? = null
) {

    val manager = GridLayoutManager(this.context, size)
    manager.spanSizeLookup = spanSizeLookup
    this.layoutManager = manager
    decoration?.let {
        if (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(decoration)
    }

}

/**
 * 常规网格布局使用
 */
fun RecyclerView.rvGrid(size: Int,  decoration: RecyclerView.ItemDecoration? = null) {
    this.layoutManager = GridLayoutManager(this.context, size)
    decoration?.let {
        if (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(decoration)
    }
}



