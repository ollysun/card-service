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
