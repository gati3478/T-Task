import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.task"
version = "0.0.3-SNAPSHOT"

val kotlinVersion: String by extra

buildscript {
	val kotlinVersion: String by extra { "1.2.21" }
	val springBootVersion: String by extra { "1.5.9.RELEASE" }

	repositories {
		mavenCentral()
	}

	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
	}
}

plugins {
	java //id("java")
}

apply {
	plugin("java")
	plugin("kotlin")
	plugin("kotlin-spring")
	plugin("kotlin-jpa")
	plugin("groovy")
	plugin("org.springframework.boot")
	plugin("idea")
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
