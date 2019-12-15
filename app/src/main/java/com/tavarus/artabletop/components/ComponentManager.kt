package com.tavarus.artabletop.components

import android.content.Context
import com.tavarus.artabletop.modules.BoardRepoModule
import com.tavarus.artabletop.modules.NavigationModule

class ComponentManager {
    var componentMap = mutableMapOf<String, DaggerComponent>()

    val BOARD_KEY = "BOARD_KEY"
    val NAV_KEY = "NAV_KEY"

    fun getOrCreateBoardComponent(context: Context, coreComponent: CoreComponent) : BoardComponent {
        return if (componentMap.containsKey(BOARD_KEY)) {
            componentMap[BOARD_KEY] as BoardComponent
        } else {
            val navComponent = getOrCreateNavComponent(context, coreComponent)
            val daggerBoard = navComponent.plus(BoardRepoModule)
            componentMap[BOARD_KEY] = daggerBoard
            daggerBoard
        }
    }

    fun getOrCreateNavComponent(context: Context, coreComponent: CoreComponent) : NavComponent {
        return if (componentMap.containsKey(NAV_KEY)) {
            componentMap[NAV_KEY] as NavComponent
        } else {
            val daggerNav = DaggerNavComponent.builder()
                .navigationModule(NavigationModule)
                .coreComponent(coreComponent)
                .context(context)
                .build()
            componentMap[NAV_KEY] = daggerNav
            daggerNav
        }
    }
}
