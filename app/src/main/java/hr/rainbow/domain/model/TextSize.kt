package hr.rainbow.domain.model

enum class TextSize(val type: Int, val value: Int) {
    Small(0, 20),
    Medium(1, 30),
    Large(2, 40);


    companion object {
        fun fromInt(type: Int): TextSize {
            var textSize = values().firstOrNull { it.type == type }

            if (textSize == null) {
                textSize = Medium
            }

            return textSize
        }
    }
}