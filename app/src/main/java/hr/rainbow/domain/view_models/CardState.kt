package hr.rainbow.domain.view_models

import hr.rainbow.domain.model.Card

data class CardState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = true
)