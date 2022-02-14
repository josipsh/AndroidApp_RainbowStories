package hr.rainbow.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import hr.rainbow.R
import hr.rainbow.databinding.PictureGalleryItemBinding
import hr.rainbow.domain.model.Picture
import hr.rainbow.util.AZURE_SAS_KEY
import hr.rainbow.util.UiEvents
import java.util.*

class PictureGalleryItemAdapter(
    private val context: Context,
    private val galleryItems: List<Picture>,
    var glide: RequestManager,
    private val onPictureClick: (Picture) -> Unit
) : RecyclerView.Adapter<PictureGalleryItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = PictureGalleryItemBinding.bind(itemView)

        fun bind(picture: Picture) {
            val title = "${picture.date.dayOfMonth}." +
                    "${picture.date.month}." +
                    "${picture.date.year} "
            binding.tvGalleryItemTitle.text = title

            glide.load("${picture.url}?$AZURE_SAS_KEY").into(binding.ivGalleryItem)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.picture_gallery_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = galleryItems[position]
        holder.itemView.setOnClickListener {
            onPictureClick(picture)
        }
        holder.bind(picture)

    }

    override fun getItemCount(): Int = galleryItems.size

}