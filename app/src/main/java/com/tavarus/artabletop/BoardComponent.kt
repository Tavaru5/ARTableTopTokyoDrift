package com.tavarus.artabletop

import com.tavarus.artabletop.fragments.BoardFragment
import com.tavarus.artabletop.fragments.HomeFragment
import com.tavarus.artabletop.models.BoardRepoModule
import dagger.Subcomponent

@BoardScope
@Subcomponent(modules = [BoardRepoModule::class])
interface BoardComponent: DaggerComponent {

    fun inject(homeFragment: HomeFragment)

    fun inject(boardFragment: BoardFragment)
}
