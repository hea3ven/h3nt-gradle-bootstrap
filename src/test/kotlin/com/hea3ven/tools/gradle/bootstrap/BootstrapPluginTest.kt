package com.hea3ven.tools.gradle.bootstrap

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Test

class BootstrapPluginTest {
	@Test
	fun addsBootstrapSourceSet() {
		val proj = ProjectBuilder.builder().build()
		proj.pluginManager.apply("java")
		proj.pluginManager.apply("com.hea3ven.tools.gradle.bootstrap")

//		val srcDirs = proj.getProp<SourceSetContainer>("sourceSets").getByName("main").allJava.srcDirs
		val srcDirs = proj.getProp<SourceSetContainer>("sourceSets").getByName("main").java.srcDirs
		assertTrue(srcDirs.any { it.toString().endsWith("src/bootstrap/java") })
	}

	@Test
	fun addsManifestEntryInJar() {
		val proj = ProjectBuilder.builder().build()
		proj.pluginManager.apply("java")
		proj.pluginManager.apply("com.hea3ven.tools.gradle.bootstrap")
		proj.getProp<BootstrapPluginExtension>("bootstrap").file = "some/file.test"
		(proj as ProjectInternal).evaluate()

		val jar = proj.getProp<Jar>("jar")
		assertTrue(jar.manifest.attributes.containsKey("H3NTBootstrap"))
		assertEquals("some/file.test", jar.manifest.attributes.get("H3NTBootstrap"))
	}

	@Test
	fun addsReleaseConfiguration() {
		val proj = createProject()

		assertTrue(proj.configurations.names.contains("release"))
	}

	private fun createProject(): Project {
		val proj = ProjectBuilder.builder().build()
		proj.pluginManager.apply("java")
		proj.pluginManager.apply("com.hea3ven.tools.gradle.bootstrap")
		return proj
	}

	@Test
	fun releaseConfigurationIsNotTransitive() {
		val proj = ProjectBuilder.builder().build()
		proj.pluginManager.apply("java")
		proj.pluginManager.apply("com.hea3ven.tools.gradle.bootstrap")

		assertFalse(proj.configurations.getByName("release").isTransitive)
	}

	@Suppress("UNCHECKED_CAST")
	private fun <T> Project.getProp(name: String) = findProperty(name) as T
}
