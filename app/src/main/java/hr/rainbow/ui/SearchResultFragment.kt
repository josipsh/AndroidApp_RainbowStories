package hr.rainbow.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.databinding.FragmentSearchResultBinding
import hr.rainbow.domain.model.Story
import hr.rainbow.domain.model.UiHandler
import hr.rainbow.domain.view_models.SearchViewModel
import hr.rainbow.domain.view_models.StoryEventType
import hr.rainbow.ui.adapters.StoryAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var uiHandler: UiHandler

    private val viewModel: SearchViewModel by activityViewModels()
    private var stories: List<Story> = emptyList()
    private lateinit var adapter: StoryAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiHandler = context as UiHandler
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiHandler.showActionBar()

        subscribeForStoryObservable()
        adapter = StoryAdapter(
            requireContext(),
            stories,
            glide
        ) { event ->
            when (event) {
                is StoryEventType.PlayStory -> {
                    StoryViewerActivity.startActivity(requireContext(), event.storyId, event.title)
                }
                is StoryEventType.LikeClick -> {
                    viewModel.onLikeClickEvent(event.storyId, event.isLiked)
                }
            }
        }

        binding.rvSearchResult.adapter = adapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)

    }

    private fun subscribeForStoryObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.queryResult.collect {
                stories = it.storyItems

                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }

                if (stories.isEmpty() && !it.isLoading) {
                    toggleNotingToShowLabelVisibility(true)
                } else {
                    toggleNotingToShowLabelVisibility(false)
                }

                if (this@SearchResultFragment::adapter.isInitialized) {
                    adapter.loadStories(stories)
                }
            }
        }
    }

    private fun toggleNotingToShowLabelVisibility(shouldBeVisible: Boolean) {
        if (shouldBeVisible) {
            binding.etNothingToShow.visibility = View.VISIBLE
        } else {
            binding.etNothingToShow.visibility = View.GONE
        }
    }
}