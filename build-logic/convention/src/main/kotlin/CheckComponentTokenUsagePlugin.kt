import org.gradle.api.Plugin
import org.gradle.api.Project

public class CheckComponentTokenUsagePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val checkComponentTokenUsage =
            tasks.register("checkComponentTokenUsage", CheckComponentTokenUsageTask::class.java) {
                group = "verification"
                description = "Reject hardcoded tokens and unsafe icon descriptions in component source."
                sourceFiles.from(
                    fileTree("components/src/main/kotlin") {
                        include("**/*.kt")
                        // Preview and Showkase files are demo scaffolding, not the
                        // public component surface this guard protects. They may
                        // legitimately use decorative icons (contentDescription = null)
                        // and illustrative raw values.
                        exclude("**/*Preview.kt", "**/*Showkase.kt")
                    },
                )
            }

        tasks.register("check") {
            dependsOn(checkComponentTokenUsage)
        }
    }
}
