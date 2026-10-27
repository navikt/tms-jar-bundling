# tms-jar-bundling

Liten plugin for å legge en applikasjons dependency-jars i samme mappe som output. Dette er et alternativ til å pakke alle dependencies
i samme jar som applikasjonen (fat jars).

## Oppsett og bruk

Denne plugin-en ligger ikke i sentralt plugin-repository, men kan hentes fra githubs package-repo. 
Dette krever at en legger til det som kilde til plugins.

Eksempel i `settings.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release"))
    }
}
```

Deretter kan en bruke plugin i `build.gradle.kts`:

```kotlin
plugins {
    ..
    
    id("no.nav.tms.jarBundling") version "0.1.0"
}
```

## Konfigurasjon

`jarBundling` antar at applikasjonen heter `app.jar` og plasseres i `/libs` i build-mappen.

Denne task-en kan konfigureres i `build.gradle.kts` som følger:

```kotlin
jarBundling {
    applicationName = "annet-navn"
    outputDirectory = "annen/mappe"
}
```
