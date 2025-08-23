rootProject.name = "gradle-dependency-reporter"

// include all modules
include(":A", ":B", ":C", ":D")

// make the plugin available to other modules via composite build
pluginManagement {
    includeBuild("plugin")
}
