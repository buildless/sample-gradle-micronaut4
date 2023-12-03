@file:Suppress(
    "DSL_SCOPE_VIOLATION",
    "UnstableApiUsage"
)

pluginManagement {
    repositories {
        maven("https://gradle.pkg.st/")
    }
}

plugins {
    id("build.less") version("1.0.0-beta8")
    id("com.gradle.enterprise") version("3.15.1")
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

val micronautVersion: String by settings
val cachePush: String? by settings
val remoteCache = System.getenv("GRADLE_CACHE_REMOTE")?.toBoolean() ?: false
val localCache = System.getenv("GRADLE_CACHE_LOCAL")?.toBoolean() ?: true

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )
    repositories {
        maven("https://maven.pkg.st/")
        mavenCentral()
    }
    versionCatalogs {
        create("mn") {
            from("io.micronaut.platform:micronaut-platform:$micronautVersion")
        }
    }
}

rootProject.name = "sample"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("GROOVY_COMPILATION_AVOIDANCE")
