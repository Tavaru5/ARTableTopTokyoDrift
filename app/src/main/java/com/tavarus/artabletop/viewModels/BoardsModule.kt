package com.tavarus.artabletop.viewModels

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BoardsModule {

    @Singleton
    @Provides
    fun provideBoards(): BoardsViewModel {
        return BoardsViewModel()
    }
}

