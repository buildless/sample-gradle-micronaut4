@file:Suppress(
    "DSL_SCOPE_VIOLATION",
    "UnstableApiUsage"
)

import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.21"
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.2.0"
}

version = "0.1"
group = "cloud.elide.sample.micronaut4"

val kotlinVersion by properties
val javaVersion: String by properties
val kotlinLangVersion: String by properties
val strict: String by properties
val strictMode = strict == "true"
val kotlinVersionEnum = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
val jvmTargetEnum = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_19
val javaVersionEnum = JavaVersion.VERSION_19
val globalExclusions = listOf(
  "io.micronaut" to "micronaut-bom",
  "io.micronaut.chatbots" to "micronaut-chatbots-bom",
)

dependencies {
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    compileOnly("org.graalvm.nativeimage:svm")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("io.micronaut:micronaut-http-client")
}

application {
    mainClass.set("cloud.elide.sample.micronaut4.ApplicationKt")
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime(MicronautRuntime.NETTY)
    testRuntime(MicronautTestRuntime.JUNIT_5)
    processing {
        incremental(true)
        annotations("cloud.elide.sample.micronaut4.*")
    }
}

java {
  sourceCompatibility = javaVersionEnum
  targetCompatibility = javaVersionEnum
}

kotlin {
  sourceSets.all {
    languageSettings.progressiveMode = false
    languageSettings.languageVersion = kotlinLangVersion
    languageSettings.apiVersion = kotlinLangVersion
  }

  targets.forEach {
    it.compilations.all {
      kotlinOptions {
        apiVersion = kotlinLangVersion
        languageVersion = kotlinLangVersion
        allWarningsAsErrors = strictMode

        if (this is KotlinJvmCompilerOptions) {
          javaParameters.set(true)
          jvmTarget.set(jvmTargetEnum)
        }
      }
    }
  }
}

afterEvaluate {
  tasks.withType(KotlinCompile::class).configureEach {
    kotlinOptions.jvmTarget = javaVersion
  }
}

configurations.all {
  globalExclusions.forEach {
    exclude(group = it.first, module = it.second)
  }
}

listOf("buildLayers").forEach {
  tasks.named(it).configure {
    doNotTrackState("too big for build cache")
  }
}

