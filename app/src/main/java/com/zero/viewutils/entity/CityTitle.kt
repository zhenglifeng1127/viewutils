package com.zero.viewutils.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

data class CityTitle(
    var type: Int,
    var data:MutableList<String>
) : MultiItemEntity {

    override fun getItemType(): Int {
        return type
    }

}