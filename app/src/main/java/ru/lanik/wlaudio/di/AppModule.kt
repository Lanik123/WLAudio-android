package ru.lanik.wlaudio.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.lanik.wlaudio.ui.screen.main.MainViewModelFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideMainViewModelFactory(): MainViewModelFactory = MainViewModelFactory()
}