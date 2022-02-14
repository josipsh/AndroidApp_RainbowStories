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
class StoryViewerViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _cards = MutableStateFlow(CardState())
    val cards = _cards.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun fetchCards(storyId: Int){
        viewModelScope.launch {
            repository.getStoryCards(storyId).collect {
                when(it){
                    is Resource.Success ->{
                        _cards.value = _cards.value.copy(
                            cards = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error ->{
                        _cards.value = _cards.value.copy(
                            cards = emptyList(),
                            isLoading = false
                        )
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = it.message ?: "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                    is Resource.Loading ->{
                        _cards.value = _cards.value.copy(
                            cards = emptyList(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}