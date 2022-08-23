val springMockkVersion: String by project
val kotlinVersion: String by project

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
    implementation(project(":card-identification-service-client"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind") {
        because("CVE-2020-36518")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-devtools")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation("io.rsocket:rsocket-micrometer")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    //testImplementation(project(":card-data-service-client"))

}
