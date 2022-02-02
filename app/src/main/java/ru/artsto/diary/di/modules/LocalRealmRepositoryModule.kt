package ru.artsto.diary.di.modules

import dagger.Module
import dagger.Provides
import ru.artsto.diary.classes.LocalRealmRepository

@Module
class LocalRealmRepositoryModule {
    @Provides
    fun provideLocalRealmRepositoryModule(): LocalRealmRepository {
        return LocalRealmRepository()
    }
}