package com.zero.viewutils.net.api

import com.zero.viewutils.entity.CommonPost
import com.zero.viewutils.entity.PageBean
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface WorkApiService {


    /**
     * 样例
     */
    @GET("/v1/Search/NewModel/Top/{Number}")
    fun getListRxJava(@Path("Number") level: Int): Observable<PageBean<String>>

    /**
     * 样例,如果用协程处理
     */
    @GET("/v1/Search/NewModel/Top/{Number}")
    suspend fun getList(@Path("Number") level: Int): PageBean<String>

}