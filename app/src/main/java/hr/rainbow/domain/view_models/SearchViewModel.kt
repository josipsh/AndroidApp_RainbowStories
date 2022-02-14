package hr.rainbow.domain.view_models

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.rainbow.data.local.recent_search_suggestion.RECENT_SEARCH_SUGGESTION_PROVIDER_URI
import hr.rainbow.data.local.tag_search_suggestion.TAG_SEARCH_SUGGESTION_PROVIDER_URI
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.model.SearchSuggestionType
import hr.rainbow.domain.model.Story
import hr.rainbow.domain.model.SuggestionItem
import hr.rainbow.util.Resource
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _recentSuggestion = MutableStateFlow(SearchSuggestionState())
    val recentSuggestion = _recentSuggestion.asStateFlow()

    private val _tagSuggestion = MutableStateFlow(SearchSuggestionState())
    val tagSuggestion = _tagSuggestion.asStateFlow()

    private val _queryResult = MutableStateFlow(StoryState())
    val queryResult = _queryResult.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun getSuggestions(context: Context, query: String) {
        viewModelScope.launch {
            getRecentSuggestions(context, query)
            getTagSuggestions(context, query)
        }
    }

    private fun getRecentSuggestions(context: Context, query: String) {
        viewModelScope.launch {
            try {
                _recentSuggestion.value = _recentSuggestion.value.copy(
                    data = emptyList(),
                    isLoading = true
                )

                val items = mutableListOf<SuggestionItem>()

                with(context) {
                    val cursor = contentResolver.query(
                        RECENT_SEARCH_SUGGESTION_PROVIDER_URI,
                        null,
                        "${SuggestionItem::query.name} like ?",
                        arrayOf("$query%"),
                        if (query == "") "_id desc" else null
                    )

                    while (cursor != null && cursor.moveToNext()) {
                        items.add(
                            SuggestionItem(
                                query = cursor.getString(cursor.getColumnIndexOrThrow(SuggestionItem::query.name)),
                                type = SearchSuggestionType.fromInt(
                                    cursor.getInt(
                                        cursor.getColumnIndexOrThrow(
                                            SuggestionItem::type.name
                                        )
                                    )
                                )
                            )
                        )
                    }
                    cursor?.close()
                }
                _recentSuggestion.value = _recentSuggestion.value.copy(
                    data = items.toList(),
                    isLoading = false
                )
            } catch (e: Throwable) {
                _recentSuggestion.value = _recentSuggestion.value.copy(
                    data = listOf(),
                    isLoading = false
                )
                _uiEvents.emit(
                    UiEvents.ShowSnackBar(
                        message = e.message ?: "Unexpected error occurred!",
                        action = "Ok"
                    )
                )
            }
        }
    }

    private fun getTagSuggestions(context: Context, query: String) {
        viewModelScope.launch {
            try {
                _tagSuggestion.value = _tagSuggestion.value.copy(
                    data = emptyList(),
                    isLoading = true
                )

                val items = mutableListOf<SuggestionItem>()

                if (query.isNotEmpty()) {
                    with(context) {
                        val cursor = contentResolver.query(
                            TAG_SEARCH_SUGGESTION_PROVIDER_URI,
                            null,
                            "tag like ?",
                            arrayOf("$query%"),
                            if (query == "") "_id desc" else null
                        )

                        while (cursor != null && cursor.moveToNext()) {
                            items.add(
                                SuggestionItem(
                                    query = cursor.getString(
                                        cursor.getColumnIndexOrThrow(
                                            "tag"
                                        )
                                    ),
                                    type = SearchSuggestionType.TAG
                                )
                            )
                        }
                        cursor?.close()
                    }
                }
                _tagSuggestion.value = _tagSuggestion.value.copy(
                    data = items.toList(),
                    isLoading = false
                )
            } catch (e: Throwable) {
                _tagSuggestion.value = _tagSuggestion.value.copy(
                    data = listOf(),
                    isLoading = false
                )
                _uiEvents.emit(
                    UiEvents.ShowSnackBar(
                        message = e.message ?: "Unexpected error occurred!",
                        action = "Ok"
                    )
                )
            }
        }
    }

    fun recentSearch(context: Context, queryItem: SuggestionItem) {
        if (queryItem.type == SearchSuggestionType.TAG) {
            tagSearch(context, queryItem)
        }
    }

    fun tagSearch(context: Context, query: SuggestionItem) {
        viewModelScope.launch {
            if (query.query.isNotEmpty()){
                saveInDb(context, query)
                repository.searchByTag(query.query).collect {
                    handleResponse(it)
                }
            }
        }
    }

    private fun saveInDb(context: Context, query: SuggestionItem) {
        context.contentResolver.insert(
            RECENT_SEARCH_SUGGESTION_PROVIDER_URI,
            ContentValues().apply {
                put(SuggestionItem::query.name, query.query)
                put(SuggestionItem::type.name, query.type.value)
            }
        )
    }

    private suspend fun handleResponse(response: Resource<List<Story>>) {
        when (response) {
            is Resource.Success -> {
                response.data?.let {
                    _queryResult.value = _queryResult.value.copy(
                        storyItems = it,
                        isLoading = false
                    )
                } ?: run {
                    _uiEvents.emit(
                        UiEvents.ShowSnackBar(
                            message = "Unexpected error occurred!",
                            action = "Ok"
                        )
                    )
                }

            }
            is Resource.Error -> {
                _queryResult.value = _queryResult.value.copy(
                    isLoading = false
                )
                _uiEvents.emit(
                    UiEvents.ShowSnackBar(
                        message = response.message ?: "Unexpected error occurred!",
                        action = "Ok"
                    )
                )
            }
            is Resource.Loading -> {
                _queryResult.value = _queryResult.value.copy(
                    isLoading = true
                )
            }
        }
    }

    fun onLikeClickEvent(storyId: Int, isLiked: Boolean) {
        repository.like(storyId, isLiked)
    }
}