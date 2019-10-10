package com.tavarus.artabletop.models

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavState @Inject constructor() {
    var currentScreen: MutableLiveData<NavStateEnum> = MutableLiveData()
    var screenArgs: MutableLiveData<Bundle> = MutableLiveData()
    var action: MutableLiveData<NavActionEnum> = MutableLiveData()
    private var screenStack: MutableList<NavStateEnum> = mutableListOf()

    init {
        currentScreen.value = NavStateEnum.HOME
    }

    fun pushToView(screen: NavStateEnum, args: Bundle = Bundle.EMPTY) {
        currentScreen.value = screen
        screenArgs.value = args
        action.value = NavActionEnum.PUSH
        screenStack.add(screen)
    }

    fun replaceWithView(screen: NavStateEnum, args: Bundle = Bundle.EMPTY) {
        currentScreen.value = screen
        screenArgs.value = args
        action.value = NavActionEnum.REPLACE
    }

    //TODO: We need a back action
}
