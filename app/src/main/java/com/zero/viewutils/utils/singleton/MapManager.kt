package com.zero.viewutils.utils.singleton

import android.graphics.BitmapFactory
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.zero.viewutils.R
import com.zero.viewutils.base.BaseApplication
import com.zero.viewutils.utils.DateTimeUtils
import com.zero.viewutils.utils.other.Permission
import org.greenrobot.eventbus.EventBus

/**
 * 定位单例工具类
 */
object MapManager {

    private val locationClient: AMapLocationClient by lazy { AMapLocationClient(BaseApplication.getCon()) }

    private val locationOption by lazy { AMapLocationClientOption() }

    private var aMapLocation:AMapLocation? =null

    init {
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

    fun getData():AMapLocation?{
        return aMapLocation
    }


    fun getLocation() {
        RxUtils.getPermission(Permission.LOCATION)?.let { observable ->
            observable.subscribe { bool ->
                if(bool){
                    if(aMapLocation ==null) {
                        locationClient.startLocation()
                    }else{
                        aMapLocation?.let {
                            if(DateTimeUtils.between(it.time,DateTimeUtils.time(),DateTimeUtils.DATETIME.MINUTE)>1){
                                locationClient.startLocation()
                            }else{
                                EventBus.getDefault().post(it)
                            }
                        }
                    }
                }
            }
        }

    }


    fun addMaker(aMap: AMap, latLng: LatLng) {
        val markerOption = MarkerOptions()
        markerOption.draggable(false)
        markerOption.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(BaseApplication.getCon().resources, R.mipmap.ic_location_blue)
            )
        )
        val marker = aMap.addMarker(markerOption)
        marker.position = latLng
    }


    fun search(location:LatLng,listener:GeocodeSearch.OnGeocodeSearchListener){
        val search = GeocodeSearch(BaseApplication.getCon())
        val point = LatLonPoint(location.latitude, location.longitude)
        val query = RegeocodeQuery(point, 200f, GeocodeSearch.AMAP)
        search.getFromLocationAsyn(query)
        search.setOnGeocodeSearchListener(listener)
    }
}