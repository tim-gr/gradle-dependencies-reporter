plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.tgad"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("gradleDependenciesReporterPlugin") {
            id = "com.tgad.gradle.dependencies.reporter"
            implementationClass = "com.tgad.reporter.GradleDependenciesReporterPlugin"
            displayName = "Gradle Dependencies HTML Reporter"
            description = "Generates an interactive HTML report to analyse which modules depend on any given project - both directly and transitively."
            tags = listOf("gradle", "dependencies", "report")
        }
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
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

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            groupId = "com.tgad"
            artifactId = "gradle-dependencies-reporter"
            version = "1.0.0"
            from(components["java"])
        }
    }
}
