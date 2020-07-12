package com.zero.viewutils.net.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ocsa.carshop.net.base.SSLSocketClient
import com.ocsa.carshop.net.base.TokenHeadInterceptor
import com.zero.viewutils.base.Constants
import com.zero.viewutils.base.L
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

class WorkRxApi {

    private val workApiService by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            //在这里可以打印请求的具体信息

            L.i("okHttp", "log: message == $message")

        }

        //http 数据log  日志中打印出http请求和相应数据
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .proxy(Proxy.NO_PROXY)
            .connectTimeout(Config.NETWORK.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Config.NETWORK.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Config.NETWORK.READ_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(TokenHeadInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)//断网重连
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager()[0])
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        return@lazy retrofit.create(WorkApiService::class.java)
    }

    fun getApi(): WorkApiService {
        return workApiService
    }
}