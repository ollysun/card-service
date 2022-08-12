with(rootProject) {
    name = "card-identification"
    pluginManagement {
        val kotlinVersion: String by settings
        val springBootVersion: String by settings
        val sonarQubePluginVersion: String by settings
        val dependencyCheckVersion: String by settings
        val dependencyManagementVersion: String by settings
        plugins {
            kotlin("jvm") version kotlinVersion
            kotlin("plugin.serialization") version kotlinVersion
            id("org.springframework.boot") version springBootVersion
            id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
            id("org.sonarqube") version sonarQubePluginVersion
            id("org.owasp.dependencycheck") version dependencyCheckVersion
            id("io.spring.dependency-management") version dependencyManagementVersion
        }
    }
}
buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

//include("protobuf2rsocket")
include("common")
include("service")
include("module-test")

// Required to prevent overlapping when creating dependencies on client/common of multiple components
project(":common").name = "card-identification-service-common"
project(":module-test").name = "card-identification-service-module-test"
