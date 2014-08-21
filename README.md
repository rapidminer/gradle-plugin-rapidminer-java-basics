## Introduction

The 'rapidminer-java-basics' bundels configuration settings for Java projects. 

* Ensure correct source and target compatibility level (1.7)
* Ensure UTF-8 encoding when compiling Java code
* Add JUnit as testCompile dependency
* Add tasks for creating source, javadoc, and test Jars
* Define jar (main, source,test) and javadoc Maven publications
* Adds 'external' source set which is also compiled into generated Jar
* Adds 'src/generated' source folders to main source set 

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

## Added Tasks

##### sourceJar
Creates a Jar containing all Project sources

##### javadocJar
Creates a Jar containing the project's JavaDoc

##### testJar
Creates a Jar containing the project's test classes