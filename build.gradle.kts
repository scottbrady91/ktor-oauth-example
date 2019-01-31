import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.scottbrady91.ktor.oauth"
version = "1.0-SNAPSHOT"

val ktor_version = "1.1.2"

plugins {
    java
    kotlin("jvm") version "1.3.11"
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("io.ktor:ktor-auth:$ktor_version")
    compile("io.ktor:ktor-client-apache:$ktor_version")
    compile("ch.qos.logback:logback-classic:1.2.3")
    testCompile("junit", "junit", "4.12")
}