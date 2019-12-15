package com.tavarus.artabletop.components

import android.content.Context
import com.tavarus.artabletop.modules.CoreAppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreAppModule::class])
interface CoreComponent {

    fun componentManager(): ComponentManager
    fun context(): Context

    @Component.Builder
    interface Builder{
        fun build() : CoreComponent
        fun coreAppModule(module: CoreAppModule) : Builder

        @BindsInstance
        fun context(context: Context) : Builder
    }
}
