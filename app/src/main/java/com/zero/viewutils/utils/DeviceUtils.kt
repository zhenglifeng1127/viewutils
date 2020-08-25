package com.zero.viewutils.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.zero.viewutils.R
import com.zero.viewutils.utils.FileProviderUtils.Companion.getFileUri
import com.zero.viewutils.utils.FileProviderUtils.Companion.grantUriPermission
import com.zero.viewutils.utils.singleton.AppManager
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

class DeviceUtils {

    companion object {

        /**
         * 屏幕宽度
         *
         * @return
         */
        fun getWidthPixels(context: Context): Int {
            return context.resources.displayMetrics.widthPixels  // 屏幕宽度（像素）
        }

        /**
         * 屏幕高度
         *
         * @return
         */
        fun getHeightPixels(context: Context): Int {
            return context.resources.displayMetrics.heightPixels
        }

        /**
         * 屏幕分辨率
         *
         * @return 返回分辨率值
         */
        fun getDensity(context: Context): Float {
            return context.resources.displayMetrics.density
        }

        /**
         * 屏幕DPI
         *
         * @return
         */
        fun getDensityDpi(context: Context): Int {
            return context.resources.displayMetrics.densityDpi
        }

        /**
         * 获取系统语言类型
         *
         * @return
         */
        fun getSystemLanguage(): String {
            return Locale.getDefault().language
        }

        /**
         * @return
         */
        fun getSystemLanguageList(): Array<Locale> {
            return Locale.getAvailableLocales()
        }

        /**
         * Android版本
         *
         * @return
         */
        fun getSystemVersion(): String {
            return Build.VERSION.RELEASE
        }

        /**
         * 机型版本参数
         *
         * @return
         */
        fun getSystemModel(): String {
            return Build.MODEL
        }

        /**
         * 系统定制商名称
         *
         * @return
         */
        fun getDeviceBrand(): String {
            return Build.BRAND
        }

        /**
         * 快捷操作相机拍照
         * @param context 上下文
         * @param filePath 存储路径
         * @param req 请求码
         */
        fun openCamera(context: Activity, filePath: String, req: Int) {
            val imageUri = getFileUri(context, filePath) ?: return
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            grantUriPermission(context, cameraIntent, imageUri)
            ActivityUtils.openForResult(cameraIntent, req)
        }

        /**
         * 快捷操作相册
         * @param context 上下文
         * @param req 请求码
         */
        fun openGallery(context: Activity, req: Int) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            ActivityUtils.openForResult(intent, req)

        }

