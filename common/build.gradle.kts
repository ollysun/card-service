val commonUtilsVersion: String by project
val kotlinSerializationVersion: String by project

plugins {
	kotlin("plugin.serialization")
}

publishing {
	publications {
		create<MavenPublication>("library") {
			from(components["java"])
		}
	}

	repositories {
		maven {
			url = uri(
				System.getenv("CI_API_V4_URL") + "/projects/"
						+ System.getenv("CI_PROJECT_ID") + "/packages/maven"
			)
			name = "GitLab"
			credentials(HttpHeaderCredentials::class) {
				name = "Job-Token"
				value = System.getenv("CI_JOB_TOKEN")
			}
			authentication {
				create<HttpHeaderAuthentication>("header")
			}
		}
	}
}

dependencies {
	implementation("com.vayapay.common:common-utils:$commonUtilsVersion")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")

	testImplementation("org.apache.commons:commons-lang3:3.12.0")
}
