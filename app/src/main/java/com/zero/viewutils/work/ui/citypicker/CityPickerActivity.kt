package com.zero.viewutils.work.ui.citypicker

import com.zero.viewutils.R
import com.zero.viewutils.base.BaseActivity
import com.zero.viewutils.base.BaseVM
import com.zero.viewutils.utils.ToolbarUtils
import kotlinx.android.synthetic.main.content_template.*
import kotlinx.android.synthetic.main.item_toolbar_normal.*

class CityPickerActivity : BaseActivity<BaseVM>(){

    override fun viewModel(): Class<BaseVM> {
        return BaseVM::class.java
    }

    override fun initArgs() {
        ToolbarUtils.Builder()
            .bar(_toolbar)
            .build()
    }

    override fun initData() {

    }

    override fun bindBody(): Any {
        return itemBody
    }

    override fun bindView(): Int {
        return R.layout.activity_city_picker
    }
}