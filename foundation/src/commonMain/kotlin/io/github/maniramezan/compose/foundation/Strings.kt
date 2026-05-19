package io.github.maniramezan.compose.foundation

public fun String.normalizedWhitespace(): String = trim().split(Regex("\\s+")).joinToString(" ")

public fun String.toStableIdentifier(separator: Char = '-'): String {
    val normalized = normalizedWhitespace().lowercase()
    val builder = StringBuilder(normalized.length)
    var lastWasSeparator = false

    normalized.forEach { character ->
        val next =
            when {
                character.isLetterOrDigit() -> character
                else -> separator
            }
        if (next == separator) {
            if (!lastWasSeparator && builder.isNotEmpty()) {
                builder.append(next)
            }
            lastWasSeparator = true
        } else {
            builder.append(next)
            lastWasSeparator = false
        }
    }

    return builder.toString().trim(separator)
}

public fun String.toDisplayName(): String =
    toStableIdentifier(separator = ' ')
        .split(' ')
        .filter { it.isNotBlank() }
        .joinToString(" ") { segment ->
            segment.replaceFirstChar { character -> character.titlecase() }
        }
