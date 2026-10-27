import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.repositories

plugins {
    kotlin("jvm").version("2.1.21")

    `java-gradle-plugin`
    id("maven-publish")
    id("com.gradle.plugin-publish") version "2.0.0"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

group = "no.nav.tms"

val libraryVersion: String = properties["lib_version"]?.toString() ?: "latest-local"

gradlePlugin {
    website.set("https://github.com/navikt/tms-jar-bundling")
    vcsUrl.set("https://github.com/navikt/tms-jar-bundling")

    plugins {
        create("jarBundling") {
            id = "no.nav.tms.jarBundling"
            implementationClass = "no.nav.tms.jar.bundling.JarBundling"
            version = libraryVersion

            displayName = "Gradle Jar Bundling Plugin"
            description = "Simple utility plugin for bundling thin app jar and its dependency-jars in the same output folder"
            tags = listOf("jar", "build")
        }
    }
}

publishing {
    repositories{
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/navikt/tms-jar-bundling")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
