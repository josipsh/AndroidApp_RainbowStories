package hr.rainbow.domain

import android.graphics.Bitmap
import hr.rainbow.util.Resource

interface MediaRepository {
    fun downloadImage(url: String): Bitmap
    fun uploadImage(image: Bitmap): Resource<String>
    fun deleteImage(imageUrl: String): Resource<Boolean>
}