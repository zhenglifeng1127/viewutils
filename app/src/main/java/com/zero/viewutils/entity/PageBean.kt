package com.zero.viewutils.entity

/**
 * 根据实际后端接口写法修改
 */
data class PageBean<T>(
    var count: Int = 0,
    var pageSize: Int = 0,
    var pageNumber: Int = 0,
    var status: Int =0 ,
    var message: String? = null,
    var list: MutableList<T>? = null
)
