package hr.rainbow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.databinding.FragmentFavoriteStoriesBinding
import hr.rainbow.domain.model.Story
import hr.rainbow.domain.view_models.StoryEventType
import hr.rainbow.domain.view_models.StoryViewModel
import hr.rainbow.ui.adapters.StoryAdapter
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteStoriesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteStoriesBinding

    private val viewModel: StoryViewModel by viewModels()
    private var stories: List<Story> = emptyList()
    private lateinit var adapter: StoryAdapter


    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteStoriesBinding.inflate(inflater, container, false)
        viewModel.fetchFavoritesStories()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        subscribeForDataObservable()
        subscribeForErrorObservable()

        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(this.context)

    }

    private fun subscribeForDataObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.favoritesStories.collect {
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

                if (this@FavoriteStoriesFragment::adapter.isInitialized) {
                    adapter.loadStories(stories)
                }
            }
        }
    }

    private fun subscribeForErrorObservable() {
        lifecycleScope.launchWhenStarted {
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

    private fun toggleNotingToShowLabelVisibility(shouldBeVisible: Boolean) {
        if (shouldBeVisible) {
            binding.etNothingToShow.visibility = View.VISIBLE
        } else {
            binding.etNothingToShow.visibility = View.GONE
        }
    }
}