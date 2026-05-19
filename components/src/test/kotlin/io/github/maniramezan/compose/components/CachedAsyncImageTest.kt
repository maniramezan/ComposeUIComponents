package io.github.maniramezan.compose.components

import coil3.request.CachePolicy
import com.google.common.truth.Truth.assertThat
import org.junit.Test

public class CachedAsyncImageTest {
    @Test
    public fun imageCachePolicyMapsToCoilCachePolicy() {
        assertThat(ImageCachePolicy.ENABLED.toCoilCachePolicy()).isEqualTo(CachePolicy.ENABLED)
        assertThat(ImageCachePolicy.READ_ONLY.toCoilCachePolicy()).isEqualTo(CachePolicy.READ_ONLY)
        assertThat(ImageCachePolicy.WRITE_ONLY.toCoilCachePolicy()).isEqualTo(CachePolicy.WRITE_ONLY)
        assertThat(ImageCachePolicy.DISABLED.toCoilCachePolicy()).isEqualTo(CachePolicy.DISABLED)
    }
}
