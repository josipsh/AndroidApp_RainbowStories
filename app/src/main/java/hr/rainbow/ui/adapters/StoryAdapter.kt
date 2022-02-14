package hr.rainbow.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import hr.rainbow.R
import hr.rainbow.databinding.StoryItemBinding
import hr.rainbow.domain.model.Story
import hr.rainbow.domain.view_models.StoryEventType
import hr.rainbow.util.AZURE_SAS_KEY

class StoryAdapter(
    private val context: Context,
    private var stories: List<Story>,
    var glide: RequestManager,
    private val onEvent: (StoryEventType) -> Unit
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = StoryItemBinding.bind(itemView)

        fun bind(story: Story, onEvent: (StoryEventType) -> Unit) {
            binding.tvTitle.text = story.title
            binding.tvDescription.text = story.description
            glide.load("${story.thumbnailUrl}?$AZURE_SAS_KEY").into(binding.ivThumbnail)

            renderLikeCounter(story)
            renderFavoriteIcon(story)

            binding.btnLike.setOnClickListener {
                if (story.isLiked) {
                    --story.likesCount
                } else {
                    ++story.likesCount
                }
                story.isLiked = !story.isLiked
                renderFavoriteIcon(story)
                renderLikeCounter(story)

                onEvent(StoryEventType.LikeClick(storyId = story.id, isLiked = story.isLiked))

            }
            binding.btnPlay.setOnClickListener {
                onEvent(StoryEventType.PlayStory(story.id, story.title))
            }
        }

        private fun renderLikeCounter(story: Story) {
            val text = "${story.likesCount} ${context.getString(R.string.likes)}"
            binding.tvLikesCount.text = text
        }

        private fun renderFavoriteIcon(story: Story) {
            if (story.isLiked) {
                binding.btnLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.favorite_full
                    )
                )
            } else {
                binding.btnLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.favorite_outlined
                    )
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadStories(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.story_item,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]

        holder.itemView.animation = AnimationUtils.loadAnimation(
            context,
            R.anim.story_scroll_anim
        )

        holder.bind(story) { event ->
            onEvent(event)
        }
    }

    override fun getItemCount(): Int = stories.size
}