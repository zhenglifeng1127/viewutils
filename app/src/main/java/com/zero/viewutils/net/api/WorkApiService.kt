package com.zero.viewutils.net.api

import com.zero.viewutils.entity.CommonPost
import com.zero.viewutils.entity.PageBean
import io.reactivex.Observable
import retrofit2.http.*

interface WorkApiService {


    /**
     * 样例
     */
    @GET("/v1/Search/NewModel/Top/{Number}")
    fun getList(@Path("Number") level: Int): Observable<PageBean<String>>



}