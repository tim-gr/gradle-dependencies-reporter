plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.tgad"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("reporter") {
            id = "com.tgad.reporter"
            implementationClass = "com.tgad.reporter.GradleDependenciesReporterPlugin"
            displayName = "Dependents Tree HTML Report"
            description = "Generates an interactive HTML report of which modules depend on the current project."
        }
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest("2.2.0")
        }

        // Create a new test suite
        val functionalTest by registering(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest("2.2.0")

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project())
            }

            targets {
                all {
                    // This test suite should run after the built-in test suite has run its tests
                    testTask.configure { shouldRunAfter(test) } 
                }
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}
