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
                    },
                )
            }

        tasks.register("check") {
            dependsOn(checkComponentTokenUsage)
        }
    }
}
