package hr.rainbow.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.rainbow.R
import hr.rainbow.databinding.SearchSuggestionItemBinding
import hr.rainbow.domain.model.SuggestionItem

class SearchSuggestionItemAdapter(
    val context: Context,
    private var items: List<SuggestionItem>,
    private val onEvent: (SuggestionItem) -> Unit
) :
    RecyclerView.Adapter<SearchSuggestionItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SearchSuggestionItemBinding.bind(itemView)

        fun bind(item: SuggestionItem) {
            binding.primaryText.text = item.query.uppercase()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadItems(newItems: List<SuggestionItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_suggestion_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.suggestionItem.setOnClickListener {
            onEvent(items[position])
        }
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}