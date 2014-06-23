package com.rapidminer.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerJavaBasicsPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.configure(project) {
			apply plugin: 'java'
			apply plugin: 'base'

			// minimize changes, at least for now (gradle uses 'build' by default)
			buildDir = "target"

			// ###################
			// Create Maven like provided configuration
			// See http://issues.gradle.org/browse/GRADLE-784
			configurations { provided }

			sourceSets {
				main.compileClasspath += configurations.provided
				test.compileClasspath += configurations.provided
				test.runtimeClasspath += configurations.provided
			}

			eclipse.classpath.plusConfigurations += configurations.provided
			// ####################

			// declare java version compatibility
			sourceCompatibility = 1.7
			targetCompatibility = 1.7
		}

	}

}
