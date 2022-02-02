package ru.artsto.diary.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.artsto.diary.MainActivity
import ru.artsto.diary.TaskActivity
import ru.artsto.diary.di.modules.AppModule
import ru.artsto.diary.di.modules.LocalRealmRepositoryModule
import ru.artsto.diary.di.modules.ViewModelFactoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelFactoryModule::class,
    LocalRealmRepositoryModule::class
])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(taskActivity: TaskActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}