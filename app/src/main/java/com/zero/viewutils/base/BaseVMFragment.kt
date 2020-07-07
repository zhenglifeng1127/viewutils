package com.zero.viewutils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.greenrobot.eventbus.EventBus

abstract class BaseVMFragment<VM:BaseVM> : Fragment() {

    private var isFirstLoad = true

    protected lateinit var vm: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(bindView(), container, false)
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = true
    }

    override fun onResume() {
        super.onResume()
        if(isFirstLoad){

            activity?.let {
                vm = ViewModelProvider(it).get(viewModel())
            }

            initArgs()

            initReceiver()

            initData()

            initListener()

            isFirstLoad = false
        }
    }

    protected abstract fun viewModel(): Class<VM>

    protected abstract fun bindView(): Int

    protected abstract fun initArgs()

    protected abstract fun initReceiver()

    protected abstract fun initData()

    open fun initListener(){

    }





}