package com.zero.viewutils.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

data class CitySortItem(
    var name: String? = null,

    val sortLetters: String? = null,

    val type:Int
): MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }


}