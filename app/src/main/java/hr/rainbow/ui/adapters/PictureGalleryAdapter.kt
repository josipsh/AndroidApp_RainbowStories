package hr.rainbow.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import hr.rainbow.R
import hr.rainbow.databinding.PictureGalleryMonthBinding
import hr.rainbow.domain.model.Picture
import hr.rainbow.util.UiEvents

class PictureGalleryAdapter(
    private val context: Context,
    private var pictureGallery: HashMap<String, MutableList<Picture>>,
    var glide: RequestManager,
    private val onPictureClickEvent: (Picture) -> Unit
) : RecyclerView.Adapter<PictureGalleryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = PictureGalleryMonthBinding.bind(itemView)

        fun bind(
            month: String, images: MutableList<Picture>,
            onBindPictureClick: (Picture) -> Unit
        ) {
            binding.tvPictureGalleryDate.text = month
            binding.rvPictureGalleryMonth.adapter =
                PictureGalleryItemAdapter(context, images, glide) {
                    onBindPictureClick(it)
                }
            binding.rvPictureGalleryMonth.layoutManager =
                GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadGallery(newGallery: HashMap<String, MutableList<Picture>>) {
        pictureGallery = newGallery
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.picture_gallery_month, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = pictureGallery.keys.elementAt(position)
        pictureGallery[key]?.let {
            holder.bind(key, it) { picture ->
                onPictureClickEvent(picture)
            }
        }
    }

    override fun getItemCount(): Int = pictureGallery.size
}