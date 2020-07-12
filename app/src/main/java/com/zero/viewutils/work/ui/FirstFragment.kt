package com.zero.viewutils.work.ui

import androidx.navigation.fragment.findNavController
import com.zero.viewutils.R
import com.zero.viewutils.base.BaseFragment
import com.zero.viewutils.utils.extends.clickDelay
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

    }

    override fun initListener() {
        buttonFirst.clickDelay {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        super.initListener()
    }
}