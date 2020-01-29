package com.tavarus.artabletop.models

import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.dataModels.NavActionEnum
import com.tavarus.artabletop.dataModels.NavStateEnum
import javax.inject.Inject

class NavState @Inject constructor() {
    var currentScreen: NavStateEnum = NavStateEnum.HOME
    var action: MutableLiveData<NavActionEnum> = MutableLiveData()
    var allowBackNav = true
    var selectedBoardID = ""

    init {
        action.value = NavActionEnum.PUSH
    }

    fun pushToView(screen: NavStateEnum, allowBack: Boolean = true) {
        currentScreen = screen
        action.value = NavActionEnum.PUSH
        allowBackNav = allowBack
    }

    fun goBack() {
        allowBackNav = true
        action.value = NavActionEnum.BACK
    }
}
