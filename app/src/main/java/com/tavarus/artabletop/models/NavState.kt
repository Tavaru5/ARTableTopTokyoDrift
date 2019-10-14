package com.tavarus.artabletop.models

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavState @Inject constructor() {
    var currentScreen: NavStateEnum = NavStateEnum.HOME
    var screenArgs: Bundle = Bundle.EMPTY
    var action: MutableLiveData<NavActionEnum> = MutableLiveData()
    var allowBackNav = true

    init {
        action.value = NavActionEnum.PUSH
    }

    fun pushToView(screen: NavStateEnum, allowBack: Boolean = true, args: Bundle = Bundle.EMPTY) {
        currentScreen = screen
        screenArgs = args
        action.value = NavActionEnum.PUSH
        allowBackNav = allowBack
    }

    fun goBack() {
        allowBackNav = true
        action.value = NavActionEnum.BACK
    }
}
