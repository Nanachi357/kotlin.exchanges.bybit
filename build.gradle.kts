plugins {
    kotlin("jvm") version "latest.release"
    kotlin("plugin.serialization") version "latest.release"
    application
}

group = "com.github.nanachi357"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:+")
    implementation("io.ktor:ktor-client-okhttp:+")
    implementation("io.ktor:ktor-client-content-negotiation:+")
    implementation("io.ktor:ktor-serialization-kotlinx-serialization:+")
    implementation("ch.qos.logback:logback-classic:+")
    implementation("org.json:json:+")
    
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.github.nanachi357.exchanges.bybit.MainKt")
}
