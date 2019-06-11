package com.tavarus.artabletop.models

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object BoardRepoModule {
    @Singleton
    @Provides
    fun provideBoardRepo(): BoardRepo {
        return BoardRepo()
    }
}
