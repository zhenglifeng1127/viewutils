package com.zero.viewutils.work.vm

import androidx.lifecycle.viewModelScope
import com.zero.viewutils.base.BaseVM
import com.zero.viewutils.entity.PageBean
import com.zero.viewutils.net.observer.NetInterface
import com.zero.viewutils.net.observer.PageObserver
import com.zero.viewutils.utils.extends.call
import com.zero.viewutils.utils.extends.check
import com.zero.viewutils.work.model.NormalModel
import kotlinx.coroutines.launch

class NormalVM :BaseVM(){
    private val model by lazy { NormalModel() }

    override fun onCreate() {
        super.onCreate()
//        viewModelScope.launch {
//            call { model.getListXc() }.check(object :NetInterface<PageBean<String>>{
//                //成功
//                override fun success(body: PageBean<String>) {
//
//                }
//                //调用失败
//                override fun error(msg: String, code: Int) {
//                    super.error(msg, code)
//                }
//                //登录状态失效一般code 401
//                override fun loginFail() {
//
//                    super.loginFail()
//                }
//            })
//
//
//        }

        /**
         *
         */
//        model.getList()
//            .subscribe(object :PageObserver<String>(disposable){
//                override fun onSuccess(t: MutableList<String>, msg: String, isLoad: Boolean) {
//
//                }
//
//                override fun onError(code: String, msg: String) {
//                    super.onError(code, msg)
//
//                }
//
//            })
    }


}