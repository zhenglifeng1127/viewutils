package com.zero.viewutils.utils.extends

import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.maps2d.model.LatLng
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.zero.viewutils.R
import com.zero.viewutils.utils.AppUtils
import com.zero.viewutils.utils.DeviceUtils
import com.zero.viewutils.utils.PopupUtils
import com.zero.viewutils.utils.singleton.RxUtils

/**
 * get set
 * 给view添加一个上次触发时间的属性（用来屏蔽连击操作）
 */
private var <T : View>T.triggerLastTime: Long
    get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
    set(value) {
        setTag(R.id.triggerLastTimeKey, value)
    }

/**
 * get set
 * 给view添加一个延迟的属性（用来屏蔽连击操作）
 */
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
    set(value) {
        setTag(R.id.triggerDelayKey, value)
    }

/**
 * 判断时间是否满足再次点击的要求（控制点击）
 */
private fun <T : View> T.clickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        clickable = true
    }
    triggerLastTime = currentClickTime
    return clickable
}


/**
 * 延迟点击,针对baseQuickAdapter列表点击事件封装
 */
fun <D :Any?> RecyclerView.clickItemDelay(delay: Long = 500, block: (BaseQuickAdapter<D, *>,  Int) -> Unit) {
    triggerDelay = delay
    adapter?.let {
        if (it is BaseQuickAdapter<*, *>) {
            it.setOnItemClickListener { _, _, position ->
                if (clickEnable()) {
                    block(it as BaseQuickAdapter<D, *>, position)
                }
            }
        }
    }
}

/**
 * 延迟点击,针对baseQuickAdapter子项点击事件封装
 */

fun <D :Any?> RecyclerView.clickChildItemDelay(delay: Long = 500, block: (BaseQuickAdapter<D, *>, Int, Int) -> Unit) {
    triggerDelay = delay
    adapter?.let {
        if (it is BaseQuickAdapter<*, *>) {
            it.setOnItemChildClickListener { _, v, position ->
                if (clickEnable()) {
                    block(it as BaseQuickAdapter<D, *>, v.id, position)
                }
            }
        }
    }
}

/**
 * 延迟点击,针对baseQuickAdapter子项点击事件封装
 */
fun <D :Any?> RecyclerView.clickChildItemDelay(delay: Long = 500, block: (D, View) -> Unit) {
    triggerDelay = delay
    adapter?.let {
        if (it is BaseQuickAdapter<*, *>) {
            it.setOnItemChildClickListener { _, v, position ->
                if (clickEnable()) {
                    block((it as BaseQuickAdapter<D, *>).data[position], v)
                }
            }
        }
    }
}

/***
 * 带延迟过滤点击事件的 View 扩展
 * @param delay Long 延迟时间，默认500毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickDelay(delay: Long = 500, block: (T) -> Unit) {
    triggerDelay = delay
    setOnClickListener {
        if (clickEnable()) {
            block(this)
        }
    }
}

/**
 * 常规列表点击
 */
fun <T : AbsListView> T.clickListItemDelay(delay: Long = 500, block: (T, Int) -> Unit) {
    triggerDelay = delay
    setOnItemClickListener { _, _, position, _ ->
        if (clickEnable()) {
            block(this, position)
        }
    }
}

/**
 * 附加第三方应用安装判断判断点击事件
 */
fun <V : View> V.otherAppClick(packageName:String,name: String ="对应软件", block: (V) -> Unit) {
    clickDelay { v ->
        if(AppUtils.isInstallApp(packageName,context)){
            block(v)
        }else{
            PopupUtils.normal("温馨提示","请先确认安装"+name+"后，再进行操作", OnConfirmListener {

            })
        }
    }
}

/**
 * 获取权限点击事件，具体权限适配根据版本调整，配套文件other-PERMISSION
 */
fun <V : View> V.rxClick(p: Array<String>, block: (V) -> Unit) {
    clickDelay { v ->

        RxUtils.getPermission(p)?.let { its ->
            its.subscribe {
                if (it) {
                    block(v)
                } else {
                    PopupUtils.normal("温馨提示","权限不足，是否跳转应用配置", OnConfirmListener {
                        AppUtils.openAppConfig()
                    })
                }
            }
        }
    }
}

/**
 * 根据坐标打开外部地图导航点击事件，支持高德，百度，网页等
 */
fun <T:View>T.clickDelayMap(latLng: LatLng, name: String, delay: Long = 500) {
    setOnClickListener {
        if (clickEnable()) {
            DeviceUtils.openAMap(latLng.latitude,latLng.longitude,name)
        }
    }
}