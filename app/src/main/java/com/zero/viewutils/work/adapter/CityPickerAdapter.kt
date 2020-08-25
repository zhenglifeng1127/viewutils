package com.zero.viewutils.work.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zero.viewutils.R
import com.zero.viewutils.entity.CitySortItem
import com.zero.viewutils.entity.CityTitle

class CityPickerAdapter(data: MutableList<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {



    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType){
            0->{
               helper.setText(R.id._cityPickerTitle,(item as CitySortItem).name)
            }
            1->{
                helper.setText(R.id._cityPickerTitle,(item as CitySortItem).name)
                helper.addOnClickListener(R.id._cityPickerTitle)
            }
            2->{
                val name =(item as CitySortItem).name
                helper.setText(R.id._cityPickerLocation,name)
                helper.addOnClickListener(R.id._cityPickerLocation)
            }
            3->{
                val title = item as CityTitle

            }
        }
    }

    init {
        addItemType(0, R.layout.item_picker_title)
        addItemType(1, R.layout.item_picker_city)
        addItemType(2, R.layout.item_picker_location)
        addItemType(3,R.layout.item_picker_hot)
    }



}