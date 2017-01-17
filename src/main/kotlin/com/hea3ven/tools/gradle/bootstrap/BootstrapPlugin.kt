package com.hea3ven.tools.gradle.bootstrap

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar

class BootstrapPlugin : Plugin<Project> {
	override fun apply(project: Project) {

		val sourceSets = project.findProperty("sourceSets") as SourceSetContainer
		sourceSets.getByName("main").java.srcDir("src/bootstrap/java")

		project.extensions.create("bootstrap", BootstrapPluginExtension::class.java)

		project.afterEvaluate {
			val jar = project.findProperty("jar") as Jar
			jar.manifest.attributes["H3NTBootstrap"] = getExtension(project).file
		}

		val releaseConf = project.configurations.create("release")
		releaseConf.isTransitive = false
	}

	private fun getExtension(project: Project) =
			project.extensions.getByType(BootstrapPluginExtension::class.java)
}

