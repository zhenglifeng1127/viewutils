package com.zero.viewutils.work.vm

import com.zero.viewutils.base.BasePageVM
import com.zero.viewutils.net.observer.PageObserver
import com.zero.viewutils.work.model.NormalModel

/**
 * 参数1为列表接口实体，参数2为补充类实体
 */
class NormalPageVM :BasePageVM<String,String>(){

    private val model by lazy { NormalModel() }

    override fun onCreate() {
        super.onCreate()

        //最初调用一次,调用前可以自行调整传入值
        getPageList()
    }

    /**
     * 列表接口调用样例
     */
    override fun getPageList() {
        //接口调用
        model.getList()
            .subscribe(object :PageObserver<String>(disposable){
                override fun onSuccess(t: MutableList<String>, msg: String, isLoad: Boolean) {
                    setListData(t,isLoad)
                }

                override fun onError(code: String, msg: String) {
                    super.onError(code, msg)
                    toast(msg)
                    loadError()
                }

            })
    }



}