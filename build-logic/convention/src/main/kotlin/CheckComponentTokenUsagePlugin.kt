import org.gradle.api.Plugin
import org.gradle.api.Project

public class CheckComponentTokenUsagePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val checkComponentTokenUsage =
            tasks.register("checkComponentTokenUsage", CheckComponentTokenUsageTask::class.java) {
                group = "verification"
                description = "Reject hardcoded Color values and raw dp literals in component source."
                sourceFiles.from(
                    fileTree("components/src/main/kotlin") {
                        include("**/*.kt")
                    },
                )
            }

        tasks.register("check") {
            dependsOn(checkComponentTokenUsage)
        }
    }
}
