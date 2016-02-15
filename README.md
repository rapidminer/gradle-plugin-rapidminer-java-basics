## Introduction
* Ensure correct source and target compatibility level (currently: 1.8)
* Ensure UTF-8 encoding when compiling Java code
* Adds JUnit (version 4.12) as testCompile dependency
* Adds 'external' source set which is also compiled into generated Jar
* Adds 'src/generated' source folders to main source set 

## How to use (requires Gradle 2.7+)
	plugins {
		id 'com.rapidminer.java-basics' version <<plugin version>>
	}
	
## Applied Plugins
* java
* eclipse
* idea
* [nebula.provided-base](https://github.com/nebula-plugins/gradle-extra-configurations-plugin)
* [nebula.optional-base](https://github.com/nebula-plugins/gradle-extra-configurations-plugin)

## Added Tasks
_Adds no tasks_