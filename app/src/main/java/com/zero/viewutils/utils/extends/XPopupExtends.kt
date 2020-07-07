package com.zero.viewutils.utils.extends

import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.ConfirmPopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.zero.viewutils.R

fun XPopup.Builder.asAppConfirm(
    title: String,
    content: String,
    confirmListener: OnConfirmListener
): ConfirmPopupView {
    return asConfirm(
        title,
        content,
        null,
        null,
        confirmListener,
        null,
        false
    ).bindLayout(R.layout.dialog_xpopup_center_impl_confirm)
}

fun XPopup.Builder.asAppConfirm(
    title: String,
    content: String,
    cancel: String,
    confirm: String,
    confirmListener: OnConfirmListener,
    cancelListener: OnCancelListener
): ConfirmPopupView {
    return asConfirm(title, content,cancel, confirm, confirmListener, cancelListener, false).bindLayout(
        R.layout.dialog_xpopup_center_impl_confirm
    )
}