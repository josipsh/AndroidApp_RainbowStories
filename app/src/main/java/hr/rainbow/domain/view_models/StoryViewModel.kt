package hr.rainbow.domain.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.rainbow.domain.DataRepository
import hr.rainbow.util.Resource
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _discoverStories = MutableStateFlow(StoryState())
    val discoverStories = _discoverStories.asStateFlow()

    private val _favoriteStories = MutableStateFlow(StoryState())
    val favoritesStories = _favoriteStories.asStateFlow()

    private val _myStories = MutableStateFlow(StoryState())
    val myStories = _myStories.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun fetchDiscoverStories() {
        viewModelScope.launch {
            repository.getDiscoverStories().collect {
                when (it) {
                    is Resource.Success -> {
                        _discoverStories.value = _discoverStories.value.copy(
                            storyItems = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _discoverStories.value = _discoverStories.value.copy(
                            storyItems = emptyList(),
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
                        _discoverStories.value = _discoverStories.value.copy(
                            storyItems = emptyList(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun fetchFavoritesStories() {
        viewModelScope.launch {
            repository.getFavoritesStories().collect {
                when (it) {
                    is Resource.Success -> {
                        _favoriteStories.value = _favoriteStories.value.copy(
                            storyItems = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _favoriteStories.value = _favoriteStories.value.copy(
                            storyItems = emptyList(),
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
                        _favoriteStories.value = _favoriteStories.value.copy(
                            storyItems = emptyList(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun fetchMyStories() {
        viewModelScope.launch {
            repository.getMyStories().collect {
                when (it) {
                    is Resource.Success -> {
                        _myStories.value = _myStories.value.copy(
                            storyItems = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _myStories.value = _myStories.value.copy(
                            storyItems = emptyList(),
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
                        _myStories.value = _myStories.value.copy(
                            storyItems = emptyList(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun onLikeClickEvent(storyId: Int, isLiked: Boolean) {
        viewModelScope.launch {
            repository.like(storyId, isLiked).collect {
                when (it) {
                    is Resource.Success -> {
                        _myStories.value = _myStories.value.copy(
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _myStories.value = _myStories.value.copy(
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
                        _myStories.value = _myStories.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}