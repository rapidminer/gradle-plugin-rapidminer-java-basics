package com.rapidminer.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerJavaBasicsPlugin implements Plugin<Project> {

	private static final String ENCODING = 'UTF-8'
	private static final String JAVA_COMPATIBILITY = 1.7
	private static final String DEFAULT_BUILD_DIR = "target"

	@Override
	void apply(Project project) {
		project.configure(project) {
			apply plugin: 'java'
			apply plugin: 'base'
			apply plugin: 'eclipse'

			// set compilation encoding
			compileJava.options.encoding = ENCODING

			tasks.withType(org.gradle.api.tasks.compile.JavaCompile) { options.encoding = ENCODING }

			// minimize changes, at least for now (gradle uses 'build' by default)
			buildDir = DEFAULT_BUILD_DIR

			// declare java version compatibility
			sourceCompatibility = JAVA_COMPATIBILITY
			targetCompatibility = JAVA_COMPATIBILITY

			// add unit test settings
			dependencies { testCompile 'junit:junit:4.11' }

			test {
				ignoreFailures = true

				// set system properties, as they are null by default
				systemProperties = System.properties
			}

			// create sourceJar task
			tasks.create(name: 'sourceJar', type: org.gradle.api.tasks.bundling.Jar)

			// configure sourceJar task
			sourceJar {
				from sourceSets.main.allSource
				classifier = 'sources'
			}

			// create javadocJar task
			tasks.create(name: 'javadocJar', type: org.gradle.api.tasks.bundling.Jar, dependsOn: javadoc)

			// configure javaDoc jar task
			task javadocJar {
				classifier = 'javadoc'
				from javadoc.destinationDir
			}

			// ###################
			// Create Maven like provided configuration
			// See http://issues.gradle.org/browse/GRADLE-784
			configurations { provided }

			afterEvaluate {
				sourceSets {
					main.compileClasspath += configurations.provided
					test.compileClasspath += configurations.provided
					test.runtimeClasspath += configurations.provided
				}
			}
		}

	}

}
