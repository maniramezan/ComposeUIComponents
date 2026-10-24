package io.github.maniramezan.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.maniramezan.compose.icons.defaultAppIcons
import io.github.maniramezan.compose.theme.AppTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w360dp-h640dp-xhdpi")
public class ComponentCategoryScreenshotTest {
    @Test
    public fun pageIndicatorMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/page-indicator.png") {
            PageIndicatorPreview()
        }
    }

    @Test
    public fun pageIndicatorDarkRtlAtDoubleFontScaleMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/page-indicator-dark-rtl-font200.png") {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(density = LocalDensity.current.density, fontScale = 2f),
            ) {
                AppTheme(darkTheme = true, dynamicColor = false) {
                    PageIndicator(pageCount = 5, currentPage = 1)
                }
            }
        }
    }

    @Test
    public fun statusTextMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/status-text.png") {
            AppTheme {
                Column(
                    modifier = Modifier.width(240.dp),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
                ) {
                    EmptyState(
                        title = "No items in this section yet",
                        message = "Items appear here after you add them to this section.",
                    )
                    ErrorState(
                        title = "Items could not be loaded",
                        message = "Try loading the items again when your connection is available.",
                    )
                }
            }
        }
    }

    @Test
    public fun actionComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/actions-components.png") {
            ActionComponentsPreview()
        }
    }

    @Test
    public fun inputComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/input-components.png") {
            InputComponentsPreview()
        }
    }

    @Test
    public fun menuPickerMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/menu-picker.png") {
            MenuPickerPreview()
        }
    }

    @Test
    public fun menuPickerDarkMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/menu-picker-dark.png") {
            AppTheme(darkTheme = true, dynamicColor = false) {
                Box(modifier = Modifier.background(AppTheme.colors.background)) {
                    MenuPickerPreviewContent()
                }
            }
        }
    }

    @Test
    public fun menuPickerFontScaleRtlMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/menu-picker-font200-rtl.png") {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                MenuPickerPreview()
            }
        }
    }

    @Test
    public fun containerComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/container-components.png") {
            ContainerComponentsPreview()
        }
    }

    @Test
    public fun dialogMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/dialog.png") {
            DialogPreview()
        }
    }

    @Test
    public fun bottomSheetMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/bottom-sheet.png") {
            BottomSheetPreview()
        }
    }

    @Test
    public fun listComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/list-components.png") {
            ListComponentsPreview()
        }
    }

    @Test
    public fun lazyListMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/lazy-list.png") {
            LazyListPreview()
        }
    }

    @Test
    public fun navigationComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/navigation-components.png") {
            NavigationComponentsPreview()
        }
    }

    @Test
    public fun tabBarCenteredMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/tab-bar-centered.png") {
            TabBarCenteredPreview()
        }
    }

    @Test
    public fun tabBarDisabledItemMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/tab-bar-disabled-item.png") {
            TabBarDisabledItemPreview()
        }
    }

    @Test
    public fun navigationRailMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/navigation-rail.png") {
            NavRailPreview()
        }
    }

    @Test
    public fun adaptiveNavScaffoldMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/adaptive-nav-scaffold.png") {
            AdaptiveNavScaffoldPreview()
        }
    }

    @Test
    public fun feedbackComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/feedback-components.png") {
            FeedbackComponentsPreview()
        }
    }

    @Test
    public fun skeletonShimmerMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/skeleton-shimmer.png") {
            SkeletonShimmerPreview()
        }
    }

    @Test
    public fun typographyComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/typography-components.png") {
            TypographyComponentsPreview()
        }
    }

    @Test
    public fun pillChipMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/pill-chip.png") {
            PillChipPreview()
        }
    }

    @Test
    public fun pillChipTierBadgeMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/pill-chip-tier-badge.png") {
            PillChipTierBadgePreview()
        }
    }

    @Test
    public fun selectionListMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/selection-list.png") {
            SelectionListSingleChoicePreview()
        }
    }

    @Test
    public fun selectionListSheetMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/selection-list-sheet.png") {
            SelectionListSheetSingleChoicePreview()
        }
    }

    @Test
    public fun overlayCardMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/overlay-card.png") {
            OverlayCardPreview()
        }
    }

    @Test
    public fun flipCardMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/flip-card.png") {
            FlipCardPreview()
        }
    }

    @Test
    public fun adaptiveContentContainerMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/adaptive-content-container.png") {
            AdaptiveContentContainerPreview()
        }
    }

    @Test
    public fun sectionHeaderMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/section-header.png") {
            SectionHeaderPreview()
        }
    }

    @Test
    public fun contentRowMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/content-row.png") {
            ContentRowPreview()
        }
    }

    @Test
    public fun paginatedContentMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/paginated-content.png") {
            PaginatedContentPreview()
        }
    }

    @Test
    public fun dividerComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/divider-components.png") {
            DividerComponentsPreview()
        }
    }

    @Test
    public fun showcaseFeedMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/showcase-feed.png") {
            ShowcaseFeedPreview()
        }
    }

    @Test
    public fun typingIndicatorMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/typing-indicator.png") {
            TypingIndicatorPreview()
        }
    }

    @Test
    public fun chatComposerMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/chat-composer.png") {
            ChatComposerPreview()
        }
    }

    @Test
    public fun chatComposerDarkMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/chat-composer-dark.png") {
            AppTheme(darkTheme = true, dynamicColor = false) {
                Box(modifier = Modifier.background(AppTheme.colors.background)) {
                    ChatComposerPreviewContent()
                }
            }
        }
    }

    @Test
    public fun chatComposerFontScaleRtlMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/chat-composer-font200-rtl.png") {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                ChatComposerPreview()
            }
        }
    }

    @Test
    public fun noticeBannerMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/notice-banner.png") {
            NoticeBannerPreview()
        }
    }

    @Test
    public fun noticeBannerDarkMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/notice-banner-dark.png") {
            AppTheme(darkTheme = true, dynamicColor = false, icons = defaultAppIcons()) {
                Box(modifier = Modifier.background(AppTheme.colors.background)) {
                    NoticeBannerPreviewContent()
                }
            }
        }
    }

    @Test
    public fun noticeBannerFontScaleRtlMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/notice-banner-font200-rtl.png") {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                NoticeBannerPreview()
            }
        }
    }

    @Test
    public fun selectableCardMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/selectable-card.png") {
            SelectableCardPreview()
        }
    }

    @Test
    public fun selectableCardDarkMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/selectable-card-dark.png") {
            AppTheme(darkTheme = true, dynamicColor = false) {
                Box(modifier = Modifier.background(AppTheme.colors.background)) {
                    SelectableCardPreviewContent()
                }
            }
        }
    }

    @Test
    public fun selectableCardFontScaleRtlMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/selectable-card-font200-rtl.png") {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                SelectableCardPreview()
            }
        }
    }

    @Test
    public fun loadingOverlayMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/loading-overlay.png") {
            LoadingOverlayPreview()
        }
    }

    @Test
    public fun indicatorComponentsMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/indicator-components.png") {
            IndicatorComponentsPreview()
        }
    }

    @Test
    public fun indicatorComponentsFontScaleMatchGolden() {
        captureRoboImage("build/outputs/roborazzi/indicator-components-font-scale.png") {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                IndicatorComponentsPreview()
            }
        }
    }

    @Test
    public fun asyncContentMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/async-content.png") {
            AsyncContentPreview()
        }
    }

    @Test
    public fun asyncContentDarkMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/async-content-dark.png") {
            AppTheme(darkTheme = true, dynamicColor = false) {
                Box(modifier = Modifier.background(AppTheme.colors.background)) {
                    AsyncContentPreviewContent()
                }
            }
        }
    }

    @Test
    public fun asyncContentFontScaleRtlMatchesGolden() {
        captureRoboImage("build/outputs/roborazzi/async-content-font200-rtl.png") {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale = 2f),
                LocalLayoutDirection provides LayoutDirection.Rtl,
            ) {
                AsyncContentPreview()
            }
        }
    }
}
