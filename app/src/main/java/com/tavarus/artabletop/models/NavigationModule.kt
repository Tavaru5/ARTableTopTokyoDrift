package com.tavarus.artabletop.models

import com.tavarus.artabletop.FeatureScope
import dagger.Module
import dagger.Provides

@FeatureScope
@Module
object NavigationModule {

    @FeatureScope
    @Provides
    fun provideNavigation(): NavState {
        return NavState()
    }
}

