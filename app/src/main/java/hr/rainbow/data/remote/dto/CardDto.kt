package hr.rainbow.data.remote.dto

import hr.rainbow.domain.model.Card
import hr.rainbow.domain.model.TextFormat
import hr.rainbow.domain.model.TextSize

data class CardDto(
    val IDCARD: Int,
    val NAME: String,
    val PROFILEID: Int,
    val SHARED: Int,
    val TEXT: String,
    val CardOrder: Int,
    val FORMAT: FormatDto,
    val TAGS: List<TagDto>
) {
    fun toCard(): Card {
        val image1: String?
        val image2: String?
        val image3: String?

        if (FORMAT.IMAGE3 != null && FORMAT.IMAGE2 != null && FORMAT.IMAGE1 != null) {
            image1 = FORMAT.IMAGE1.URL
            image2 = FORMAT.IMAGE2.URL
            image3 = FORMAT.IMAGE3.URL
        } else if (FORMAT.IMAGE3 == null && FORMAT.IMAGE2 != null && FORMAT.IMAGE1 != null) {
            image1 = FORMAT.IMAGE1.URL
            image2 = FORMAT.IMAGE2.URL
            image3 = null
        } else if (FORMAT.IMAGE3 == null && FORMAT.IMAGE2 == null && FORMAT.IMAGE1 != null) {
            image2 = FORMAT.IMAGE1.URL
            image1 = null
            image3 = null
        } else {
            image1 = null
            image2 = null
            image3 = null
        }

        return Card(
            id = IDCARD,
            topText = TEXT,
            bottomText = TEXT,
            startImageUrl = image1,
            centerImageUrl = image2,
            endImageUrl = image3,
            color = FORMAT.COLOR,
            textSize = TextSize.fromInt(FORMAT.FONT_SIZE),
            textFormat = TextFormat.fromInt(FORMAT.FONT_FORMAT)
        )
    }
}