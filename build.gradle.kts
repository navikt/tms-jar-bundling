import org.gradle.kotlin.dsl.repositories

plugins {
    kotlin("jvm").version("2.1.21")

    `java-gradle-plugin`
    `maven-publish`
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

val libraryVersion: String = properties["lib_version"]?.toString() ?: "0.1.0-latest-local"

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
    repositories {
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
