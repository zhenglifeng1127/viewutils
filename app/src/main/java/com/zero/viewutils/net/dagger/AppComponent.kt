package com.zero.viewutils.net.dagger

import com.zero.viewutils.work.model.NormalModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(model: NormalModel)
}