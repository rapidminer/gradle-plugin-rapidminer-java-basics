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
			apply plugin: 'eclipse'
			apply plugin: 'maven-publish'

			// ###################
			// Used to create Maven like provided configuration
			// See http://issues.gradle.org/browse/GRADLE-784
			apply plugin: 'propdeps'
			apply plugin: 'propdeps-eclipse'

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
				// set system properties, as they are null by default
				systemProperties = System.properties
			}

			// create and configure sourceJar task
			tasks.create(name: 'sourceJar', type: org.gradle.api.tasks.bundling.Jar)
			sourceJar {
				from sourceSets.main.allSource
				classifier = 'sources'
			}

			// create and configure javadocJar task
			tasks.create(name: 'javadocJar', type: org.gradle.api.tasks.bundling.Jar, dependsOn: javadoc)
			javadocJar {
				classifier = 'javadoc'
				from javadoc.destinationDir
			}

			// create and configure testJar tasl
			tasks.create(name: 'testJar', type: org.gradle.api.tasks.bundling.Jar) {
				classifier = 'tests'
				from sourceSets.test.classes
			}

			// specify artifacts
			artifacts {
				jar
				sourceJar
				javadocJar
				testJar
			}

			publishing {
				publications {
					jar(org.gradle.api.publish.maven.MavenPublication) { from components.java }
					sourceJar(org.gradle.api.publish.maven.MavenPublication) { artifact tasks.sourceJar }
					javadocJar(org.gradle.api.publish.maven.MavenPublication) { artifact tasks.javadocJar }
					testJar(org.gradle.api.publish.maven.MavenPublication) { artifact tasks.testJar }
				}
			}

			// This disables the pedantic doclint feature of JDK8
			// see http://blog.joda.org/2014/02/turning-off-doclint-in-jdk-8-javadoc.html
			if (JavaVersion.current().isJava8Compatible()) {
				tasks.withType(Javadoc) {
					options.addStringOption('Xdoclint:none', '-quiet')
				}
			}


		}
	}
}