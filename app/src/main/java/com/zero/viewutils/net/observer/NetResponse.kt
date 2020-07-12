package com.zero.viewutils.net.observer

import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetResponse<T> {
    var code = SUCCESS
    var body: T? = null
    var msg: String? = null
    var netCode = SUCCESS

    constructor(response: Response<T>) {
        body = response.body()
    }

    constructor(t: T) {
        body = t
    }

    constructor(throwable: Throwable) {
        body = null
        code = ERROR
        //对异常进行判断，这个是我随便写的一点，可以写一个工具类给封装起来
        msg = when (throwable) {
            is SocketTimeoutException -> "超时"
            is HttpException -> {
                netCode = throwable.code()
                when (throwable.code()) {
                    NOT_FIND -> "没有找到合适的资源"
                    SYSTEM_ERROR -> "服务器内部错误"
                    LOGIN_FAIL -> "登录状态失效"
                    else -> throwable.message()
                }
            }
            is JSONException -> "json解析异常"
            is UnknownHostException -> "网络异常"
            else -> throwable.message
        }
    }

    companion object{
        const val SUCCESS:Int = 200

        const val LOGIN_FAIL:Int = 401

        const val NOT_FIND = 404

        const val SYSTEM_ERROR = 500

        const val ERROR = -1



    }



}