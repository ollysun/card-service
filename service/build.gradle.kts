val commonUtilsVersion: String by project
val hsmProxyVersion: String by project
val jUnit: String by project
val logbackEncoder: String by project
val berTlvVersion: String by project
val springMockkVersion: String by project
val kotlinVersion: String by project
val shedLockVersion: String by project
val loggingUtilsVersion: String by project
val awSpringVersion: String by project
val cardDataVersion: String by project
val jacksonDataType: String by project
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
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind:[2.13.2.1,3)") {
        because("CVE-2020-36518")
        implementation("org.springframework.boot:spring-boot-starter-validation")
    }
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
    implementation("com.vayapay.common:common-utils:$commonUtilsVersion")
    testImplementation("junit:junit:4.13.2")
    implementation("net.javacrumbs.shedlock:shedlock-spring:$shedLockVersion")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation("io.rsocket:rsocket-micrometer")
    implementation("com.vayapay.carddata:card-data-service-client:$cardDataVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonDataType")
    // Dependencies for formatting logs in Kibana friendly format
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:$logbackEncoder")
    runtimeOnly("org.codehaus.janino:janino")
    runtimeOnly("com.vayapay.common:logging-utils:$loggingUtilsVersion")
    runtimeOnly("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config:$awSpringVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")

}
