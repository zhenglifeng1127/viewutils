package com.zero.viewutils.work.ui

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.zero.viewutils.R
import com.zero.viewutils.base.BaseFragment
import com.zero.viewutils.base.L
import com.zero.viewutils.utils.DeviceUtils
import com.zero.viewutils.utils.extends.clickDelay
import com.zero.viewutils.utils.extends.openCamera
import com.zero.viewutils.utils.singleton.CityManager
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : BaseFragment() {


    override fun bindView(): Int {
        return R.layout.fragment_first
    }

    override fun initArgs() {

    }

    override fun initReceiver() {

    }

    override fun initData() {
        cityPicker.openCamera("img.jpg")
    }




    override fun initListener() {
        buttonFirst.clickDelay {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        super.initListener()
    }
}