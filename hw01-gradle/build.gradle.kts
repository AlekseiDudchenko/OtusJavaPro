plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "de.dudchenko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:32.1.2-jre")
}

tasks.shadowJar {
    archiveBaseName.set("gradleHelloWorld")
    archiveVersion.set("0.1")
    archiveClassifier.set("")
    manifest {
        attributes ["Main-Class"] = "de.dudchenko.HelloOtus"
    }
}

tasks.test {
    useJUnitPlatform()
}