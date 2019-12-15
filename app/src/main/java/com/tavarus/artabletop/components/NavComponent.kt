package com.tavarus.artabletop.components

import android.content.Context
import com.tavarus.artabletop.daggerScopes.FeatureScope
import com.tavarus.artabletop.MainActivity
import com.tavarus.artabletop.fragments.LoginFragment
import com.tavarus.artabletop.modules.BoardRepoModule
import com.tavarus.artabletop.modules.NavigationModule
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(dependencies = [CoreComponent::class], modules = [NavigationModule::class])
interface NavComponent: DaggerComponent {

    fun inject(loginFragment: LoginFragment)

    fun inject(mainActivity: MainActivity)

    fun plus(boardRepoModule: BoardRepoModule): BoardComponent

    @Component.Builder
    interface Builder{
        fun build() : NavComponent
        fun navigationModule(module: NavigationModule) : Builder
        fun coreComponent(coreComponent: CoreComponent): Builder

        @BindsInstance
        fun context(context: Context) : Builder
    }
}
