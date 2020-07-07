package com.zero.viewutils.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.kingja.loadsir.core.LoadSir
import com.zero.viewutils.utils.AppUtils
import io.reactivex.plugins.RxJavaPlugins

/**
 * application 实际根据需求添加内容
 */
class BaseApplication :Application(){

    init {
        mContext = this
    }


    companion object{
        private lateinit var mContext:BaseApplication

//        private val pushService by lazy { PushServiceFactory.getCloudPushService() }
//
//        private val mWxApi: IWXAPI by lazy { WXAPIFactory.createWXAPI(mContext, Constants.WX_KEY, true) }

        private val sp:SharedPreferences by lazy { mContext.applicationContext.getSharedPreferences(AppUtils.getAppName(mContext), Context.MODE_PRIVATE) }

        fun getCon():BaseApplication{
            return mContext
        }

        fun getData():SharedPreferences{
            return sp
        }


//        fun getWxApi(): IWXAPI {
//            return mWxApi
//        }
//
//        fun getPush():CloudPushService{
//            return pushService
//        }

    }


    override fun onCreate() {
        super.onCreate()
        initArgs()
        RxJavaPlugins.setErrorHandler {
            //异常处理
        }

    }

    /**
     * 配置必要文件
     */
    private fun initArgs(){
//        LitePal.initialize(this)
//        mWxApi.registerApp(Constants.WX_KEY)
        initLoadSir()
        initUME()
//        initPushService(this)
    }

    private fun initUME(){
//        UMConfigure.init(
//            this,
//            "",
//            "android",
//            UMConfigure.DEVICE_TYPE_PHONE,
//            ""
//        )
    }

    private fun initLoadSir(){
        LoadSir.beginBuilder()
            .addCallback(NetCallback())
            .commit()
    }

    /**
     * 调整比例处理修改系统字体大小导致适配问题
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        newConfig.fontScale = 1.0f
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private fun initPushService(applicationContext: Context) {
//        this.createNotificationChannel()
//        PushServiceFactory.init(applicationContext)
//        pushService.register(applicationContext, object : CommonCallback {
//            override fun onSuccess(response: String) {
//
//            }
//
//            override fun onFailed(errorCode: String, errorMessage: String) {
//                //setConsoleText("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
//            }
//        })
//        HuaWeiRegister.register(this)
//
//        OppoRegister.register(
//            applicationContext,
//            "",
//            ""
//        )
//
//        MiPushRegister.register(applicationContext, "", "")
//
//        VivoRegister.register(this)

    }

    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val mNotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            // 通知渠道的id
//            val id = "1"
//            // 用户可以看到的通知渠道的名字.
//            val name = "notification channel"
//            // 用户可以看到的通知渠道的描述
//            val description = "notification description"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val mChannel = NotificationChannel(id, name, importance)
//            // 配置通知渠道的属性
//            mChannel.description = description
//            // 设置通知出现时的闪灯（如果 android 设备支持的话）
//            mChannel.enableLights(true)
//            mChannel.lightColor = Color.RED
//            // 设置通知出现时的震动（如果 android 设备支持的话）
//            mChannel.enableVibration(true)
//            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//            //最后在notificationmanager中创建该通知渠道
//            mNotificationManager.createNotificationChannel(mChannel)
//        }
    }
}