package io.github.maniramezan.compose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AppShapesTest {
    @Test
    fun defaultShapesUseExpectedCornerRadii() {
        val shapes = AppShapes.default()
        assertThat(shapes.standard).isEqualTo(RoundedCornerShape(12.dp))
        assertThat(shapes.small).isEqualTo(RoundedCornerShape(8.dp))
        assertThat(shapes.large).isEqualTo(RoundedCornerShape(16.dp))
        assertThat(shapes.image).isEqualTo(RoundedCornerShape(8.dp))
        assertThat(shapes.badge).isEqualTo(RoundedCornerShape(4.dp))
    }
}
