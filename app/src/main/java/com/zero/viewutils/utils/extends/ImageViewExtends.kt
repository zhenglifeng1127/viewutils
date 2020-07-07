package com.zero.viewutils.utils.extends

import android.widget.ImageView
import com.zero.viewutils.utils.GlideUtils
import com.zero.viewutils.utils.QrUtils

/**
 * 设置本地资源
 */
fun ImageView.localImage(id: Int) {
    GlideUtils.localImage(this.context, id, this)
}

/**
 * 设置上圆角
 */
fun ImageView.cornerTop(url: String, size: Int) {
    if (url.isNotEmpty()) {
        GlideUtils.cornerTop(this.context, url, this, size)
    }
}

/**
 * 设置圆角
 */
fun ImageView.corner(url: String, size: Int) {
    if (url.isNotEmpty()) {
        GlideUtils.corner(this.context, url, this, size)
    }
}

/**
 * 设置网络图片
 */
fun ImageView.centerFit(url: String) {
    if (url.isNotEmpty()) {
        GlideUtils.centerFit(this.context, url, this)
    }
}


//fun ImageView.banner(url: String) {
//    if (url.isNotEmpty()) {
//        GlideUtils.banner(this.context, url, this)
//    }
//}

/**
 * 设置二维码
 */
fun ImageView.setQr(text: String, width: Int) {
    val w = context.getDimenPx(width)
    this.setImageBitmap(QrUtils.createQRImage(text, w, w))
}