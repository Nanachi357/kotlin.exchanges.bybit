

plugins {
    kotlin("jvm") version "latest.release"
    application
}

group = "com.github.nanachi357"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-okhttp:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-client-websockets:2.3.7")
    implementation("io.ktor:ktor-serialization-gson:2.3.7")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.json:json:20231013")
    
    // === LOGGING === (same as in working project)
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
    
    // Test logging configuration (same as in working project)
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

// Gradle logging configuration - using built-in system

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.github.nanachi357.exchanges.bybit.ApplicationKt")
}
