rootProject.name = "t-task"

pluginManagement {
	repositories {
		jcenter()
		gradlePluginPortal()
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
				useVersion(gradle.rootProject.extra["kotlin.version"] as String)
			}
		}
	}
}
