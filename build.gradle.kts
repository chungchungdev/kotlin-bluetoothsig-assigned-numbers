plugins {
  id(libs.plugins.kotlin.jvm.get().pluginId) version libs.versions.kotlin
  id(libs.plugins.kotlin.serialization.get().pluginId) version libs.versions.kotlin
}

group = "com.chungchungdev"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  implementation(libs.kotlinpoet)
  implementation(platform(libs.ktor.bom))
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.contentNegotiation)
  implementation(libs.ktor.kotlinxSerialization.json)
  implementation(libs.kotlinx.datetime)
  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kaml)
  testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(17) }
