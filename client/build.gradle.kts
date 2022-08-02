import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val jUnit: String by project
val commonUtilsVersion: String by project

plugins {
	id("org.springframework.boot")
	kotlin("plugin.spring")
}

apply(plugin = "io.spring.dependency-management")

// Create Jar instead of executable spring boot application
tasks.bootJar {enabled = false}
tasks.jar {enabled = true}



dependencies {
	api(project(":card-identification-service-common"))

	implementation("org.springframework.boot:spring-boot-starter-rsocket")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	implementation("com.fasterxml.jackson.core:jackson-databind:[2.13.2.1,3)") {
		because("CVE-2020-36518")
	}

	//testImplementation(project(":stub"))
	//testImplementation("com.vayapay.common:common-rsocketMock:$commonUtilsVersion")

	// Dependencies required for cucumber
	testImplementation("com.vayapay.common:common-cucumber:$commonUtilsVersion")
	testImplementation("org.junit.vintage:junit-vintage-engine:$jUnit")


}


