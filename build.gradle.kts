import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.task"
version = "0.0.3-SNAPSHOT"

val kotlinVersion: String = "1.2.21"

plugins {
	val kotlinVersion = "1.2.21"
	val springBootVersion = "1.5.10.RELEASE"
	id("java")
	id("org.jetbrains.kotlin.jvm") version kotlinVersion
	id("groovy")
	id("idea")
	id("org.springframework.boot") version springBootVersion
	id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
	id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
	id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
}

configure<JavaPluginConvention> {
	setSourceCompatibility(1.8)
	setTargetCompatibility(1.8)
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
}

repositories {
	mavenCentral()
}

dependencies {
	compile("org.springframework.boot:spring-boot-starter-web")
	compile("org.springframework.boot:spring-boot-starter-security")
	compile("org.springframework.boot:spring-boot-starter-data-jpa")
	compile("org.springframework.boot:spring-boot-starter-thymeleaf")
	compile("org.thymeleaf.extras:thymeleaf-extras-springsecurity4")
	compile("com.h2database:h2")
	compile("javax.xml.bind:jaxb-api:2.3.0")
	compile(kotlin("stdlib-jdk8", version = kotlinVersion))
	compile(kotlin("reflect", version = kotlinVersion))
	compile("org.codehaus.groovy:groovy-all:2.4.13")

	// Testing
	testCompile("org.spockframework:spock-core:1.1-groovy-2.4")
	testCompile("org.spockframework:spock-spring:1.1-groovy-2.4")
	testCompile("org.hamcrest:hamcrest-core:1.3")
	testRuntime("net.bytebuddy:byte-buddy:1.7.10")
	testRuntime("org.objenesis:objenesis:2.6")
	testCompile("org.springframework.boot:spring-boot-starter-test")
	testCompile("org.springframework.security:spring-security-test")
}
