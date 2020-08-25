package com.zero.viewutils.utils

import androidx.collection.ArrayMap
import com.zero.viewutils.base.L
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 迭代法解析json,已知结构下使用，一般解析第三方库即可
 */
class JsonUtils {
    companion object{

        //非增式添加list转换
        fun json2List(json: String?): MutableList<ArrayMap<String, Any>> {
            val list: MutableList<ArrayMap<String, Any>> = ArrayList()
            try {
                json?.let {
                    val jsonArray = JSONArray(it)
                    var i = 0
                    while (jsonArray.length() > i) {
                        val obj = jsonArray.getJSONObject(i)
                        val keyIterator: Iterator<*> = obj.keys()
                        val map: ArrayMap<String, Any> = ArrayMap()
                        while (keyIterator.hasNext()) {
                            val key = keyIterator.next().toString()
                            map[key] = if (obj.opt(key) == null) "" else obj.opt(key)
                        }
                        list.add(map)
                        i++
                    }
                }
            } catch (e: JSONException) {
                L.e("json2List", "json is error")
            }
            return list
        }

        fun json2Map(json: String?): ArrayMap<String, Any> {
            val m = ArrayMap<String, Any>()
            try {
                json?.let {
                    val obj = JSONObject(it)
                    val keyIterator: Iterator<*> = obj.keys()
                    while (keyIterator.hasNext()) {
                        val key = keyIterator.next() as String
                        m[key] = if (obj.opt(key) == null) "" else obj.opt(key)
                    }
                }
            } catch (e: JSONException) {
                L.i("error of json")
            }
            return m
        }
    }
}