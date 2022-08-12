val commonUtilsVersion: String by project
val kotlinSerializationVersion: String by project

plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	//id("org.springframework.boot")

	id("org.springframework.boot") apply false
	id("io.spring.dependency-management")
}

sourceSets {
	create("moduleTest") {
		withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
			kotlin.srcDir("$projectDir/src/moduleTest/kotlin")
			resources.srcDir("$projectDir/src/moduleTest/resources")
			compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
			runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
		}
	}
}

task<Test>("moduleTest") {
	description = "Runs the module tests"
	group = "verification"
	testClassesDirs = sourceSets["moduleTest"].output.classesDirs
	classpath = sourceSets["moduleTest"].runtimeClasspath

	// Set tags for cucumber on which need to be filtered, use and/or to combine tags. Scenarios with tag @Ignore
	// are automatically ignored.
	if (System.getProperty("cucumber.filter.tags").isNullOrEmpty()) {
		systemProperty("cucumber.filter.tags", "not @Ignore")
	} else {
		systemProperty("cucumber.filter.tags", System.getProperty("cucumber.filter.tags") + " and not @Ignore")
	}
	systemProperty("spring.profiles.active", System.getProperty("spring.profiles.active"))

	mustRunAfter(tasks["test"])
	useJUnitPlatform()
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {

	testImplementation(project(":service"))

	testImplementation("org.springframework.boot:spring-boot-starter-rsocket")

	testRuntimeOnly("com.h2database:h2")
	testImplementation("io.r2dbc:r2dbc-h2")
	testImplementation("com.vayapay.common:common-rsocketMock:$commonUtilsVersion")

	// Dependencies for cucumber tests
	testImplementation("com.vayapay.common:common-cucumber:$commonUtilsVersion")
	testImplementation("org.junit.vintage:junit-vintage-engine")
}

