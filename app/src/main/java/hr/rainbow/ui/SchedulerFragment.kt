package hr.rainbow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.databinding.FragmentSchedulerBinding
import hr.rainbow.domain.model.DaySchedule
import hr.rainbow.domain.model.DayScheduleState
import hr.rainbow.domain.view_models.SchedulerViewModel
import hr.rainbow.domain.view_models.SwipeEventType
import hr.rainbow.ui.adapters.WeekSchedulerAdapter
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.collect
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class SchedulerFragment : Fragment() {

    private var _binding: FragmentSchedulerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var glide: RequestManager

    private val viewModel: SchedulerViewModel by viewModels()

    private lateinit var viewPager: ViewPager2
    private var weekSchedule: MutableList<DayScheduleState> = createEmptyWeekSchedule()

    private lateinit var adapter: WeekSchedulerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchedulerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeForObservable()

        viewPager = binding.Scheduler
        adapter = WeekSchedulerAdapter(
            this.requireContext(),
            weekSchedule,
            glide
        ) { event ->
            when (event) {
                is SwipeEventType.SwipeForward -> {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
                is SwipeEventType.SwipeBackward -> {
                    viewPager.currentItem = viewPager.currentItem - 1
                }
            }
        }
        binding.Scheduler.adapter = adapter

        viewPager.currentItem = LocalDate.now().dayOfWeek.value - 1
    }

    private fun subscribeForObservable() {
        subscribeMondayObservable()
        subscribeTuesdayObservable()
        subscribeWednesdayObservable()
        subscribeThursdayObservable()
        subscribeFridayObservable()
        subscribeSaturdayObservable()
        subscribeSundayObservable()
        subscribeErrorObservable()
    }

    private fun subscribeMondayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.monday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 0)
                }
            }
        }
    }

    private fun subscribeTuesdayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.tuesday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 1)
                }
            }
        }
    }

    private fun subscribeWednesdayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.wednesday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 2)
                }
            }
        }
    }

    private fun subscribeThursdayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.thursday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 3)
                }
            }
        }
    }

    private fun subscribeFridayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.friday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 4)
                }
            }
        }
    }

    private fun subscribeSaturdayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.saturday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 5)
                }
            }
        }
    }

    private fun subscribeSundayObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.sunday.collect {
                if (this@SchedulerFragment::adapter.isInitialized) {
                    adapter.loadDaySchedule(it, 6)
                }
            }
        }
    }

    private fun subscribeErrorObservable() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiEvents.collect {
                when (it) {
                    is UiEvents.ShowSnackBar -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT)
                            .setAction(it.action) {

                            }.show()
                    }
                }
            }
        }
    }

    private fun createEmptyWeekSchedule(): MutableList<DayScheduleState> {
        val items = mutableListOf<DayScheduleState>()
        for (index in 1..7) {
            items.add(
                DayScheduleState(
                    daySchedule = DaySchedule(
                        dayName = DayOfWeek.of(index).toString(),
                        tasks = emptyList()
                    ),
                    isLoading = false
                )
            )
        }
        return items
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}