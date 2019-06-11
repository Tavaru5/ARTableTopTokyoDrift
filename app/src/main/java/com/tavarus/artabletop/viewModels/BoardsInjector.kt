package com.tavarus.artabletop.viewModels

import com.tavarus.artabletop.MainActivity
import com.tavarus.artabletop.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton


@Component//(modules = [BoardsModule::class, NavigationModule::class])
@Singleton
interface BoardsInjector {
    fun inject(homeFragment: HomeFragment)

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build() : BoardsInjector
    }

}
