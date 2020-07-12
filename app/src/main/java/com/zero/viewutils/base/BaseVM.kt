package com.zero.viewutils.base

import android.os.Bundle
import androidx.lifecycle.*
import com.zero.viewutils.utils.ToastUtils
import io.reactivex.disposables.CompositeDisposable

/**
 * 基础VM样式
 */
abstract class BaseVM : ViewModel(), LifecycleObserver {


    val disposable: CompositeDisposable by lazy { CompositeDisposable() }



    var extra: Bundle? = null

    val loading :MutableLiveData<Boolean> = MutableLiveData(false)


    fun toast(msg: String) {
        ToastUtils.center(msg)
    }

    open fun initData(extra: Bundle?) {
        this.extra = extra
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        disposable.clear()
        System.gc()
    }


}