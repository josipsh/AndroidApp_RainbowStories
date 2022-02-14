package hr.rainbow.domain.view_models

import hr.rainbow.domain.model.SuggestionItem

data class SearchSuggestionState(
    val data: List<SuggestionItem> = emptyList(),
    val isLoading: Boolean = false
)
