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

    init {
        currentScreen.value = NavStateEnum.HOME
        action.value = NavActionEnum.PUSH
    }

    fun pushToView(screen: NavStateEnum, args: Bundle = Bundle.EMPTY) {
        currentScreen.value = screen
        screenArgs.value = args
        action.value = NavActionEnum.PUSH
    }

    fun replaceWithView(screen: NavStateEnum, args: Bundle = Bundle.EMPTY) {
        currentScreen.value = screen
        screenArgs.value = args
        action.value = NavActionEnum.REPLACE
    }

    fun goBack() {
        action.value = NavActionEnum.BACK
    }
}
