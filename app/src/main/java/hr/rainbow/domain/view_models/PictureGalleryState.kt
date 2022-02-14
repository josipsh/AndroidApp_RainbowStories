package hr.rainbow.domain.view_models

import hr.rainbow.domain.model.Picture

data class PictureGalleryState(
    val gallery: HashMap<String, MutableList<Picture>> = HashMap(0),
    val isLoading: Boolean = true
)
