val springMockkVersion: String by project
val kotlinVersion: String by project
val jUnit: String by project
val awsVersion: String by project

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

apply(plugin = "io.spring.dependency-management")

tasks.bootJar {
    archiveClassifier.set("fatjar")
}

dependencies {
    implementation(project(":card-identification-service-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind") {
        because("CVE-2020-36518")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    runtimeOnly("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config:$awsVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$jUnit")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
