## Change Log

#### 0.4.2

#### 0.4.1
* Updates 'provided' Nebula plugin to version 3.1.0

#### 0.4.0
* Changes Java target compatibility to version 1.8
* Updates 'provided' Nebula plugin to version 3.0.3
* Applies 'optional' Nebula plugin (with version 3.0.3)

#### 0.3.3
* Replaces own provided configuration with Nebula provided-base provided configuration.
* Fixes Junit version to '4.12' instead of '4.+'

#### 0.3.2
* Adds IntelliJ IDEA support

#### 0.3.1
* Adds shortened plugin name 'com.rapidminer.java-basics' to comply with plugins.gradle.org standards

#### 0.3.0
* Moved Java publishing related tasks to new Gradle plugin. 
  For sourceJar, javaDocJar and publications please apply 'com.rapidminer.gradle.java-publishing'

#### 0.2.6
* Do not show exception if shadowJar task does not exist
* Added test and sources artifact to main artifact publication 
* Changes buildDir from 'target/' to Gradle default ('build/')

#### 0.2.5
* Makes updateTestTimestamps task more robust

#### 0.2.4
* Adds Gradle 2.1 compatible plugin name 'com.rapidminer.gradle.java-basics'
* Adds 'external' sourceSet
* Adds testJar task and Maven publication
* Disables Java 8 JavaDoc doclint check
* Fixes updateTestTimestamps task

#### 0.2.3
* Adds 'generated' sourceSet which extends main sourceSet
* Adds updateTestTimestamps task

#### 0.2.2
* Replace Pivotal's prop-devs plugins by custom implementation

#### 0.2.1
* Use Pivotal's prop-devs plugin to generate provided and optional dependency scope

#### 0.2.0
* Appplies 'maven-publish' plugin
* Configures Jar, JavaDoc, and Sources publications
* Do not ignore test failures by default

#### 0.1.3
* Fix provided configuration for Gradle 2.0
* Adds JavaDoc and Sources Jar tasks

#### 0.1.2
* By default JUnit 4.11 will be added as testCompile dependency
* Set system properties when executing tests 
* By default test errors will be ignored (otherwise Jenkins will become red instead of yellow on test failures)  

#### 0.1.1
* Changes default compile encoding to UTF-8

#### 0.1.0 
* Extension release






