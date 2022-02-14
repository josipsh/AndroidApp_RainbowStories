package hr.rainbow.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import hr.rainbow.R
import hr.rainbow.databinding.TaskItemBinding

class DayTasksAdapter(
    private val context: Context,
    private val dayTasks: List<String>,
    var glide: RequestManager
) : RecyclerView.Adapter<DayTasksAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = TaskItemBinding.bind(itemView)

        fun bind(task: String) {
            glide.load(task).into(binding.ivTask)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = dayTasks[position]

        holder.itemView.animation = AnimationUtils.loadAnimation(
            context,
            R.anim.scheduler_scroll_anim
        )

        holder.bind(task)
    }

    override fun getItemCount(): Int = dayTasks.size

}