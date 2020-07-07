package com.zero.viewutils.ui

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView
import com.zero.viewutils.R
import com.zero.viewutils.utils.singleton.AppManager

class LoadingPopup(context: Context = AppManager.endOfStack()) :CenterPopupView(context){

    override fun getImplLayoutId(): Int {
        return R.layout.item_popup_loading
    }
}