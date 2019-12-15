package com.tavarus.artabletop

import android.app.Application
import com.tavarus.artabletop.components.CoreComponent
import com.tavarus.artabletop.components.DaggerCoreComponent
import com.tavarus.artabletop.modules.CoreAppModule

class App: Application() {

    private val coreComponent: CoreComponent = DaggerCoreComponent.builder()
        .coreAppModule(CoreAppModule)
        .context(this)
        .build()

    fun provideCoreComponent(): CoreComponent = coreComponent

}
