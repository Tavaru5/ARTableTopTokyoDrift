package com.tavarus.artabletop

import android.content.Context
import com.tavarus.artabletop.fragments.LoginFragment
import com.tavarus.artabletop.models.BoardRepoModule
import com.tavarus.artabletop.models.NavigationModule
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
