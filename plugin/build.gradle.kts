plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.1"
    // TODO Signing
}

repositories {
    mavenCentral()
}

group = "io.github.tim-gr"
version = "1.0.0"

gradlePlugin {
    website = "https://github.com/tim-gr/gradle-dependency-reporter"
    plugins {
        create("gradleDependenciesReporterPlugin") {
            id = "io.github.tim-gr.dependencies-reporter"
            implementationClass = "io.github.tim_gr.dependencies_reporter.DependenciesReporterPlugin"
            displayName = "Gradle Dependencies Reporter"
            description = "Creates an interactive HTML report to help with analysing which modules depend on a given project - both directly and transitively."
            tags = listOf("gradle", "dependencies", "report")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            groupId = "io.github.tim-gr"
            artifactId = "dependencies-reporter"
            version = "1.0.0"
            from(components["java"])
        }
    }
}
