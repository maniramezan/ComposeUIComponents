package io.github.maniramezan.compose.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SampleBaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateStartupProfile() {
        baselineProfileRule.collect(
            packageName = PACKAGE_NAME,
        ) {
            pressHome()
            startActivityAndWait()
            device.waitForIdle()
        }
    }

    private companion object {
        const val PACKAGE_NAME = "io.github.maniramezan.compose.sample"
    }
}
