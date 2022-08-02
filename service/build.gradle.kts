val commonUtilsVersion: String by project
val hsmProxyVersion: String by project
val jUnit: String by project
val logbackEncoder: String by project
val r2dbcVersion: String by project
val berTlvVersion: String by project
val springMockkVersion: String by project
val kotlinVersion: String by project
val shedLockVersion: String by project
val loggingUtilsVersion: String by project
val awSpringVersion: String by project

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

//    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))
//    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("org.springframework.boot:spring-boot-starter")

	implementation("org.springframework.boot:spring-boot-starter-rsocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.core:jackson-databind:[2.13.2.1,3)") {
		because("CVE-2020-36518")
	}

	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("com.github.ben-manes.caffeine:caffeine")
	implementation(platform("io.r2dbc:r2dbc-bom:$r2dbcVersion"))
	implementation("io.r2dbc:r2dbc-spi")
	implementation("io.r2dbc:r2dbc-postgresql") {
		exclude(group = "io.netty.incubator", module = "netty-incubator-codec-classes-quic")
		exclude(group = "io.netty.incubator", module = "netty-incubator-codec-native-quic")
	}
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
	implementation("com.payneteasy:ber-tlv:$berTlvVersion")
	implementation("com.vayapay.common:common-utils:$commonUtilsVersion")
	implementation("com.vayapay.hsmproxy:hsm-proxy-client:$hsmProxyVersion")
	implementation("com.vayapay.hsmproxy:hsm-proxy-common:$hsmProxyVersion")
	implementation("net.javacrumbs.shedlock:shedlock-spring:$shedLockVersion")
	implementation("net.javacrumbs.shedlock:shedlock-provider-r2dbc:$shedLockVersion")

	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
	runtimeOnly("org.flywaydb:flyway-core")
	runtimeOnly("org.springframework:spring-jdbc")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	implementation("io.rsocket:rsocket-micrometer")
	// Dependencies for formatting logs in Kibana friendly format
	runtimeOnly("net.logstash.logback:logstash-logback-encoder:$logbackEncoder")
	runtimeOnly("org.codehaus.janino:janino")
	runtimeOnly("com.vayapay.common:logging-utils:$loggingUtilsVersion")
	runtimeOnly("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config:$awSpringVersion")
	testImplementation("io.r2dbc:r2dbc-h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
	//testImplementation(project(":card-data-service-client"))

}
