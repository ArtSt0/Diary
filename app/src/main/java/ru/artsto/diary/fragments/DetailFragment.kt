package ru.artsto.diary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ru.artsto.diary.databinding.FragmentDetailBinding
import ru.artsto.diary.viewModels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private val vmMainView: MainViewModel by activityViewModels()
    private lateinit var bind: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentDetailBinding.inflate(inflater, container, false)
        bind.vmMain = vmMainView
        bind.lifecycleOwner = this

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.tvName.text = args.taskModel.name
        bind.tvDate.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(args.taskModel.date_start.toLong()*10000))
        bind.tvTime.text = args.taskModel.hour
        bind.tvDescription.text = args.taskModel.description
    }
}