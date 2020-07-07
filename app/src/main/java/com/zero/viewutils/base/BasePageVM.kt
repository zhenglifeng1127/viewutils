package com.zero.viewutils.base

import androidx.lifecycle.MutableLiveData
import com.zero.viewutils.entity.CommonPost

/**
 * 基础类，分页列表使用，具体参照样例
 */
abstract class BasePageVM<T,E> :BaseVM(){

    var pagePost: CommonPost<E> = CommonPost(data = null)

    val list: MutableLiveData<MutableList<T>> = MutableLiveData()

    val load:MutableLiveData<Boolean> = MutableLiveData()

    abstract fun getPageList()

    fun setListData(data:MutableList<T>,isLoad:Boolean){
        pagePost.isCanLoad = isLoad
        list.value = data
        load.value = false
        loading.value = false
    }

    fun loadError(){
        if (pagePost.page > 1) {
            pagePost.page = pagePost.page - 1
        }
        load.value = false
        loading.value = false
    }

    fun loadMore(){
        pagePost.page =  pagePost.page + 1
        pagePost.isLoad = true
        getPageList()
    }

    fun refresh(){
        pagePost.page = 1
        pagePost.isLoad = false
        getPageList()
    }

    fun changeStatus(status:Int){
        pagePost.status = status
        refresh()
    }

}