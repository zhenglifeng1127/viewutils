package com.zero.viewutils.net.observer

import com.zero.viewutils.base.L
import com.zero.viewutils.entity.PageBean
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * 仅供参考，实际根据接口而定
 */
abstract class PageObserver<T> : Observer<PageBean<T>> {

    private var compositeDisposable: CompositeDisposable? = null
    private var s: Disposable? = null
    private var observerSize: Int = 0


    constructor(compositeDisposable: CompositeDisposable) {
        this.compositeDisposable = compositeDisposable
    }

    constructor(compositeDisposable: CompositeDisposable, size: Int) {
        this.compositeDisposable = compositeDisposable
        observerSize = size
    }

    override fun onSubscribe(d: Disposable) {
        s = d
        s?.let {
            compositeDisposable?.add(it)
        }
    }

    override fun onNext(result: PageBean<T>) {
        check()
        L.i("code", result.status.toString())
        when (result.status) {

            200 -> {
                if(result.message.isNullOrEmpty()){
                    result.message = "获取数据成功"
                }
                if (result.list.isNullOrEmpty()) {
                    val empty: MutableList<T> = ArrayList()
                    onSuccess(empty,  result.message.toString(), result.count > result.pageNumber * result.pageSize)
                } else
                    result.list?.let {
                        onSuccess(
                            it,
                            result.message.toString(),
                            result.count > result.pageNumber * result.pageSize
                        )
                    }


            }
            401 -> onTokenFail()
            202 -> result.message?.let { onError(result.status.toString(), it) }
            else -> {
                if (result.message.isNullOrEmpty())
                    result.message = ""
                result.message?.let { onError(result.status.toString(), it) }
            }
        }

    }


    private fun check() {
        if (0 == observerSize - 1) {
            compositeDisposable!!.remove(s!!)
        } else {
            observerSize -= 1
        }
    }


    override fun onError(t: Throwable) {
        //失败处理
        check()
        L.i("error msg", t.toString())
        if (t is SocketTimeoutException || t is TimeoutException) {
            //超时
            onError("error on time", "连接超时，请稍候再试")
        } else if (t is ConnectException) {
            //连接失败
            onError("error on connect", "连接失败，请稍候再试")
        } else if (t is UnknownHostException) {
            //地址有误
            onError("error on host", "连接失败，请稍候再试")
        } else {
            //其他
            onError("error on other", "连接失败，请稍候再试")
        }
    }

    override fun onComplete() {

    }

    open fun onError(code: String, msg: String) {
        L.i(code, msg)
        //        ToastUtils.showCenterText(msg);
    }

    private fun onTokenFail() {
        //token超时具体根据实际CODE
    }

    abstract fun onSuccess(t: MutableList<T>, msg: String, isLoad: Boolean)


}
