package com.tavarus.artabletop.modules

import com.tavarus.artabletop.daggerScopes.BoardScope
import com.tavarus.artabletop.models.BoardRepo
import dagger.Module
import dagger.Provides

@BoardScope
@Module
object BoardRepoModule {
    @BoardScope
    @Provides
    fun provideBoardRepo(): BoardRepo {
        return BoardRepo()
    }
}
