package com.zero.viewutils.utils.singleton

import android.os.Build
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zero.viewutils.utils.other.Permission
import io.reactivex.Observable

/**
 * 处理申请权限
 */
object RxUtils {

    private var rxPermissions = RxPermissions(AppManager.endOfStack())

    fun getPermission(p: Array<String>): Observable<Boolean>? {
        return when (p) {
            Permission.CALENDAR -> request(Permission.CALENDAR)
            Permission.CAMERA -> request(Permission.CAMERA)
            Permission.APK -> request(Permission.APK)
            Permission.LOCATION -> request(Permission.LOCATION)
            Permission.CONTACTS -> request(Permission.CONTACTS)
            Permission.MICROPHONE -> request(Permission.MICROPHONE)
            Permission.PHONE -> request(Permission.PHONE)
            Permission.SENSORS -> request(Permission.SENSORS)
            Permission.SMS -> request(Permission.SMS)
            Permission.STORAGE -> request(Permission.STORAGE)
            else -> null
        }
    }

    private fun request(p: Array<String>): Observable<Boolean>? {
        return if (Build.VERSION.SDK_INT > 22) {
            rxPermissions.request(*p)
        } else {
            null
        }
    }
}