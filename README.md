## Introduction
* Ensure correct source and target compatibility level (currently: 1.7)
* Ensure UTF-8 encoding when compiling Java code
* Adds JUnit as testCompile dependency
* Adds 'external' source set which is also compiled into generated Jar
* Adds 'src/generated' source folders to main source set 

## How to use (requires Gradle 2.1+)
	plugins {
		id 'com.rapidminer.java-basics' version «plugin version»
	}
	
## Applied Plugins
* java
* eclipse

## Added Tasks
_Adds no tasks_