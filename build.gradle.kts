plugins {
  kotlin("jvm") version "1.4.10"
  id("java-gradle-plugin")
  id("org.jmailen.kotlinter") version "3.2.0"
}

repositories {
  jcenter()
  mavenCentral()
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
      id = "uk.org.lidalia.downloaddependencies"
      implementationClass = "uk.org.lidalia.gradle.plugins.downloaddependencies.DownloadDependenciesPlugin"
    }
  }
}
