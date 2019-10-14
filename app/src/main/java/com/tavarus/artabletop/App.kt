package com.tavarus.artabletop

import android.app.Application
import com.tavarus.artabletop.CoreComponent
import com.tavarus.artabletop.DaggerCoreComponent
import com.tavarus.artabletop.models.BoardRepoModule
import com.tavarus.artabletop.models.NavigationModule

class App: Application() {

    private val coreComponent: CoreComponent = DaggerCoreComponent.builder()
        .navigationModule(NavigationModule)
        .context(this)
        .build()

    private val boardComponent: BoardComponent = DaggerBoardComponent.builder()
        .boardRepoModule(BoardRepoModule)
        .context(this)
        .build()

    fun provideCoreComponent(): CoreComponent = coreComponent

    fun provideBoardComponent(): BoardComponent = boardComponent

}
