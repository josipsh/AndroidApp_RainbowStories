package hr.rainbow.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import hr.rainbow.R
import hr.rainbow.databinding.CardItemBinding
import hr.rainbow.domain.model.Card
import hr.rainbow.domain.model.TextFormat
import hr.rainbow.domain.view_models.StoryViewerEventType
import hr.rainbow.util.AZURE_SAS_KEY
import hr.rainbow.util.fireOnDoubleClick
import hr.rainbow.util.fireOnSecondClick

class StoryViewerAdapter(
    val context: Context,
    var cards: List<Card>,
    var glide: RequestManager,
    val onEvent: (StoryViewerEventType) -> Unit
) : RecyclerView.Adapter<StoryViewerAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding = CardItemBinding.bind(itemView)

        fun bind(card: Card) {
            binding.etTopText.text = card.topText
            binding.etBottomText.text = card.bottomText


            card.startImageUrl?.let {
                binding.ivStart.visibility = View.VISIBLE
                glide.load("${it}?$AZURE_SAS_KEY").into(binding.ivStart)
            } ?: run {
                binding.ivStart.visibility = View.GONE
            }
            card.centerImageUrl?.let {
                binding.ivCenter.visibility = View.VISIBLE
                glide.load("${it}?$AZURE_SAS_KEY").into(binding.ivCenter)
            } ?: run {
                binding.ivCenter.visibility = View.GONE
            }
            card.endImageUrl?.let {
                binding.ivEnd.visibility = View.VISIBLE
                glide.load("${it}?$AZURE_SAS_KEY").into(binding.ivEnd)
            } ?: run {
                binding.ivEnd.visibility = View.GONE
            }

            binding.etTopText.let {
                setFontFormat(it, card.textFormat)
                it.setTextColor(Color.parseColor(card.color))
                it.setAutoSizeTextTypeUniformWithConfiguration(
                    10,
                    card.textSize.value,
                    5,
                    TypedValue.COMPLEX_UNIT_DIP
                )
            }
            binding.etBottomText.let {
                setFontFormat(it, card.textFormat)
                it.setTextColor(Color.parseColor(card.color))
                it.setAutoSizeTextTypeUniformWithConfiguration(
                    10,
                    card.textSize.value,
                    5,
                    TypedValue.COMPLEX_UNIT_DIP
                )
            }

            binding.btnLeft.setOnClickListener {
                fireOnSecondClick { onEvent(StoryViewerEventType.ToggleOverlayControls) }
                fireOnDoubleClick { onEvent(StoryViewerEventType.SwipeBackward) }
            }

            binding.btnRight.setOnClickListener {
                fireOnSecondClick { onEvent(StoryViewerEventType.ToggleOverlayControls) }
                fireOnDoubleClick { onEvent(StoryViewerEventType.SwipeForward) }
            }
        }

        private fun setFontFormat(textView: TextView, textFormat: TextFormat) {
            when(textFormat){
                TextFormat.REGULAR ->{
                    textView.setTextAppearance(R.style.AppTheme_StoryViewerTextStyle_Regular)
                }
                TextFormat.BOLD ->{
                    textView.setTextAppearance(R.style.AppTheme_StoryViewerTextStyle_Bold)
                }
                TextFormat.ITALIC ->{
                    textView.setTextAppearance(R.style.AppTheme_StoryViewerTextStyle_Italic)
                }
                TextFormat.BOLD_ITALIC ->{
                    textView.setTextAppearance(R.style.AppTheme_StoryViewerTextStyle_BoldItalic)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadCards(newCards: List<Card>) {
        cards = newCards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size
}