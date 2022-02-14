package hr.rainbow.domain.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.model.Picture
import hr.rainbow.domain.workers.DeleteImageWorker
import hr.rainbow.util.KEY_DELETE_IMAGE_URL
import hr.rainbow.util.Resource
import hr.rainbow.util.UPLOAD_IMAGE_WORK_NAME
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PictureGalleryViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val workManager: WorkManager

) : ViewModel() {

    private val _pictureGalleryData = MutableStateFlow(PictureGalleryState())
    val pictureGallery = _pictureGalleryData.asStateFlow()

    private val _chosenPicture =
        MutableStateFlow(Picture(id = 0, url = "", date = LocalDateTime.now()))
    val chosenPicture = _chosenPicture.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun fetchPictureGalleryData() {
        viewModelScope.launch {
            dataRepository.getPictureGallery().collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { gallery ->
                            _pictureGalleryData.value = _pictureGalleryData.value.copy(
                                gallery = gallery,
                                isLoading = false
                            )
                        } ?: run {
                            _uiEvents.emit(
                                UiEvents.ShowSnackBar(
                                    message = "Unable to download images",
                                    action = "Ok"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _pictureGalleryData.value = _pictureGalleryData.value.copy(
                            isLoading = false
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _pictureGalleryData.value = _pictureGalleryData.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun chosenPictureClick(picture: Picture) {
        _chosenPicture.value = picture
    }

    fun deleteChosenPicture() {
        viewModelScope.launch {
            dataRepository.deleteImageUrlForUser(_chosenPicture.value).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let {
                            deletePictureInCdn(_chosenPicture.value.url)

                            _chosenPicture.value = _chosenPicture.value.copy(
                                url = ""
                            )
                        } ?: run {
                            _uiEvents.emit(
                                UiEvents.ShowSnackBar(
                                    message = "Unable to deleye image",
                                    action = "Ok"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _chosenPicture.value = _chosenPicture.value.copy(
                            url = ""
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    private fun deletePictureInCdn(url: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        workManager.beginUniqueWork(
            UPLOAD_IMAGE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequestBuilder<DeleteImageWorker>()
                .setConstraints(constraints)
                .setInputData(
                    Data.Builder()
                        .putString(KEY_DELETE_IMAGE_URL, url)
                        .build()
                )
                .build()
        ).enqueue()
    }
}