package com.zero.viewutils.utils.extends

import androidx.lifecycle.ViewModel
import com.zero.viewutils.net.observer.NetInterface
import com.zero.viewutils.net.observer.NetResponse
import com.zero.viewutils.net.observer.NetResponse.Companion.ERROR
import com.zero.viewutils.net.observer.NetResponse.Companion.LOGIN_FAIL
import com.zero.viewutils.net.observer.NetResponse.Companion.SUCCESS

suspend fun <T> ViewModel.call(job: suspend () -> T): NetResponse<T> {
    return try {
        NetResponse(job())
    } catch (e: java.lang.Exception) {
        NetResponse(e)
    }
}

fun<T> NetResponse<T>.check(callback:NetInterface<T>){
        when(code){
            ERROR->{
                if(netCode!= LOGIN_FAIL)
                    callback.error(msg.nullString(),netCode)
                else
                    callback.loginFail()
            }
            SUCCESS->{
                body?.let {
                    callback.success(it)
                }
            }
        }
}