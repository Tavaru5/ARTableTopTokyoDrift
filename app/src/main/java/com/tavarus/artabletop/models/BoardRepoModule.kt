package com.tavarus.artabletop.models

import com.tavarus.artabletop.BoardScope
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
