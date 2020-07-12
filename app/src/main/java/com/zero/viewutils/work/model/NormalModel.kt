package com.zero.viewutils.work.model

import com.zero.viewutils.entity.PageBean
import com.zero.viewutils.net.api.WorkApi
import com.zero.viewutils.net.api.WorkRxApi
import com.zero.viewutils.net.dagger.ApiModule
import com.zero.viewutils.net.dagger.DaggerAppComponent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import javax.inject.Inject

/**
 * 注意 DaggerAppComponent在make project后会生成
 */
class NormalModel{
    lateinit var api: WorkApi @Inject set

    lateinit var api2: WorkRxApi @Inject set

    init {
        DaggerAppComponent.builder().apiModule(ApiModule()).build().inject(this)
    }

    fun getList(): Observable<PageBean<String>>{
        return api.getApi().getListRxJava(1)
            //此部分可以合并//此部分可以合并
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    suspend fun getListXc():PageBean<String>{
        return api2.getApi().getList(1)
    }
}