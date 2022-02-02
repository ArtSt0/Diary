package ru.artsto.diary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ru.artsto.diary.databinding.ActivityMainBinding
import ru.artsto.diary.viewModels.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val vmMainView: MainViewModel by viewModels()
    private lateinit var bind:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DiaryApplication).appComponent.inject(this)
        ViewModelProvider(this, factory)[MainViewModel::class.java]

        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bind.vmMain = vmMainView
        bind.lifecycleOwner = this

        //загружаем данные из json в БД
        vmMainView.loadingJsonToDB(resources.openRawResource(R.raw.tasklist))
    }
}
