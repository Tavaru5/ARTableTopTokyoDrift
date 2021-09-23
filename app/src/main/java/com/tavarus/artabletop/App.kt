package com.tavarus.artabletop

import android.app.Application
import com.facebook.soloader.SoLoader
import com.tavarus.artabletop.components.CoreComponent
import com.tavarus.artabletop.components.DaggerCoreComponent
import com.tavarus.artabletop.modules.CoreAppModule
import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.android.utils.FlipperUtils;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }
    }

    private val coreComponent: CoreComponent = DaggerCoreComponent.builder()
        .coreAppModule(CoreAppModule)
        .context(this)
        .build()

    fun provideCoreComponent(): CoreComponent = coreComponent

}
