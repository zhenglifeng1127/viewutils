package com.zero.viewutils.utils.singleton

import androidx.collection.ArrayMap
import com.zero.viewutils.base.BaseApplication
import com.zero.viewutils.base.L
import com.zero.viewutils.entity.ValueBean
import com.zero.viewutils.utils.JsonUtils

object CityManager {

    private const val fileName = "data.json"

    val data: MutableList<ValueBean> by lazy { ArrayList<ValueBean>() }

    init {
        getData()
    }

    private fun getData() {
        val strJson = getJsonFile()
        if (strJson.isEmpty()) {
            return
        }
        val parent = JsonUtils.json2Map(strJson)
        val p = JsonUtils.json2Map(parent["86"].toString())
        val it: Iterator<String> = p.keys.iterator()
        do {
            if (it.hasNext()) {
                val key = it.next()
                val city = JsonUtils.json2Map(p[key].toString())
                if(city.size ==1){
                    data.add(ValueBean(id = key.toInt(),name = p[key].toString(),value = key))
                }else{
                    saveData(city)
                }

            } else {
                break
            }
        } while (true)


    }

    private fun saveData(map: ArrayMap<String, Any>){
        val it: Iterator<String> = map.keys.iterator()
        do {
            if (it.hasNext()) {
                val key = it.next()
                data.add(ValueBean(id = key.toInt(),name = map[key].toString(),value = key))
            } else {
                break
            }
        } while (true)
    }


    private fun getJsonFile(): String {
        val buffer = StringBuffer()
        val assets = BaseApplication.getCon().assets
//        try {
//            val assets = BaseApplication.getCon().assets
//
//            val bf = BufferedReader(InputStreamReader(assets.open(fileName)));
//            bf.use {
//                var line: String
//                while (true){
//                    line = it.readLine()?:break
//                    buffer.append(line)
//                }
//            }
//            return buffer.toString();
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        assets.open(fileName).bufferedReader().readLines().forEach { buffer.append(it) }

        L.i("buffer",buffer.toString())
        return buffer.toString()
    }

    fun size():Int{
        return data.size
    }
}