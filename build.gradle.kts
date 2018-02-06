import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.task"
version = "0.0.3-SNAPSHOT"

plugins {
	val springBootVersion = "1.5.10.RELEASE"
	java
	groovy
	id("org.jetbrains.kotlin.jvm")
	id("org.springframework.boot") version springBootVersion
	id("org.jetbrains.kotlin.plugin.allopen")
	id("org.jetbrains.kotlin.plugin.spring")
	id("org.jetbrains.kotlin.plugin.jpa")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
}

repositories {
	jcenter()
}

dependencies {
	listOf(
		"org.springframework.boot:spring-boot-starter-web",
		"org.springframework.boot:spring-boot-starter-security",
		"org.springframework.boot:spring-boot-starter-data-jpa",
		"org.springframework.boot:spring-boot-starter-thymeleaf",
		"org.thymeleaf.extras:thymeleaf-extras-springsecurity4",
		"com.h2database:h2",
		"javax.xml.bind:jaxb-api:2.3.0",
		kotlin("stdlib-jdk8"),
		kotlin("reflect"),
		"org.codehaus.groovy:groovy-all:2.4.13"
	).forEach { compile(it) }

	// Testing
	listOf(
		"org.spockframework:spock-core:1.1-groovy-2.4",
		"org.spockframework:spock-spring:1.1-groovy-2.4",
		"org.hamcrest:hamcrest-core:1.3",
		"org.springframework.boot:spring-boot-starter-test",
		"org.springframework.security:spring-security-test"
	).forEach { testImplementation(it) }

	listOf(
		"net.bytebuddy:byte-buddy:1.7.10",
		"org.objenesis:objenesis:2.6"
	).forEach { testRuntime(it) }
}
