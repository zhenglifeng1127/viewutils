package com.zero.viewutils.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zero.viewutils.utils.extends.toast
import com.zero.viewutils.utils.singleton.AppManager
import com.zero.viewutils.utils.singleton.AppManager.isExit
import java.util.*

/**
 * 跳转辅助类
 */
class ActivityUtils {
    companion object {
        /**
         * 常规跳转
         */
        fun openNext(cls: Class<*>, bundle: Bundle?=null) {
            val intent = Intent()
            intent.setClass(AppManager.endOfStack(), cls)
            bundle?.let {
                intent.putExtras(it)
            }
            AppManager.endOfStack().startActivity(intent)
        }

        /**
         * startActivityForResult
         *
         * @param bundle 数据
         * @param code   返回标识
         */
        fun resultBack(bundle: Bundle?, code: Int) {
            val intent = Intent()
            if (null != bundle) {
                intent.putExtras(bundle)
            }
            AppManager.endOfStack().setResult(code, intent)
            AppManager.finish()
        }
        /**
         * startActivityForResult
         *
         * @param bundle 数据
         * @param code   请求标识
         * @param cls 目标页面
         */
        fun openForResult(code: Int, cls: Class<*>,bundle: Bundle?=null) {
            val intent = Intent()
            intent.setClass(AppManager.endOfStack(), cls)
            bundle?.let {
                intent.putExtras(it)
            }
            AppManager.endOfStack().startActivityForResult(intent, code)
        }

        fun openForResult(intent: Intent, code: Int) {
            AppManager.endOfStack().startActivityForResult(intent, code)
        }


    }
}