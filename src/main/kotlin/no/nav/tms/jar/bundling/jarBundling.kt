package no.nav.tms.jar.bundling

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import java.io.File

abstract class JarBundlingConf {
    abstract val outputDirectory: Property<String>
    abstract val applicationName: Property<String>
}

abstract class JarBundling : Plugin<Project> {

    override fun apply(target: Project) {

        val conf = target.extensions.create("jarBundling", JarBundlingConf::class.java).apply {
            outputDirectory.convention("libs")
            applicationName.convention("app")
        }

        val configureTask = target.tasks.register("configureJar", ConfigureJarTask::class.java) {
            it.application = target
            it.applicationName.set(conf.applicationName)

            val classes = target.tasks.named("classes")

            it.dependsOn(classes)
        }

        val packageTask = target.tasks.register("packageJar", BundleJarsTask::class.java) {
            it.application = target
            it.outputDirectory.set(conf.outputDirectory)
        }

        target.tasks.withType(Jar::class.java) {
            it.dependsOn(configureTask)
            it.finalizedBy(packageTask)
        }
    }
}

abstract class ConfigureJarTask : DefaultTask() {
    @Input
    lateinit var application: Project

    @get:Input
    abstract val applicationName: Property<String>

    @TaskAction
    fun action() {
        application.tasks.withType(Jar::class.java) { jar ->

            val javaApplication = application.extensions.getByType(JavaApplication::class.java)

            val mainClassName = javaApplication.mainClass

            jar.archiveBaseName.set(applicationName.get())
            jar.manifest { manifest ->
                val classpath = application.configurations.getByName("runtimeClasspath")
                manifest.attributes["Main-Class"] = mainClassName
                manifest.attributes["Class-Path"] = classpath.joinToString(separator = " ") {
                    it.name
                }
            }
        }
    }
}

abstract class BundleJarsTask : DefaultTask() {
    @Input
    lateinit var application: Project

    @get:Input
    abstract val outputDirectory: Property<String>

    @TaskAction
    fun action() {
        val classpath = application.configurations.getByName("runtimeClasspath")

        classpath.forEach {
            val file = File("${application.layout.buildDirectory.get()}/${outputDirectory.get()}/${it.name}")
            if (!file.exists()) it.copyTo(file)
        }
    }
}
