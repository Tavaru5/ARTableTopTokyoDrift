package com.tavarus.artabletop.modules

import com.tavarus.artabletop.daggerScopes.FeatureScope
import com.tavarus.artabletop.models.NavState
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

