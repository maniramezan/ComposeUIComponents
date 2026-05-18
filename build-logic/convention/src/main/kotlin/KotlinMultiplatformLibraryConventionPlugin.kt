import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

public class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()

            jvm {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }

            sourceSets.configureEach {
                languageSettings.optIn("kotlin.RequiresOptIn")
            }
            sourceSets.named("commonMain") {
                kotlin.srcDir("src/main/kotlin")
            }
            sourceSets.named("jvmTest") {
                kotlin.srcDir("src/test/kotlin")
            }
        }
    }
}
