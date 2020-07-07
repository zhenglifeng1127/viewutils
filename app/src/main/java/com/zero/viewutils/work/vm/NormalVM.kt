package com.zero.viewutils.work.vm

import com.zero.viewutils.base.BaseVM
import com.zero.viewutils.work.model.NormalModel

class NormalVM :BaseVM(){
    private val model by lazy { NormalModel() }

    override fun onCreate() {
        super.onCreate()
    }


}