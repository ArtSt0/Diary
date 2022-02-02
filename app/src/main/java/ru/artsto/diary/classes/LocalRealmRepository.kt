package ru.artsto.diary.classes

import io.realm.Realm
import io.realm.kotlin.where
import ru.artsto.diary.realmModels.Task
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

open class LocalRealmRepository {

    fun searchTask(task: Task): Task? {
        val realm = Realm.getDefaultInstance()
        val query = realm.where<Task>()
                .equalTo("date_start", task.date_start)
                .equalTo("date_finish", task.date_finish)
                .findFirst()
        query?.load()
        return query
    }

    //добавить
    fun create(task: Task){
        val realm = Realm.getDefaultInstance()
        var idTask = realm.where<Task>().max("id")
        idTask = if (idTask==null){ 1 }else{ idTask.toInt()+1 }
        task.id = idTask
        realm.executeTransaction { realmTransaction->
            realmTransaction.insert(task)
        }
        realm.close()
    }

    //перезапись
    fun replace(task: Task): Boolean {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction{
            val query = searchTask(task = task)
            query?.name = task.name
            query?.description = task.description
        }
        realm.close()
        return true
    }

    fun loadingJsonToDB(openRawResource: InputStream) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction{it->
            try {
                //если объект создан, обновить его(необходим PrimaryKey)
                it.createOrUpdateAllFromJson(Task::class.java, fileToString(openRawResource))
            }catch (e:Exception){
                e.message
            }
        }
        realm.close()
    }

    private fun fileToString(inputStream: InputStream): String {
        val baos = ByteArrayOutputStream()
        var i = inputStream.read()
        while (i != -1) {
            baos.write(i)
            i = inputStream.read()
        }
        inputStream.close()
        return baos.toString()
    }

    fun createListAdapter(calendar: Calendar): MutableList<Task> {

        val varList = mutableListOf<Task>()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        for (i in 0..23){
            val calendar1 = Calendar.getInstance()
            calendar1.set(year, month, dayOfMonth, i, 0, 0)

            val calendar2 = Calendar.getInstance()
            calendar2.set(year, month, dayOfMonth, i+1,0, 0)

            val timestamp1 = calendar1.timeInMillis/10000L
            val timestamp2 = calendar2.timeInMillis/10000L

            val task = Task(
                date_start = timestamp1.toString(),
                date_finish = timestamp2.toString(),
                index = i
            )
            task.hour = timeToString(timestamp1 = timestamp1.toString(), timestamp2 = timestamp2.toString())
            val realm = Realm.getDefaultInstance()
            //поиск по БД
            val query = realm.where<Task>()
                .equalTo("date_start", timestamp1.toString())
                .equalTo("date_finish", timestamp2.toString())
                .findFirst()

            query?.let {
                task.id = it.id
                task.name = it.name
                task.description = it.description
            }
            realm.close()
            /*
            //поиск по json данных
            tasks?.forEach {
                if (timestamp1.compareTo(it.date_start!!.toLong())==0 && timestamp2.compareTo(it.date_finish!!.toLong())==0)
                {
                    task.id = it.id
                    task.description = it.description
                    task.name = it.name
                }
            }*/

            varList.add(task)
        }
        return varList
    }

    private fun timeToString(timestamp1: String, timestamp2: String): String {
        var tvTime = ""

        if (timestamp1!="" && timestamp2!="") {
            tvTime += SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(timestamp1.toLong() * 10000)
            tvTime += " - "
            tvTime += SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(timestamp2.toLong() * 10000)
        }
        return tvTime
    }
}