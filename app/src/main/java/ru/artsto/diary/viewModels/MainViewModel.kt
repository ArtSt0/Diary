package ru.artsto.diary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.artsto.diary.classes.LocalRealmRepository
import ru.artsto.diary.realmModels.Task
import java.io.InputStream
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localRealmRepository: LocalRealmRepository
): ViewModel(){

    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> = _date

    private val _listTask = MutableLiveData<List<Task>>()
    val listTask: LiveData<List<Task>> = _listTask

    init {
        setListAdapter(Calendar.getInstance())
    }
    //загружаем данные из Json в БД
    fun loadingJsonToDB(openRawResource: InputStream) {
        localRealmRepository.loadingJsonToDB(openRawResource)
    }

    fun setListAdapter(calendar: Calendar){
        setDate(calendar)
        val listTask =  localRealmRepository.createListAdapter(calendar = calendar)
        listTask.let {
            _listTask.value = it
        }
    }

    private fun setDate(calendar: Calendar) {
        _date.value = calendar.timeInMillis
    }
}