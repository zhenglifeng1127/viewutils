package com.zero.viewutils.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.zero.viewutils.BuildConfig
import com.zero.viewutils.utils.singleton.AppManager

class AppUtils {

    companion object {
        /**
         * 检查手机上是否安装了指定的软件
         *
         * @param name：应用包名
         */
        @Synchronized
        fun isInstallApp(name: String, con: Context): Boolean {
            val manager: PackageManager = con.packageManager
            val infoList: MutableList<PackageInfo> = manager.getInstalledPackages(0)

            for (info: PackageInfo in infoList) {
                if (name == info.packageName) {
                    return true
                }
            }
            return false
        }

        /**
         * 获取应用程序名称
         */
        @Synchronized
        fun getAppName(con: Context): String? {
            try {
                val packageManager = con.packageManager
                val packageInfo = packageManager.getPackageInfo(
                    con.packageName, 0
                )
                return packageInfo.applicationInfo.packageName
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * 根据机型选择打开应用设置
         */
        fun openAppConfig() {
            if (RomUtils.isFlyme()) {
                val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
                try {
                    AppManager.endOfStack().startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return
            }
            if (RomUtils.isMiui()) {
                val i = Intent("miui.intent.action.APP_PERM_EDITOR")
                val componentName = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                )
                i.component = componentName
                i.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID)
                try {
                    AppManager.endOfStack().startActivity(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return
            }
            if (RomUtils.isEmui()) {
                try {
                    val intent = Intent()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val comp = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.permissionmanager.ui.MainActivity"
                    )//华为权限管理
                    intent.component = comp
                    AppManager.endOfStack().startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return
            }

            val localIntent = Intent()
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.action = Intent.ACTION_VIEW
                localIntent.setClassName(
                    "com.android.settings",
                    "com.android.settings.InstalledAppDetails"
                )
                localIntent.putExtra(
                    "com.android.settings.ApplicationPkgName",
                    BuildConfig.APPLICATION_ID
                )
            }
            AppManager.endOfStack().startActivity(localIntent)
        }

    }
}