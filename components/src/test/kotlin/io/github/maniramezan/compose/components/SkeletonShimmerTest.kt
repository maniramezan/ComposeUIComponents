package io.github.maniramezan.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** Pure-function phase behaviour — no Compose runtime needed. */
class SkeletonShimmerPhaseTest {
    private companion object {
        const val PERIOD = 1_800L
        const val SWEEP_FRACTION = 0.65f
        const val TOLERANCE = 0.001f
    }

    @Test
    fun phaseIsIdenticalAcrossPeriods() {
        // The pause between sweeps is part of the effect: phase must repeat exactly each
        // period so independently-composed skeletons stay locked to one band.
        for (offset in longArrayOf(0L, 137L, 900L, 1_600L, 1_799L)) {
            val base = skeletonShimmerSweep(offset)
            for (cycle in 1..5) {
                assertThat(skeletonShimmerSweep(offset + cycle * PERIOD))
                    .isWithin(TOLERANCE)
                    .of(base)
            }
        }
    }

    @Test
    fun sweepRampsFromZeroToOneOverTheSweepFraction() {
        assertThat(skeletonShimmerSweep(0L)).isWithin(TOLERANCE).of(0f)
        val sweepEndMillis = (PERIOD * SWEEP_FRACTION).toLong()
        assertThat(skeletonShimmerSweep(sweepEndMillis)).isWithin(0.01f).of(1f)
    }

    @Test
    fun bandRestsOffScreenForTheRemainderOfEachPeriod() {
        // After the sweep the band is parked past the trailing edge until the next period.
        val restStart = (PERIOD * SWEEP_FRACTION).toLong() + 1
        for (t in restStart until PERIOD step 50) {
            assertThat(skeletonShimmerSweep(t)).isEqualTo(1f)
        }
    }

    @Test
    fun sweepIsMonotonicNonDecreasingWithinAPeriod() {
        var previous = skeletonShimmerSweep(0L)
        // Exclusive of PERIOD: at exactly one period the phase wraps back to 0.
        for (t in 0L until PERIOD step 30) {
            val current = skeletonShimmerSweep(t)
            assertThat(current).isAtLeast(previous - TOLERANCE)
            previous = current
        }
    }

    @Test
    fun negativeFrameMillisStillProducesAnInRangeSweep() {
        // withInfiniteAnimationFrameMillis' origin is arbitrary and can be negative.
        for (t in longArrayOf(-1L, -1_801L, -12_345L)) {
            val sweep = skeletonShimmerSweep(t)
            assertThat(sweep).isAtLeast(0f)
            assertThat(sweep).isAtMost(1f)
        }
    }
}

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
class SkeletonShimmerModifierTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun contentStillRendersWithShimmerApplied() {
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            Box(
                Modifier
                    .testTag("shimmered")
                    .size(120.dp)
                    .skeletonShimmer(),
            )
        }
        composeRule.onNodeWithTag("shimmered").assertIsDisplayed()
    }

    @Test
    fun disabledViaLocalIsAnInertPassThrough() {
        composeRule.setContent {
            CompositionLocalProvider(LocalSkeletonShimmerEnabled provides false) {
                Box(
                    Modifier
                        .testTag("shimmered")
                        .size(120.dp)
                        .skeletonShimmer(),
                )
            }
        }
        // No crash, no clock needed (no infinite animation is started), content is present.
        composeRule.onNodeWithTag("shimmered").assertIsDisplayed()
    }
}
