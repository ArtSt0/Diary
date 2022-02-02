package ru.artsto.diary

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import ru.artsto.diary.di.components.AppComponent
import ru.artsto.diary.di.components.DaggerAppComponent

class DiaryApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().application(this).build()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("myrealm.realm") //имя БД
            .allowWritesOnUiThread(true) //разрешение работать в потоке UI
            .allowQueriesOnUiThread(true)
            .schemaVersion(0) //версия БД
            .build()

        Realm.setDefaultConfiguration(config)
    }
}