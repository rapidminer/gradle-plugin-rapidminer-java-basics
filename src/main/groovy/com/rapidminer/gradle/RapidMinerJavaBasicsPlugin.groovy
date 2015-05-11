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
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.ExcludeRule
import org.gradle.api.plugins.JavaPlugin



/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerJavaBasicsPlugin implements Plugin<Project> {

	private static final String ENCODING = 'UTF-8'
	private static final String JAVA_COMPATIBILITY = JavaVersion.VERSION_1_7

	@Override
	void apply(Project project) {

		project.configure(project) {
			apply plugin: 'java'
			apply plugin: 'eclipse'
			apply plugin: 'idea'
			apply plugin: 'provided-base'


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

			// set compilation encoding
			compileJava.options.encoding = ENCODING

			tasks.withType(JavaCompile) { options.encoding = ENCODING }

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
					if(output.exists()) {
						output.lastModified = timestamp
						if(output.isDirectory()){
							output.eachFile { File f ->
								f.lastModified = timestamp
							}
						}
					}
				}
			}
			check.dependsOn(updateTestTimestamps)

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
					logger.info('Cannot configure shadowJar task. Project does not apply shadow plugin.')
				}
			}
		}
	}
}
