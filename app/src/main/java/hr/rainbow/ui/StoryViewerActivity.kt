package hr.rainbow.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.databinding.ActivityStoryViewerBinding
import hr.rainbow.domain.model.Card
import hr.rainbow.domain.view_models.StoryViewerEventType
import hr.rainbow.domain.view_models.StoryViewerViewModel
import hr.rainbow.ui.adapters.StoryViewerAdapter
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STORY_ID = "hr.rainbow.ui.story.id"
private const val TITLE = "hr.rainbow.ui.story.title"

@AndroidEntryPoint
class StoryViewerActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, storyId: Int, title: String) {
            ActivityCompat.startActivity(
                context,
                Intent(
                    context,
                    StoryViewerActivity::class.java
                ).apply {
                    putExtra(STORY_ID, storyId)
                    putExtra(TITLE, title)
                },
                null
            )
        }
    }

    private lateinit var binding: ActivityStoryViewerBinding

    private val viewModel: StoryViewerViewModel by viewModels()

    private var title: String = ""
    private var storyId: Int = -1
    private lateinit var viewPager: ViewPager2
    private var cards: List<Card> = emptyList()
    private lateinit var adapter: StoryViewerAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = intent.getStringExtra(TITLE) ?: ""
        storyId = intent.getIntExtra(STORY_ID, -1)
        if (storyId != -1) {
            viewModel.fetchCards(storyId)
        }

        subscribeForStoryObservable()
        subscribeForErrorObservable()

        supportActionBar?.hide()
        hideSystemUI()
        setupListeners()

        viewPager = binding.viewPager
        adapter = StoryViewerAdapter(
            this,
            cards,
            glide
        ) {
            when (it) {
                is StoryViewerEventType.SwipeForward -> {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
                is StoryViewerEventType.SwipeBackward -> {
                    viewPager.currentItem = viewPager.currentItem - 1
                }
                is StoryViewerEventType.ToggleOverlayControls -> {
                    toggleOverlayControls()
                }
            }
        }

        viewPager.adapter = adapter
        binding.etStoryName.text = title

    }

    private fun subscribeForStoryObservable() {
        lifecycleScope.launch {
            viewModel.cards.collect {
                cards = it.cards

                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }

                if (this@StoryViewerActivity::adapter.isInitialized) {
                    adapter.loadCards(cards)
                }
            }
        }
    }

    private fun subscribeForErrorObservable() {
        lifecycleScope.launch {
            viewModel.uiEvents.collect {
                when (it) {
                    is UiEvents.ShowSnackBar -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(it.action) {

                            }.show()
                    }
                }
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun toggleOverlayControls() {
        binding.overlayControls.isVisible = !binding.overlayControls.isVisible
    }

}