        /**
         * 快捷裁剪
         * @param context 上下文
         * @param photoPath 图片路径
         * @param picPath 裁剪输出路径
         * @param width 输出宽
         * @param height 输出高
         * @param req 请求码
         */
        fun pickPhoto(
            context: Activity,
            photoPath: String,
            picPath: String,
            width: Int,
            height: Int,
            req: Int
        ) {
            val imageUri = getFileUri(context, photoPath) ?: return
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(imageUri, "image/*")
            grantUriPermission(context, intent, imageUri)
            // 设置裁剪
            intent.putExtra("crop", "true")
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", width)
            intent.putExtra("outputY", height)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.putExtra("noFaceDetection", true)
            intent.putExtra("return-data", false)
            //裁剪之后，保存在裁剪文件中，关键
            val imageCropUri = getFileUri(context, picPath) ?: return
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri)
            grantUriPermission(context, intent, imageCropUri)
            ActivityUtils.openForResult(intent, req)
        }

        /**
         * GPS是否开启
         * @param context 上下文
         * @return
         */
        fun isGpsEnable(context: Context): Boolean {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            return gps || network
        }

        /**
         * 获取蓝牙地址
         * @return 蓝牙地址
         */
        fun getBluetoothAddress(): String? {
            try {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val field = bluetoothAdapter.javaClass.getDeclaredField("mService")
                // 参数值为true，禁用访问控制检查
                field.isAccessible = true
                val bluetoothManagerService = field.get(bluetoothAdapter) ?: return null
                val method = bluetoothManagerService.javaClass.getMethod("getAddress")
                val address = method.invoke(bluetoothManagerService)
                return if (address != null && address is String) {
                    address
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        fun openTel(activity: Activity, phone: String) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }

        /*
         *用途:根据部分特征参数设备信息来判断是否为模拟器
         *返回:true 为模拟器
         */
        @SuppressLint("DefaultLocale")
        fun isFeatures(): Boolean {
            return (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.toLowerCase().contains("vbox")
                    || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT)
        }

        /*
     *用途:根据CPU是否为电脑来判断是否为模拟器
     *返回:true 为模拟器
     */
        fun checkIsNotRealPhone(): Boolean {
            val cpuInfo = readCpuInfo()
            return cpuInfo.contains("intel") || cpuInfo.contains("amd")
        }

        /*
         *用途:根据CPU是否为电脑来判断是否为模拟器(子方法)
         *返回:String
         */
        @SuppressLint("DefaultLocale")
        fun readCpuInfo(): String {
            var result = ""
            try {
                val args = arrayOf("/system/bin/cat", "/proc/cpuinfo")
                val cmd = ProcessBuilder(*args)

                val process = cmd.start()
                val sb = StringBuffer()
                var readLine: String? = ""
                val responseReader = BufferedReader(InputStreamReader(process.inputStream, "utf-8"))
                while (readLine != null) {
                    readLine = responseReader.readLine()
                    sb.append(readLine)
                }
                responseReader.close()
                result = sb.toString().toLowerCase()
            } catch (ex: Exception) {
            }

            return result
        }

        /*
     *用途:检测模拟器的特有文件
     *返回:true 为模拟器
     */
        private val known_pipes = arrayOf("/dev/socket/qemud", "/dev/qemu_pipe")

        fun checkPipes(): Boolean {
            for (i in known_pipes.indices) {
                val pipes = known_pipes[i]
                val qemuSocket = File(pipes)
                if (qemuSocket.exists()) {
                    Log.v("Result:", "Find pipes!")
                    return true
                }
            }
            Log.i("Result:", "Not Find pipes!")
            return false
        }



        //打开地图
        /**
         * 打开高德地图（公交出行，起点位置使用地图当前位置）
         *
         * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
         *
         * @param dlat  终点纬度
         * @param dlon  终点经度
         * @param dname 终点名称
         */
        fun openAMap(dlat: Double, dlon: Double, dname: String) {
            if (AppUtils.isInstallApp(
                    "com.autonavi.minimap",
                    AppManager.endOfStack()
                )
            ) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setPackage("com.autonavi.minimap")
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(
                    "androidamap://route?sourceApplication=" + R.string.app_name
                            + "&sname=我的位置&dlat=" + dlat
                            + "&dlon=" + dlon
                            + "&dname=" + dname
                            + "&dev=0&m=0&t=0"
                )
                AppManager.endOfStack().startActivity(intent)
            } else {
                val data = PointUtils.gcj02_To_Bd09(dlat, dlon)
                openBaiduMap(data.wgLat, data.wgLon, dname)
            }
        }

        private fun openBaiduMap(dlat: Double, dlon: Double, dname: String) {
            if (AppUtils.isInstallApp(
                    "com.baidu.BaiduMap",
                    AppManager.endOfStack()
                )
            ) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(
                    ("baidumap://map/direction?origin=我的位置&destination=name:"
                            + dname
                            + "|latlng:" + dlat + "," + dlon
                            + "&mode=driving&sy=3&index=0&target=1")
                )
                AppManager.endOfStack().startActivity(intent)
            } else {
                val data = PointUtils.bd09_To_Gcj02(dlat, dlon)
                openTentcentMap(data.wgLat, data.wgLon, dname)
            }
        }

        /**
         * 调用腾讯地图
         *
         */
        fun openTentcentMap(dlat: Double, dlon: Double, dname: String) {
            if (AppUtils.isInstallApp(
                    "com.baidu.BaiduMap",
                    AppManager.endOfStack()
                )
            ) {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                intent.data = Uri.parse(
                    ("qqmap://map/routeplan?" +
                            "type=drive" +
                            "&from=" +
                            "&fromcoord=" +
                            "&to=" + dname +
                            "&tocoord=" + dlat + "," + dlon +
                            "&policy=0" +
                            "&referer=appName")
                )
                AppManager.endOfStack().startActivity(intent)
            } else {
                openBrowserMap(dlat, dlon, dname)
            }
        }


        /**
         * 打开网页版 导航
         * 不填我的位置，则通过浏览器定未当前位置
         *
         */
        fun openBrowserMap(dlat: Double, dlon: Double, dname: String) {

            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(
                ("http://uri.amap.com/navigation?" +
                        "from=&to=" + dlon + "," + dlat + "," + dname +
                        "&mode=car&policy=1&src=mypage&coordinate=gaode&callnative=0")
            )
            AppManager.endOfStack().startActivity(intent)

        }
    }




}