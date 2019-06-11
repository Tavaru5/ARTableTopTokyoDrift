package com.tavarus.artabletop.models

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavState @Inject constructor() {
    var currentScreen: MutableLiveData<NavStateEnum> = MutableLiveData()
    var screenArgs: MutableLiveData<Bundle> = MutableLiveData()

    init {
        currentScreen.value = NavStateEnum.HOME
    }

    fun pushToView(screen: NavStateEnum, args: Bundle = Bundle.EMPTY) {
        currentScreen.value = screen
        screenArgs.value = args
    }

    //How do I go back??
}
