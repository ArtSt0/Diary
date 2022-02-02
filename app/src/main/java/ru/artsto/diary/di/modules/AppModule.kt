package ru.artsto.diary.di.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import ru.artsto.diary.DiaryApplication

@Module
abstract class AppModule {
    @Binds
    abstract fun provideContext(application: DiaryApplication): Context
}
