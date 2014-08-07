## Introduction

The 'rapidminer-java-basics' bundels configuration settings for Java projects. 

* Ensure correct source and target compatibility level (1.7)
* Ensure UTF-8 encoding when compiling Java code
* Set build dir to 'target'
* Add JUnit as testCompile dependency
* Add tasks for creating source and javadoc Jars
* Define jar, source and javadoc Maven publications
* Adds Maven like 'provided' and 'optional' source sets via propdeps plugin

## How to use
	buildscript {
		dependencies {
			classpath 'com.rapidminer.gradle:java-basics:$VERSION'
		}
	}

	apply plugin: 'com.rapidminer.gradle.java-basics'
	
## Applied Plugins
* java
* eclipse
* maven-publish (http://www.gradle.org/docs/current/userguide/publishing_maven.html)
* propdeps (https://github.com/spring-projects/gradle-plugins/tree/master/propdeps-plugin)
* propdeps-eclipse

## Added Tasks

#####sourceJar
Creates a Jar containing all Project sources

#####javadocJar
Creates a Jar containing the project's JavaDoc