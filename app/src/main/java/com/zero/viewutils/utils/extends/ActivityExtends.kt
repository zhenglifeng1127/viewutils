package com.zero.viewutils.utils.extends

import android.view.View
import androidx.annotation.LayoutRes
import android.app.ActivityManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity

import com.zero.viewutils.base.BaseActivity
import com.zero.viewutils.utils.singleton.AppManager
import com.zero.viewutils.utils.singleton.AppManager.isExit

import java.util.*
import kotlin.system.exitProcess


fun BaseActivity<*>.inflate(@LayoutRes id:Int): View {
    return this.layoutInflater.inflate(id, null,false)
}




//activity类拓展工具

/**
 * 获取自定义view布局简化
 *
 * @param id 布局xml id
 *
 */
fun AppCompatActivity.inflate(@LayoutRes id: Int): View {
    return this.layoutInflater.inflate(id, null, false)
}

/**
 * 跳转页面
 *
 * @param cls 目标页面class
 *
 * @param bundle 附加数据，可为空
 *
 */
 fun AppCompatActivity.openNext(cls: Class<*>, bundle: Bundle? = null) {
    val intent = Intent()
    intent.setClass(this, cls)
    bundle?.let {
        intent.putExtras(bundle)
    }
    this.startActivity(intent)
}

/**
 * 带返回的页面跳转
 *
 * @param cls 目标页面class
 *
 * @param code 请求code
 *
 * @param bundle 附加数据,可为空
 *
 */
fun AppCompatActivity.forResult(cls: Class<*>, code: Int, bundle: Bundle? = null) {
    val intent = Intent()
    intent.setClass(this, cls)
    bundle?.let {
        intent.putExtras(bundle)
    }
    this.startActivityForResult(intent, code)
}

/**
 * 带返回的页面跳转返回
 *
 * @param code 请求code
 *
 * @param bundle 附加数据,可为空
 *
 */
fun AppCompatActivity.resultBack(code: Int, bundle: Bundle?) {
    val intent = Intent()
    bundle?.let {
        intent.putExtras(bundle)
    }
    this.setResult(code, intent)
    this.finish()
}

/**
 * 双击退出
 */
fun AppCompatActivity.exitApp() {
    val tExit: Timer
    if (!isExit) {
        isExit = true // 准备退出
        this.toast("再点击一次退出")
        tExit = Timer()
        tExit.schedule(object : TimerTask() {
            override fun run() {
                isExit = false // 取消退出
            }
        }, 2000) // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
    } else {
        try {
            AppManager.close()
            val activityMgr = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.killBackgroundProcesses(this.packageName)
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun AppCompatActivity.hideInput(){
    val imm : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    val v = window.peekDecorView()
    v?.let {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

/**
 * 点击View是否需要关闭软键盘
 */
fun View.isShouldHideKeyboard( event: MotionEvent): Boolean {
    if (this is EditText) {
        val l = intArrayOf(0, 0)
        this.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val bottom = top +this.getHeight()
        val right = left + this.getWidth()
        return !(event.x > left && event.x < right
                && event.y > top && event.y < bottom)
    }
    // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
    return false
}



