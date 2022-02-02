package ru.artsto.diary

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ru.artsto.diary.compose.TaskScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ru.artsto.diary.databinding.ActivityTaskBinding
import ru.artsto.diary.viewModels.TaskViewModel
import java.util.*
import javax.inject.Inject

class TaskActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val vmTaskView: TaskViewModel by viewModels()
    private lateinit var bind: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as DiaryApplication).appComponent.inject(this)
        ViewModelProvider(this, factory)[TaskViewModel::class.java]

        bind = DataBindingUtil.setContentView(this, R.layout.activity_task)
        bind.vmTask = vmTaskView
        bind.lifecycleOwner = this
        bind.composeView.setContent {
            TaskScreen(
                vmTaskView::setOnDateChangeListen,
                vmTaskView.listTask,
                vmTaskView::onClickButton,
                vmTaskView.searchTask,
                vmTaskView::replaceTask
            )
        }
        vmTaskView.destroy.observe(this) {
            val resultIntent = Intent()
            resultIntent.putExtra("calendar",it)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        val date = intent.getLongExtra("date", Calendar.getInstance().timeInMillis)
        val cal  = Calendar.getInstance()
        cal.timeInMillis = date
        //Toast.makeText(this, SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault()).format(date),Toast.LENGTH_LONG).show()

        vmTaskView.setOnDateChangeListen(cal)
    }
}