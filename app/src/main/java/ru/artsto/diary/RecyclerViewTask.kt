package ru.artsto.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.artsto.diary.databinding.RecyclerViewTaskBinding
import ru.artsto.diary.fragments.ListTaskFragmentDirections
import ru.artsto.diary.models.TaskModel
import ru.artsto.diary.realmModels.Task
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewTask(var posts: List<Task>): RecyclerView.Adapter<RecyclerViewTask.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(posts[position],holder)
    }

    override fun getItemCount(): Int {
        return posts.size
    }


    class TaskHolder(private val binding: RecyclerViewTaskBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, holder: TaskHolder) {

            var tvTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(task.date_start.toLong()*10000)
            tvTime += " - "
            tvTime += SimpleDateFormat("HH:mm", Locale.getDefault()).format(task.date_finish.toLong()*10000)

            binding.tvTime.text = tvTime
            if (task.id>0){
                binding.tvName.visibility = View.VISIBLE
                binding.tvName.text = task.name
            }else{
                binding.tvName.visibility = View.GONE
            }


            //val activity = holder.itemView.context as Activity
            binding.task = task
            binding.rvClick = object :OnItemClickTask{
                override fun onItemTaskClick(task: Task) {
                    val taskModel = TaskModel(
                        id = task.id,
                        name = task.name,
                        description = task.description,
                        date_start = task.date_start,
                        date_finish = task.date_finish,
                        index = task.index,
                        hour = task.hour,
                    )
                val action = ListTaskFragmentDirections.actionListTaskFragmentToDetailFragment(taskModel = taskModel)
                holder.binding.root.findNavController().navigate(action)
                }
            }
        }

        companion object{
            fun from(parent: ViewGroup): TaskHolder {
                val binding = DataBindingUtil.inflate<RecyclerViewTaskBinding>(
                    LayoutInflater.from(parent.context), R.layout.recycler_view_task, parent, false)
                return TaskHolder(binding)
            }
        }
    }

    interface OnItemClickTask{
        fun onItemTaskClick(task:Task)
    }
}