package io.github.maniramezan.compose.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ThumbsRatingTapTest {
    private data class Case(
        val name: String,
        val current: ThumbsRating,
        val tapped: ThumbsRating,
        val allowClearing: Boolean,
        val expected: ThumbsRating?,
    )

    private val cases =
        listOf(
            Case("select positive from none", ThumbsRating.None, ThumbsRating.Positive, true, ThumbsRating.Positive),
            Case("select negative from none", ThumbsRating.None, ThumbsRating.Negative, false, ThumbsRating.Negative),
            Case("switch to negative", ThumbsRating.Positive, ThumbsRating.Negative, false, ThumbsRating.Negative),
            Case("switch to positive", ThumbsRating.Negative, ThumbsRating.Positive, true, ThumbsRating.Positive),
            Case("clear positive", ThumbsRating.Positive, ThumbsRating.Positive, true, ThumbsRating.None),
            Case("clear negative", ThumbsRating.Negative, ThumbsRating.Negative, true, ThumbsRating.None),
            Case("keep positive when clearing is off", ThumbsRating.Positive, ThumbsRating.Positive, false, null),
            Case("keep negative when clearing is off", ThumbsRating.Negative, ThumbsRating.Negative, false, null),
            Case("tapping none is never reported", ThumbsRating.None, ThumbsRating.None, true, null),
        )

    @Test
    fun resolvesEveryCaseInTheDecisionTable() {
        cases.forEach { case ->
            assertThat(resolveThumbsRatingTap(case.current, case.tapped, case.allowClearing))
                .isEqualTo(case.expected)
        }
    }
}
