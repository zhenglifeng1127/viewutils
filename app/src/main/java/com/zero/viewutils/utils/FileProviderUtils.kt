package com.zero.viewutils.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * FileProvider uri工具类
 */
class FileProviderUtils {
    companion object {

        fun getFileUri(con: Context, path: String): Uri? {
            if (path.isEmpty()) {
                return null
            }
            val file = File(path)
            file.parentFile?.mkdirs()
            return getFileUri(con, file)
        }

        fun getFileUri(context: Context, mFile: File): Uri? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    AppUtils.getAppName(context) + ".selector.provider",
                    mFile
                )
            } else {
                Uri.fromFile(mFile)
            }
        }

        /**
         * uri动态读写权限授予
         *
         * @param context
         * @param intent
         * @param uri
         */
        fun grantUriPermission(context: Context, intent: Intent, uri: Uri) {
            val resolveInfoList = context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    }
}