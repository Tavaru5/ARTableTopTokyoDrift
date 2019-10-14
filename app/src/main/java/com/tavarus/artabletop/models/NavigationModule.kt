package com.tavarus.artabletop.models

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NavigationModule {

    @Singleton
    @Provides
    fun provideNavigation(): NavState {
        return NavState()
    }
}

