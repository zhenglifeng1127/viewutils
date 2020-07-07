package com.zero.viewutils.utils

import android.view.View
import android.widget.ImageView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.OnSelectListener
import com.zero.viewutils.ui.LoadingPopup
import com.zero.viewutils.utils.extends.asAppConfirm
import com.zero.viewutils.utils.other.PopupGlideLoader
import com.zero.viewutils.utils.singleton.AppManager

/**
 * XPopup库封装工具
 */
class PopupUtils {

    companion object {
        /**
         * 常规弹出样式
         */
        fun normal(title: String = "温馨提示", content: String, listener: OnConfirmListener) {
            XPopup.Builder(AppManager.endOfStack()).asAppConfirm(
                title, content, listener
            ).show()
        }

        /**
         * 常规弹出样式
         */
        fun normal(
            title: String = "温馨提示",
            content: String,
            cancel: String,
            confirm: String,
            listener: OnConfirmListener,
            cancelListener: OnCancelListener
        ) {
            XPopup.Builder(AppManager.endOfStack()).asAppConfirm(
                title, content, cancel, confirm, listener, cancelListener
            ).show()
        }

        /**
         * 依附VIEW显示简单列表，PartShadowPopupView可制作筛选效果
         */
        fun attach(v: View, title: Array<String>, icon: IntArray?, listener: OnSelectListener) {
            XPopup.Builder(AppManager.endOfStack())
                .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(
                    title,
                    icon,
                    listener
                ).show()
        }

        /**
         * 大图展示
         */
        fun img(img: ImageView, position: Int, data: List<Any>?) {
            XPopup.Builder(AppManager.endOfStack()).asImageViewer(
                img, position, data,
                { popupView, _ ->
                    // 作用是当Pager切换了图片，需要更新源View
                    popupView.updateSrcView(img)
                }, PopupGlideLoader()
            ).show()
        }

//        fun img(img: ImageView, position: Int, list: List<CommonImage>) {
//            XPopup.Builder(AppManager.endOfStack()).asImageViewer(
//                img, position, list,
//                { popupView, _ ->
//                    // 作用是当Pager切换了图片，需要更新源View
//                    popupView.updateSrcView(img)
//                }, GlideListLoader(list = list.toMutableList())
//            ).show()
//        }

        /**
         * 右侧显示
         */
        fun showRightPopup(popup: BasePopupView) {
            XPopup.Builder(AppManager.endOfStack())
                .popupPosition(PopupPosition.Right)//右边
                .hasStatusBarShadow(true) //启用状态栏阴影
                .asCustom(popup)
                .show()
        }

        /**
         * 显示已有popup
         */
        fun showPopup(popup: BasePopupView) {
            XPopup.Builder(AppManager.endOfStack())
                .moveUpToKeyboard(false)
                .asCustom(popup)
                .show()
        }

        /**
         * 显示loading
         */
        fun showLoad(): LoadingPopup {
            val popup = LoadingPopup()
            XPopup.Builder(AppManager.endOfStack())
                .autoOpenSoftInput(true)
                .asCustom(popup)
                .show()
            return popup
        }


    }
}