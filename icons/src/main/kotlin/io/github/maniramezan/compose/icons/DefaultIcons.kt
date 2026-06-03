package io.github.maniramezan.compose.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import io.github.maniramezan.compose.theme.AppIcons
import io.github.maniramezan.compose.theme.IconToken

public fun defaultAppIcons(): AppIcons =
    AppIcons(
        check = IconToken(Icons.Filled.Check),
        close = IconToken(Icons.Filled.Close),
        expand = IconToken(Icons.Filled.KeyboardArrowDown),
    )
