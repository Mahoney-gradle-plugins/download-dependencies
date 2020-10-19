package uk.org.lidalia.gradle.plugins.downloaddependencies

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.internal.deprecation.DeprecatableConfiguration

class DownloadDependenciesPlugin : Plugin<Project> {
  override fun apply(rootProject: Project) {
    rootProject.allprojects.forEach { project ->
      project.tasks.register("downloadDependencies") {
        it.doLast { task ->
          task.project.configurations.resolveAll()
          task.project.buildscript.configurations.resolveAll()
        }
      }
    }
  }
}

private fun ConfigurationContainer.resolveAll() = this
  .filter { it.isCanBeResolved && !it.isDeprecated() }
  .forEach { it.resolve() }

private fun Configuration.isDeprecated(): Boolean =
  this is DeprecatableConfiguration && resolutionAlternatives != null
