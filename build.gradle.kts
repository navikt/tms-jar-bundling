import org.gradle.api.tasks.testing.logging.TestExceptionFormat

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

version = "0.0.1"
group = "no.nav.tms"

gradlePlugin {
    website.set("https://github.com/navikt/tms-jar-bundling")
    vcsUrl.set("https://github.com/navikt/tms-jar-bundling")

    plugins {
        create("thinJarBundling") {
            id = "no.nav.tms.thinJarBundling"
            implementationClass = "no.nav.tms.jar.bundling.ThinJarBundling"

            displayName = "Gradle Thin Jar Bundling Plugin"
            description = "Simple utility plugin for bundling thin app jar with its dependencies in the same output folder"
            tags.set(listOf("jar", "thin", "build"))
        }
    }
}

repositories {
    mavenLocal()
}

