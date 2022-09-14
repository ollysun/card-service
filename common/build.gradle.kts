val commonUtilsVersion: String by project
val kotlinSerializationVersion: String by project
val springBootVersion: String by project

plugins {
    kotlin("plugin.serialization")
}

dependencies {
    implementation("com.vayapay.common:common-utils:$commonUtilsVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
}
