/*
 * Copyright 2013-2014 RapidMiner GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rapidminer.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.tasks.javadoc.Javadoc



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

			// Configure 'external' source set
			sourceSets {
				external {
					java { srcDir 'src/external/java/' }
					resources { srcDir 'src/external/resources/' }
				}
				main {
					compileClasspath += external.output
					runtimeClasspath += external.output
				}
			}

			// Ensure that external source set will be added to the jar
			jar { from sourceSets.external.output }

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
			tasks.create(name: 'updateTestTimestamps') << {
				def timestamp = System.currentTimeMillis()
				test.outputs.files.each { File output ->
					output.eachFile { File f ->
						f.lastModified = timestamp
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

			/*
			 * Configure an artifact which contains the classes from the test source set
			 */
			configurations { testArtifacts.extendsFrom testRuntime }


			// create and configure testJar tasl
			tasks.create(name: 'testJar', type: org.gradle.api.tasks.bundling.Jar) {
				classifier 'test'
				from sourceSets.test.output
			}

			// specify artifacts
			artifacts {
				jar
				sourceJar
				javadocJar
				testArtifacts testJar
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

			project.afterEvaluate {
				// Check if project contains shadowJar task and
				// configure it to use external sourceSet as well
				try {
					// check if task exists
					project.tasks.getByName('shadowJar')
					// configure task
					shadowJar { from sourceSets.external.output }
				} catch(UnknownTaskException e){
					logger.info('Cannot configure shadowJar task. Project does not apply shadow plugin.', e)
				}
			}
		}
	}
}
