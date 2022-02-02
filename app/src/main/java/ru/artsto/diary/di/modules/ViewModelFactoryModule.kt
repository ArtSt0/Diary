package ru.artsto.diary.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.artsto.diary.di.factory.ViewModelFactory
import ru.artsto.diary.di.qualifier.ViewModelKey
import ru.artsto.diary.viewModels.MainViewModel
import ru.artsto.diary.viewModels.TaskViewModel

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    abstract fun bindTaskViewModel(taskViewModel: TaskViewModel): ViewModel
}
