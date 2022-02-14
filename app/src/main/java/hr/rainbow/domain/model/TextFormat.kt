package hr.rainbow.domain.model

enum class TextFormat(val type: Int) {
    REGULAR(0),
    BOLD(1),
    ITALIC(2),
    BOLD_ITALIC(3);

    companion object {
        fun fromInt(type: Int): TextFormat {
            var textFormat = values().firstOrNull { it.type == type }

            if (textFormat == null) {
                textFormat = REGULAR
            }

            return textFormat
        }
    }
}