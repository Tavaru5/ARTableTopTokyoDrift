package com.tavarus.artabletop.models

import androidx.lifecycle.MutableLiveData
import com.tavarus.artabletop.dataModels.NavActionEnum
import com.tavarus.artabletop.dataModels.NavStateEnum
import javax.inject.Inject

class NavState @Inject constructor() {
    var currentScreen: NavStateEnum = NavStateEnum.HOME
    var action: MutableLiveData<NavActionEnum> = MutableLiveData()
    var allowBackNav = true
    var selectedBoardIndex = 0
    var screenParams = mapOf<String, Any>()

    init {
        action.value = NavActionEnum.PUSH
    }

    fun pushToView(screen: NavStateEnum, allowBack: Boolean = true, newParams: Map<String, Any>? = null) {
        currentScreen = screen
        action.value = NavActionEnum.PUSH
        allowBackNav = allowBack
        if (newParams != null) {
            screenParams = newParams
        }
    }

    fun goBack() {
        allowBackNav = true
        action.value = NavActionEnum.BACK
    }
}
