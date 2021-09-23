package com.tavarus.artabletop.components

import com.tavarus.artabletop.daggerScopes.BoardScope
import com.tavarus.artabletop.board.BoardFragment
import com.tavarus.artabletop.editor.EditorFragment
import com.tavarus.artabletop.home.HomeFragment
import com.tavarus.artabletop.modules.BoardRepoModule
import dagger.Subcomponent

@BoardScope
@Subcomponent(modules = [BoardRepoModule::class])
interface BoardComponent: DaggerComponent {

    fun inject(homeFragment: HomeFragment)

    fun inject(boardFragment: BoardFragment)

    fun inject(editorFragment: EditorFragment)
}
