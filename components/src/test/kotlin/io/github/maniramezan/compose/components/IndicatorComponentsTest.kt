package io.github.maniramezan.compose.components

import android.content.res.Configuration
import android.os.LocaleList
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
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

    @Test
    public fun avatarImageFillsTheCircleWithoutCallerSizing() {
        composeRule.setContent {
            AppTheme {
                Avatar(name = "Avery Quinn", image = { Box(Modifier.testTag(IMAGE)) })
            }
        }

        composeRule
            .onNodeWithTag(IMAGE, useUnmergedTree = true)
            .assertWidthIsEqualTo(40.dp)
            .assertHeightIsEqualTo(40.dp)
    }

    @Test
    public fun blankBadgeLabelsRenderNothing() {
        composeRule.setContent {
            AppTheme {
                Badge(text = "", modifier = Modifier.testTag("empty"))
                Badge(text = "  ", modifier = Modifier.testTag("blank"))
            }
        }

        composeRule.onAllNodesWithTag("empty").assertCountEquals(0)
        composeRule.onAllNodesWithTag("blank").assertCountEquals(0)
    }

    @Test
    public fun badgeCountFollowsConfigurationLocaleChanges() {
        val locale = mutableStateOf(Locale.US)
        composeRule.setContent {
            val configuration = Configuration(LocalConfiguration.current)
            configuration.setLocales(LocaleList(locale.value))
            CompositionLocalProvider(LocalConfiguration provides configuration) {
                AppTheme { CountBadge(count = 125, maxCount = 99) }
            }
        }

        composeRule.onNodeWithText("99+", useUnmergedTree = true).assertIsDisplayed()
        val arabic = Locale.forLanguageTag("ar")
        composeRule.runOnIdle { locale.value = arabic }
        val expected = NumberFormat.getIntegerInstance(arabic).format(99) + "+"
        composeRule.onNodeWithText(expected, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    public fun initialsRecognizeUnicodeWordBoundariesAndLetters() {
        assertEquals("AQ", avatarInitials("Avery\u00a0Quinn"))
        assertEquals("AQ", avatarInitials("Avery—Quinn"))
        assertEquals("𐐀Q", avatarInitials("𐐨very Quinn"))
    }

    private companion object {
        const val IMAGE = "image"
        const val AVATAR = "avatar"
        const val ZERO = "zero"
    }
}
