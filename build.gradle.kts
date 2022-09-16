import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val javaVersion: String by project
val logbackEncoder: String by project
val springMockkVersion: String by project
val springCloudVersion: String by project
val kotlinLoggingVersion: String by project
val kotlinCoroutinesVersion: String by project
val openCsvVersion: String by project

plugins {
    kotlin("plugin.serialization") apply false
    kotlin("jvm") apply false
    java
    id("org.sonarqube")
    id("org.owasp.dependencycheck")
    id("io.spring.dependency-management")
    jacoco
}
sonarqube {
    properties {
        property("sonar.projectKey", "vayapay-root_card-identification")
        property("sonar.organization", "vayapay")
    }
}
tasks.sonarqube {
    dependsOn("dependencyCheckAnalyze")
    dependsOn("jacocoTestReport")
}
subprojects {
    version = "1.0.0-SNAPSHOT"
    group = "com.vayapay.card-registration"
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.owasp.dependencycheck")
    apply(plugin = "jacoco")
    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
        }
    }
    configurations.all {
        exclude("org.apache.logging.log4j")
    }
    dependencies {
        implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
        implementation("com.opencsv:opencsv:$openCsvVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutinesVersion") {
            exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-core")
            exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-websocket")
            exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-el")
        }
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinCoroutinesVersion")


        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = javaVersion
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
    dependencyCheck {
        analyzers.assemblyEnabled = false
        analyzers.ossIndexEnabled = false
        failBuildOnCVSS = 1f
        val dataDir = System.getenv("DEPENDENCY_DATA_DIR")
        if (dataDir != null) {
            data.directory = dataDir
        }
        suppressionFile = File(rootDir, "suppressions.xml").absolutePath
    }
    val jaCoCoExclusions = listOf("com/vayapay/**/mock/**")
    tasks.jacocoTestReport {
        dependsOn(tasks.check)
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                exclude(jaCoCoExclusions)
            }
        }))
        reports {
            xml.required.set(true)
            xml.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco.xml"))
        }
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
}

