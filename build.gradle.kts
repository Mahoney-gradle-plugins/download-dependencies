plugins {
  kotlin("jvm") version "1.4.10"
  id("java-gradle-plugin")
  id("org.jmailen.kotlinter") version "3.2.0"
  id("com.gradle.plugin-publish") version "0.12.0"
}

version = "1.0-SNAPSHOT"
group = "uk.org.lidalia"

repositories {
  jcenter()
}

dependencies {
  testImplementation("io.kotest:kotest-runner-junit5:4.3.0")
}

kotlin {
  sourceSets {
    main {
      kotlin.setSrcDirs(setOf("src"))
    }
    test {
      kotlin.setSrcDirs(setOf("tests"))
    }
  }
}

tasks.test {
  environment("BUILD_SYSTEM", "GRADLE")
  useJUnitPlatform()
  jvmArgs(
    "-Xshare:off",
    "--illegal-access=deny",
    "--add-opens", "java.base/java.lang=ALL-UNNAMED"
  )
}

kotlinter {
  reporters = arrayOf("checkstyle", "plain", "html")
}

gradlePlugin {
  plugins {
    create("downloadDependenciesPlugin") {
      id = "${project.group}.${project.name}"
      displayName = "Download Dependencies Plugin"
      description = "A plugin that adds a downloadDependencies task that downloads all " +
        "dependencies of a Gradle build in a single task, allowing offline running."
      implementationClass = "gradle.plugins.${project.group}.downloaddependencies.DownloadDependenciesPlugin"
    }
  }
}

pluginBundle {
  website = "https://github.com/Mahoney-gradle-plugins/${project.name}"
  vcsUrl = "https://github.com/Mahoney-gradle-plugins/${project.name}.git"
  tags = listOf("dependencies", "download", "offline")
}
