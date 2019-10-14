package com.tavarus.artabletop

import android.content.Context
import com.tavarus.artabletop.fragments.BoardFragment
import com.tavarus.artabletop.fragments.HomeFragment
import com.tavarus.artabletop.models.BoardRepoModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BoardRepoModule::class])
interface BoardComponent {

    fun inject(homeFragment: HomeFragment)

    fun inject(boardFragment: BoardFragment)

    @Component.Builder
    interface Builder{
        fun build() : BoardComponent
        fun boardRepoModule(module: BoardRepoModule) : Builder

        @BindsInstance
        fun context(context: Context) : Builder
    }
}
