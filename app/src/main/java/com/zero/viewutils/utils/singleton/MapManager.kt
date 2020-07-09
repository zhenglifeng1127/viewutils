package com.zero.viewutils.utils.singleton

import android.graphics.BitmapFactory
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.AMapUtils
import com.amap.api.maps2d.CameraUpdate
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.zero.viewutils.R
import com.zero.viewutils.base.BaseApplication
import com.zero.viewutils.utils.DateTimeUtils
import com.zero.viewutils.utils.DeviceUtils
import com.zero.viewutils.utils.extends.number2f
import com.zero.viewutils.utils.other.Permission
import org.greenrobot.eventbus.EventBus

/**
 * 定位单例工具类
 */
object MapManager {

    private val locationClient: AMapLocationClient by lazy { AMapLocationClient(BaseApplication.getCon()) }

    private val locationOption by lazy { AMapLocationClientOption() }

    private var aMapLocation: AMapLocation? = null

    init {
        //配置根据需求自行增减
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationOption.isOnceLocationLatest = true
        locationOption.isGpsFirst
        locationOption.httpTimeOut = 30000
        locationClient.setLocationOption(locationOption)
        locationClient.setLocationListener { aMap ->
            aMap?.let {
                if (it.errorCode == 0) {
                    locationClient.stopLocation()
                    aMapLocation = aMap
                    EventBus.getDefault().post(it)
                } else {
                    locationClient.stopLocation()
                }
            }
        }
    }

    fun getData(): AMapLocation? {
        return aMapLocation
    }


    fun getLocation() {
        RxUtils.getPermission(Permission.LOCATION)?.let { observable ->
            observable.subscribe { bool ->
                if (bool) {
                    if (aMapLocation == null) {
                        locationClient.startLocation()
                    } else {
                        aMapLocation?.let {
                            if (DateTimeUtils.between(
                                    it.time,
                                    DateTimeUtils.time(),
                                    DateTimeUtils.DATETIME.MINUTE
                                ) > 1
                            ) {
                                locationClient.startLocation()
                            } else {
                                EventBus.getDefault().post(it)
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 移动镜头到指定位置
     */
    fun move(aMap: AMap, location: LatLng,zoom:Float =  20f) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoom))
    }

    /**
     * 距离差计算,保留两位小数
     */
    fun distance(location: LatLng,locationTo: LatLng):String{
        return AMapUtils.calculateLineDistance(location, locationTo).number2f()
    }

    /**
     * 打开外部导航
     * @param latLng 目的地坐标(这里是高德坐标系，百度坐标系后续会自行转换调用)
     * @param name 目的地名称
     *
     */
    fun open(latLng: LatLng,name:String){
        DeviceUtils.openAMap(latLng.latitude,latLng.longitude,name)
    }


    /**
     * 添加maker
     */
    fun addMaker(aMap: AMap, latLng: LatLng, maker: Int = R.mipmap.ic_location_blue) {
        val markerOption = MarkerOptions()
        markerOption.draggable(false)
        markerOption.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(BaseApplication.getCon().resources, maker)
            )
        )
        val marker = aMap.addMarker(markerOption)
        marker.position = latLng
    }


    /**
     * 搜索
     */
    fun search(location: LatLng, listener: GeocodeSearch.OnGeocodeSearchListener) {
        val search = GeocodeSearch(BaseApplication.getCon())
        val point = LatLonPoint(location.latitude, location.longitude)
        val query = RegeocodeQuery(point, 200f, GeocodeSearch.AMAP)
        search.getFromLocationAsyn(query)
        search.setOnGeocodeSearchListener(listener)
    }
}