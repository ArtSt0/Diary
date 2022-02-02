package ru.artsto.diary.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.artsto.diary.RecyclerViewTask
import ru.artsto.diary.realmModels.Task

object BindingAdapters {
        @JvmStatic
        @BindingAdapter("android:adapterTask")
        fun getTablesListAdapter(recycler: RecyclerView, posts: List<Task>?) {
            if (posts!=null)
            {
                recycler.setHasFixedSize(true)
                recycler.layoutManager = LinearLayoutManager(recycler.context)
                val cus = RecyclerViewTask(posts)
                recycler.adapter = cus
            }
        }
}