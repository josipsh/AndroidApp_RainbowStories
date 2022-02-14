package hr.rainbow.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import hr.rainbow.R
import hr.rainbow.databinding.DayItemBinding
import hr.rainbow.domain.model.DayScheduleState
import hr.rainbow.domain.view_models.SwipeEventType

class WeekSchedulerAdapter(
    private val context: Context,
    private val weekSchedule: MutableList<DayScheduleState>,
    var glide: RequestManager,
    private val onEvent: (SwipeEventType) -> Unit
) : RecyclerView.Adapter<WeekSchedulerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = DayItemBinding.bind(itemView)

        fun bind(data: DayScheduleState) {

            binding.tvHeaderDayName.text = data.daySchedule.dayName

            binding.rvDayTasks.adapter = DayTasksAdapter(context, data.daySchedule.tasks, glide)
            binding.rvDayTasks.layoutManager = LinearLayoutManager(context)

            if (data.isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }

            if (data.daySchedule.tasks.isEmpty() && !data.isLoading) {
                toggleNotingToShowLabelVisibility(true)
            } else {
                toggleNotingToShowLabelVisibility(false)
            }

            binding.btnArrowBackward.setOnClickListener { onEvent(SwipeEventType.SwipeBackward) }

            binding.btnArrowForward.setOnClickListener { onEvent(SwipeEventType.SwipeForward) }
        }

        private fun toggleNotingToShowLabelVisibility(shouldBeVisible: Boolean) {
            if (shouldBeVisible) {
                binding.etNothingToShow.visibility = View.VISIBLE
            } else {
                binding.etNothingToShow.visibility = View.GONE
            }
        }

    }

    fun loadDaySchedule(daySchedule: DayScheduleState, position: Int) {
        weekSchedule[position] = daySchedule
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.day_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daySchedule = weekSchedule[position]
        holder.bind(daySchedule)
    }

    override fun getItemCount(): Int = weekSchedule.size
}