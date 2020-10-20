package gradle.plugins.uk.org.lidalia.downloaddependencies

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import org.gradle.testfixtures.ProjectBuilder

class DownloadDependenciesPluginSpec : StringSpec({

  "task applied to single project" {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("uk.org.lidalia.download-dependencies")

    project.tasks.names shouldContain "downloadDependencies"
  }

  "task applied to multi-module project" {
    val rootProject = ProjectBuilder.builder()
      .build()

    val childProject1 = ProjectBuilder.builder()
      .withName("childProject1")
      .withParent(rootProject)
      .build()

    val childProject2 = ProjectBuilder.builder()
      .withName("childProject2")
      .withParent(rootProject)
      .build()

    val grandchildProject = ProjectBuilder.builder()
      .withName("grandchildProject")
      .withParent(childProject2)
      .build()

    rootProject.pluginManager.apply("uk.org.lidalia.download-dependencies")

    listOf(
      rootProject,
      childProject1,
      childProject2,
      grandchildProject,
    ).forEach { project ->
      project.tasks.names shouldContain "downloadDependencies"
    }
  }
})
