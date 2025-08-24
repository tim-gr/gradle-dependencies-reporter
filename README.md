# gradle-dependency-reporter

This repository contains a custom Gradle plugin for Gradle dependencies analysis.

When applied to the root project of your multi-module Gradle project, the plugin registers a task
to generate an interactive HTML dependencies report. This report visualizes how modules depend on
a specific module, both directly and transitively. This is particularly useful for understanding
the impact of changes in a module across the entire project.

In the future, the plugin may be extended to register additional tasks for understanding
the dependency graphs of your multi-module Gradle project.

## Usage

1. Apply the plugin in your root project's build file (`build.gradle.kts`):

   ```kotlin
   plugins {
       id("com.tgad.gradle.dependency.reporter") version "1.0.0"
   }
   ```
   
2. Run the following Gradle task to generate the report:

   ```bash
   ./gradlew dependentsHtmlReport -Pmodule=<name-of-module-to-be-analyzed>
   ```
