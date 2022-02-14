package hr.rainbow.domain.model

enum class SearchSuggestionType(val value: Int) {
    RECENT(0),
    TAG(1);

    companion object {
        fun fromInt(value: Int): SearchSuggestionType {
            var result = values().firstOrNull {
                it.value == value
            }
            if (result == null){
                result = RECENT
            }

            return result
        }
    }
}