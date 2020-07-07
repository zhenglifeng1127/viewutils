package com.zero.viewutils.base

import android.os.Environment

class Constants {

    companion object {

        val isDebug: Boolean by lazy { false }

        val BASE_URL_API: String by lazy { "" }//本地

        val GALLERY_PATH by lazy {BaseApplication.getCon().getExternalFilesDir(Environment.DIRECTORY_PICTURES)}

        val spName: String by lazy { "SP_MODE" }

        const val FIRST_PAGE = 1

        const val FIRST_PER_PAGE = 20

        const val WX_KEY = ""

        const val SDK_PAY_FLAG = 1

        const val SDK_AUTH_FLAG = 2

        const val PACKAGE_WECHAT = "com.tencent.mm"//微信

        const val PACKAGE_MOBILE_QQ = "com.tencent.mobileqq" //qq

        const val SERVICE_PHONE = ""

        const val WEB_STYLE = "<style>\n" +
                "img{max-width:100%; height:auto}\n" +
                "video{max-width:100%;height:auto}\n" +
                "</style>"
    }
}