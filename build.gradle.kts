import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val javaVersion: String by project
val logbackEncoder: String by project
val springMockkVersion: String by project
val springCloudVersion: String by project

plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
    id("org.sonarqube")
    id("org.owasp.dependencycheck")
    id("io.spring.dependency-management")
    jacoco
}

apply(plugin = "io.spring.dependency-management")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

dependencyCheck {
    analyzers.assemblyEnabled = false
    failBuildOnCVSS = 1f
    val dataDir = System.getenv("DEPENDENCY_DATA_DIR")
    if (dataDir != null) {
        data.directory = dataDir
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "vayapay-root_card-identification")
        property("sonar.organization", "vayapay")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.check)
    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco.xml"))
    }
}
tasks.sonarqube {
    dependsOn("dependencyCheckAnalyze")
    dependsOn("jacocoTestReport")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = javaVersion
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
    }
}
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://gitlab.com/api/v4/groups/14703411/-/packages/maven")
        name = "GitLab"
        val ciToken = System.getenv("CI_JOB_TOKEN")
        credentials(HttpHeaderCredentials::class) {
            if (ciToken != null) {
                name = "Job-Token"
                value = ciToken
            } else {
                val gitLabPrivateToken: String by project
                name = "Private-Token"
                value = gitLabPrivateToken
            }
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.rsocket:rsocket-micrometer")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:$logbackEncoder")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
}