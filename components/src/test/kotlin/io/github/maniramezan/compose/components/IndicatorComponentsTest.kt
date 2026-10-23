package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.text.NumberFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
public class IndicatorComponentsTest {
    @get:Rule
    public val composeRule = createComposeRule()

    @Test
    public fun initialsUseFirstAndLastWordLetters() {
        assertEquals("AQ", avatarInitials("Avery Quinn"))
        assertEquals("AM", avatarInitials("avery j. morgan"))
        assertEquals("B", avatarInitials("  Blake  "))
        assertEquals("CR", avatarInitials("Casey-Rivera"))
        assertEquals("É", avatarInitials("élan"))
    }

    @Test
    public fun initialsSkipNonLetterWordsAndHandleEmptyNames() {
        assertEquals("", avatarInitials(""))
        assertEquals("", avatarInitials("   "))
        assertEquals("", avatarInitials("42 !!"))
        assertEquals("DS", avatarInitials("Dana 2nd Sky"))
    }

    @Test
    public fun badgeCountCollapsesAboveMax() {
        val format = NumberFormat.getIntegerInstance(Locale.US)
        assertEquals("7", formatBadgeCount(7, maxCount = 99, format = format))
        assertEquals("99", formatBadgeCount(99, maxCount = 99, format = format))
        assertEquals("99+", formatBadgeCount(100, maxCount = 99, format = format))
        assertEquals("9+", formatBadgeCount(12, maxCount = 9, format = format))
    }

    @Test
    public fun avatarIsOneImageNodeNamedByCaller() {
        composeRule.setContent {
            AppTheme { Avatar(name = "Avery Quinn") }
        }

        composeRule
            .onNodeWithContentDescription("Avery Quinn")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Image))
        // Initials are drawn but not announced separately.
        composeRule.onAllNodesWithText("AQ").assertCountEquals(0)
    }

    @Test
    public fun avatarRendersCallerImageInsteadOfInitials() {
        composeRule.setContent {
            AppTheme {
                Avatar(name = "Avery Quinn", image = { Box(Modifier.testTag(IMAGE)) })
            }
        }

        composeRule.onAllNodesWithTag(IMAGE, useUnmergedTree = true).assertCountEquals(1)
        composeRule.onNodeWithContentDescription("Avery Quinn").assertIsDisplayed()
    }

    @Test
    public fun blankAvatarNameIsDecorative() {
        composeRule.setContent {
            AppTheme { Avatar(name = "", modifier = Modifier.testTag(AVATAR)) }
        }

        val config = composeRule.onNodeWithTag(AVATAR).fetchSemanticsNode().config
        assertTrue(config.isClearingSemantics)
        assertTrue(SemanticsProperties.ContentDescription !in config.map { it.key })
    }

    @Test
    public fun badgeReadsItsLabel() {
        composeRule.setContent {
            AppTheme { Badge(text = "Beta") }
        }

        composeRule.onNodeWithText("Beta").assertIsDisplayed()
    }

    @Test
    public fun countBadgeAnnouncesCallerDescriptionAndHidesNonPositiveCounts() {
        composeRule.setContent {
            AppTheme {
                Box {
                    CountBadge(count = 3, contentDescription = "3 unread")
                    CountBadge(count = 0, modifier = Modifier.testTag(ZERO))
                }
            }
        }

        composeRule.onNodeWithContentDescription("3 unread").assertIsDisplayed()
        composeRule.onAllNodesWithTag(ZERO).assertCountEquals(0)
    }

    private companion object {
        const val IMAGE = "image"
        const val AVATAR = "avatar"
        const val ZERO = "zero"
    }
}
