package ru.artsto.diary.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.artsto.diary.TaskActivity
import ru.artsto.diary.databinding.FragmentListTaskBinding
import ru.artsto.diary.viewModels.MainViewModel
import java.util.*

class ListTaskFragment : Fragment() {

    private val vmMainView: MainViewModel by activityViewModels()
    private lateinit var bind: FragmentListTaskBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = FragmentListTaskBinding.inflate(inflater, container, false)
        bind.vmMain = vmMainView
        bind.lifecycleOwner = this

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if (result.resultCode== Activity.RESULT_OK) {
                //действие при получении ответа
                val calendar = result.data?.getSerializableExtra("calendar") as Calendar
                    vmMainView.setListAdapter(calendar = calendar)
            }
        }
        bind.fab.setOnClickListener {
            val intentTask = Intent(it.context, TaskActivity::class.java)
            //Toast.makeText(view.context,SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault()).format(vmMainView.date.value),Toast.LENGTH_LONG).show()
            intentTask.putExtra("date", vmMainView.date.value)
            launcher.launch(intentTask)
        }

        bind.calendarView.setOnDateChangeListener { cal_view, i, i2, i3 ->
            val calendar = Calendar.getInstance()
            calendar.set(i, i2, i3, 0, 0, 0)
            cal_view.date = calendar.timeInMillis
            vmMainView.setListAdapter(calendar)
        }
    }
}