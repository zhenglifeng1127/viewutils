package com.zero.viewutils.net.dagger

import com.zero.viewutils.net.api.WorkApi
import com.zero.viewutils.net.api.WorkRxApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {


    @Singleton
    @Provides
    fun provideWorkApi(): WorkApi {
        return WorkApi()
    }

    @Singleton
    @Provides
    fun provideWorkRxApi(): WorkRxApi {
        return WorkRxApi()
    }
}