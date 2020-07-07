@file:Suppress("UNCHECKED_CAST")

package com.zero.viewutils.utils.extends

import android.content.SharedPreferences
import android.text.Spannable
import android.text.SpannableString
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zero.viewutils.utils.other.RoundBackgroundColorSpan
import com.zero.viewutils.utils.other.StringConverter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jsoup.Jsoup
import java.io.File

/**
 * 带背景式时间字符串处理，详细见样例
 */
fun SpannableString.timeSpan(start: Int, end: Int) {
    this.setSpan(RoundBackgroundColorSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}


/**
 * 将相对地址的网页文本补充上前缀
 */
fun String.processImgSrc(baseUrl: String): String {
    val document = Jsoup.parse(this)
    document.setBaseUri(baseUrl)
    document.select("img[src]").forEach {
        val imgUrl = it.attr("src")
        if (imgUrl.trim {url-> url <= ' ' }.startsWith("/")) {
            it.attr("src", it.absUrl("src"))
        }
    }
    return document.html()
}

/**
 * 将字符串部分位置替换为星号
 */
fun String?.hideText(start: Int, end: Int): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    if (start >= end) {
        return this
    }
    if (this.length - 1 < end) {
        return this
    }
    var add = ""
    for (i in 0 until end - start)
        add += "*"
    return this.substring(0, start) + add + this.substring(end)
}

/**
 * 是否为手机号码
 */
fun String?.isMobile(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    return Regex("^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2,5,6,7]|17[0-8]|18[0-9]|19[1,3,5,8,9])\\d{8}$").matches(
        this
    )
}

/**
 * 是否为数字，两位小数
 */
fun String.isNumber(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    return Regex("^(([1-9]\\d*)|0)(\\.\\d{1,3})?$").matches(this)
}

/**
 * 将字符串数组转化为OKHttp图片上传格式
 */
fun ArrayList<String>?.convert(): MutableList<MultipartBody.Part> {
    val list: MutableList<MultipartBody.Part> = ArrayList()
    this?.let {
        for (text in this) {
            val file = File(text)
            val body = RequestBody.create(MediaType.parse("image/jpg"), file)
            val part = MultipartBody.Part.createFormData("files", file.name, body)
            list.add(part)
        }
    }
    return list
}

/**
 * 获取非常规json处理对象
 */
fun GsonBuilder.chinaJson(): Gson {
    return this.registerTypeAdapter(String::class.java, StringConverter()).create()
}

/**
 * 处理空字符串，若为空替换为默认值
 */
fun String?.nullString(defaultValue: String = ""): String {
    return if (this.isNullOrEmpty()) defaultValue else this
}

/**
 * 字符串拼接分隔符使用
 */
fun String?.addString(value: Any, split: String): String {
    return if (this.isNullOrEmpty()) value.toString() else this + split + value
}

/**
 * 保存数据到sp
 */
fun SharedPreferences.save(name: String, data: Any) {
    val edit = this.edit()
    when (data) {
        is String -> {
            edit.putString(name, data)
        }
        is Int -> {
            edit.putInt(name, data)
        }
        is Boolean -> {
            edit.putBoolean(name, data)
        }
        is Float -> {
            edit.putFloat(name, data)
        }
        is Long -> {
            edit.putLong(name, data)
        }
        is MutableSet<*> -> {
            edit.putStringSet(name, data as MutableSet<String>?)
        }
    }
    edit.apply()
}

/**
 * 分割字符串并转化为arrayList
 */
fun String?.splitToList(split: String): ArrayList<String> {
    if (this.isNullOrEmpty()) {
        return ArrayList()
    }
    return if (this.contains(split)) {
        val sp = this.split(split.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        ArrayList(listOf(*sp))
    } else {
        val list = ArrayList<String>()
        list.add(this)
        list
    }
}

/**
 * 列表转分隔字符串
 */
fun MutableList<String>.getListString(split: String =","): String {
    val s = ""
    this.forEach { s.addString(it,split ) }
    return s
}

/**
 * 列表转分隔字符串
 */
fun ArrayList<String>.getListString(split: String =","): String {
    val s = ""
    this.forEach { s.addString(it,split ) }
    return s
}



