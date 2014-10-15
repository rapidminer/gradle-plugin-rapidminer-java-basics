## Introduction
* Ensure correct source and target compatibility level (1.7)
* Ensure UTF-8 encoding when compiling Java code
* Add JUnit as testCompile dependency
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

## Added Tasks