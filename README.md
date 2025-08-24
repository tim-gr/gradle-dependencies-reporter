# gradle-dependency-reporter

This repository contains a custom Gradle plugin for Gradle dependencies reports.

When applied to the root project of your multi-module Gradle project, the plugin registers a task
to generate an interactive HTML dependencies report. This report visualizes how modules depend on
a selected module, both directly and transitively. This is particularly useful for understanding
the impact of changes in a module on the entire project.

In the future, the plugin may be extended to register additional tasks for a better understanding of
the dependency graphs of your multi-module Gradle project.

## Usage

1. Apply the plugin in your root project's build file (`build.gradle.kts`):

   ```kotlin
   plugins {
       id("com.tgad.dependencies.reporter") version "1.0.0"
   }
   ```
   
2. Run the following Gradle task to generate the report (replace the module name as needed):

   ```bash
   ./gradlew dependentsHtmlReport -Pmodule=:feature:A
   ```
