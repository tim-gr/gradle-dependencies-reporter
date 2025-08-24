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

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation(gradleTestKit())
}

tasks.test {
    useJUnitPlatform()
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
