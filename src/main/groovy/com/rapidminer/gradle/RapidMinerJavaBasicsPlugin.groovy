package com.rapidminer.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerJavaBasicsPlugin implements Plugin<Project> {

	private static final String ENCODING = 'UTF-8'
	private static final String JAVA_COMPATIBILITY = JavaVersion.VERSION_1_7
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
			configurations { provided }

			sourceSets {
				main.compileClasspath += configurations.provided
				test.compileClasspath += configurations.provided
				test.runtimeClasspath += configurations.provided
			}

			//TODO add external source sets
			/*
			 * Extend the main source set by adding generated java and generated resources
			 */
			sourceSets {
				main {
					java { srcDir 'src/generated/java' }
					resources { srcDir 'src/generated/resources' }
				}
			}

			eclipse.classpath.plusConfigurations += [configurations.provided]

			// set compilation encoding
			compileJava.options.encoding = ENCODING

			tasks.withType(org.gradle.api.tasks.compile.JavaCompile) { options.encoding = ENCODING }

			// minimize changes, at least for now (gradle uses 'build' by default)
			buildDir = DEFAULT_BUILD_DIR

			// declare java version compatibility
			sourceCompatibility = JAVA_COMPATIBILITY
			targetCompatibility = JAVA_COMPATIBILITY

			// add unit test settings
			dependencies { testCompile 'junit:junit:4.+' }

			test {
				// set system properties, as they are null by default
				systemProperties = System.properties
			}

			// ensure that each Jenkins build sees updated test results (fails otherwise)
			tasks.create(name: 'updateTestTimestamps') {
				inputs.files test.outputs.files
				doLast{
					def timestamp = System.currentTimeMillis()
					if(test.hasProperty('testResultsDir')){
						test.testResultsDir?.eachFile { it.lastModified = timestamp }
					}
				}
			}
			check.dependsOn(updateTestTimestamps)

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

			// specify artifacts
			artifacts {
				jar
				sourceJar
				javadocJar
			}

			publishing {
				publications {
					jar(org.gradle.api.publish.maven.MavenPublication) { from components.java }
					sourceJar(org.gradle.api.publish.maven.MavenPublication) { artifact tasks.sourceJar }
					javadocJar(org.gradle.api.publish.maven.MavenPublication) { artifact tasks.javadocJar }
				}
			}

		}
	}
}