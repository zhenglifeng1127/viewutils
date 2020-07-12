package com.zero.viewutils.net.observer

interface NetInterface<T> {

    fun success(body:T)

    fun error(msg:String,code:Int)

    fun loginFail(){

    }
}