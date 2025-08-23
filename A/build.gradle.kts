plugins {
    id("java-library")
    id("com.tgad.reporter") // plugin is available because of pluginManagement includeBuild
}

dependencies {
    implementation(project(":B"))
}
