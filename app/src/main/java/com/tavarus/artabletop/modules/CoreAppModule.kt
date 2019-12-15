package com.tavarus.artabletop.modules

import com.tavarus.artabletop.components.ComponentManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CoreAppModule {

    @Provides
    @Singleton
    fun provideComponentManager() = ComponentManager()
}
