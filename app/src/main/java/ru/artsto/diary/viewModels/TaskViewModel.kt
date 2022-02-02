package ru.artsto.diary.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.artsto.diary.classes.LocalRealmRepository
import ru.artsto.diary.realmModels.Task
import java.util.*
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val localRealmRepository: LocalRealmRepository
):ViewModel() {

    private val _destroy = MutableLiveData<Calendar>()
    val destroy: LiveData<Calendar> = _destroy

    val searchTask:MutableState<Task> = mutableStateOf(Task())
    val listTask:MutableList<Task> = mutableStateListOf()

    fun setOnDateChangeListen(calendar:Calendar){
        val list = localRealmRepository.createListAdapter(calendar = calendar)
        list.let {
            listTask.clear()
            listTask.addAll(list)
        }
    }

    fun replaceTask(task: Task){
        val res = localRealmRepository.replace(task = task)
        if (res){
            val cal = Calendar.getInstance()
            cal.timeInMillis = task.date_start.toLong()*10000
            destroyActivity(cal)
        }
    }

    fun onClickButton(task: Task) {
        val res = localRealmRepository.searchTask(task = task)
        if (res!=null) {
            task.id = res.id
            searchTask.value = task
        }else{
            localRealmRepository.create(task = task)
            val cal = Calendar.getInstance()
            cal.timeInMillis = task.date_start.toLong() * 10000
            destroyActivity(cal)
        }
    }

    private fun destroyActivity(cal: Calendar) {
        _destroy.value = cal
    }
}