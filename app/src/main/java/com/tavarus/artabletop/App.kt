package com.tavarus.artabletop

import android.app.Application
import com.tavarus.artabletop.models.BoardRepoModule
import com.tavarus.artabletop.models.NavigationModule
import com.tavarus.artabletop.modules.CoreAppModule

class App: Application() {

    private val coreComponent: CoreComponent = DaggerCoreComponent.builder()
        .coreAppModule(CoreAppModule)
        .context(this)
        .build()

    fun provideCoreComponent(): CoreComponent = coreComponent

}
