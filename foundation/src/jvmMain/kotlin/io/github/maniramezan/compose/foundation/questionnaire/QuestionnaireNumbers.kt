package io.github.maniramezan.compose.foundation.questionnaire

import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

/** Locale-aware numeric editing without grouping, exponent notation, or non-finite values. */
public object QuestionnaireNumbers {
    /** Keeps decimal digits, one locale separator, and an optional leading minus sign. */
    public fun sanitize(
        text: String,
        locale: Locale,
        allowsNegative: Boolean,
    ): String {
        val symbols = DecimalFormatSymbols.getInstance(locale)
        var hasSeparator = false
        return buildString {
            text.forEach { character ->
                when {
                    Character.digit(character, 10) >= 0 -> append(character)
                    character == symbols.decimalSeparator && !hasSeparator -> {
                        append(character)
                        hasSeparator = true
                    }
                    (character == '-' || character == symbols.minusSign) && isEmpty() && allowsNegative -> append('-')
                }
            }
        }
    }

    /** Parses an edited field, including localized decimal digits. Incomplete text returns null. */
    public fun parse(
        text: String,
        locale: Locale,
    ): Double? {
        val separator = DecimalFormatSymbols.getInstance(locale).decimalSeparator
        val canonical =
            buildString {
                text.forEach { character ->
                    val digit = Character.digit(character, 10)
                    when {
                        digit >= 0 -> append(digit)
                        character == separator -> append('.')
                        character == '-' -> append('-')
                        else -> return null
                    }
                }
            }
        return canonical.toDoubleOrNull()?.takeIf { it.isFinite() }
    }

    /** Formats without grouping separators so the field can parse its own output. */
    public fun format(
        value: Double,
        locale: Locale,
    ): String =
        NumberFormat
            .getNumberInstance(locale)
            .apply {
                isGroupingUsed = false
                maximumFractionDigits = 340
            }.format(value)
            .replace(DecimalFormatSymbols.getInstance(locale).minusSign, '-')
            .filter {
                Character.getType(it) !=
                    Character.FORMAT.toInt()
            }
}
