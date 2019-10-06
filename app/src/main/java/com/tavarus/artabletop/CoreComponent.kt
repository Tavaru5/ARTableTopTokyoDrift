package com.tavarus.artabletop

import android.content.Context
import com.tavarus.artabletop.fragments.BoardFragment
import com.tavarus.artabletop.fragments.HomeFragment
import com.tavarus.artabletop.fragments.LoginFragment
import com.tavarus.artabletop.models.BoardRepoModule
import com.tavarus.artabletop.models.NavigationModule
import com.tavarus.artabletop.viewModels.HomeViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class, BoardRepoModule::class])
interface CoreComponent {

    fun inject(homeFragment: HomeFragment)

    fun inject(boardFragment: BoardFragment)

    fun inject(loginFragment: LoginFragment)

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder{
        fun build() : CoreComponent
        fun navigationModule(module: NavigationModule) : Builder
        fun boardRepoModule(module: BoardRepoModule) : Builder

        @BindsInstance
        fun context(context: Context) : Builder
    }
}
