package com.zero.viewutils.utils.extends

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat

/**
 * 显示toast
 */
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
/**
 * 显示toast，根据资源文件
 */
fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}
/**
 * 显示toast，居中
 */
fun Context.centerToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val t = Toast.makeText(this, resId, duration)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
}

//----------尺寸转换----------

fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

//----------屏幕尺寸----------

fun Context.getScreenWidth(): Int {
    val dm = resources.displayMetrics
    return dm.widthPixels
}

fun Context.getScreenHeight(): Int {
    val dm = resources.displayMetrics
    return dm.heightPixels
}


//----------NetWork----------

/**
 * 打开网络设置
 */
fun Context.openWirelessSettings() {
    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}


/**
 * 退回到桌面
 */
fun Context.startHomeActivity() {
    val homeIntent = Intent(Intent.ACTION_MAIN)
    homeIntent.addCategory(Intent.CATEGORY_HOME)
    startActivity(homeIntent)
}

/**
 * 网络检测
 */
fun Context.getActiveNetworkInfo(): Boolean {
    val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        manager?.run {
            getNetworkCapabilities(this.activeNetwork)?.run {
                return when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    }else{
        manager?.run {
            activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    return true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                }
            }
        }
    }
    return false
}

/**
 * 获取arrays转换成list
 */
fun Context.listOf(@ArrayRes id:Int):ArrayList<String>{
    return this.resources.getStringArray(id).toList() as ArrayList<String>
}

/**
 * 获取arrays转换成string[]
 */
fun Context.stringArray(@ArrayRes id:Int):Array<String>{
    return this.resources.getStringArray(id)
}

/**
 * 简易Inflate
 */
fun Context.inflate(@LayoutRes id:Int): View {
    return LayoutInflater.from(this)
        .inflate(id, null,false)
}

/**
 * 获取dimen,根据适配而定
 */
fun Context.getDimen(@DimenRes id:Int):Float{
    return this.resources.getDimension(id)
}

/**
 * 获取dimen转换为PX,根据适配而定
 */
fun Context.getDimenPx(@DimenRes id:Int):Int{
    return this.resources.getDimensionPixelSize(id)
}

/**
 * 获取资源颜色
 */
fun Context.getResColor(@ColorRes id:Int):Int{
    return ContextCompat.getColor(this,id)
}

/**
 * 跳转拨号页面
 */
fun Context.callPhone(phone:String){
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}