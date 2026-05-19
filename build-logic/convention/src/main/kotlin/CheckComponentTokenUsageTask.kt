import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

public abstract class CheckComponentTokenUsageTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    public abstract val sourceFiles: ConfigurableFileCollection

    @TaskAction
    public fun checkTokenUsage() {
        val rawDpPattern = Regex("""\b\d+(?:\.\d+)?\.dp\b""")
        val hardcodedColorPattern = Regex("""Color\s*\(\s*0x[0-9A-Fa-f_]+""")
        val nullableContentDescriptionPattern = Regex("""contentDescription\s*:\s*String\?""")
        val nullContentDescriptionPattern = Regex("""contentDescription\s*=\s*null""")
        val violations =
            sourceFiles.files.flatMap { file ->
                file.readLines().mapIndexedNotNull { index, line ->
                    val hasViolation =
                        rawDpPattern.containsMatchIn(line) ||
                            hardcodedColorPattern.containsMatchIn(line) ||
                            nullableContentDescriptionPattern.containsMatchIn(line) ||
                            nullContentDescriptionPattern.containsMatchIn(line)
                    if (hasViolation) "${file.invariantSeparatorsPath}:${index + 1}: $line" else null
                }
            }

        check(violations.isEmpty()) {
            "Component source quality violations:\n" + violations.joinToString("\n")
        }
    }
}
