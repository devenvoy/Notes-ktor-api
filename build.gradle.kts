
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.devansh"
version = "0.0.1"

application {
    mainClass.set("com.devansh.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.bundles.ktor.server)

    implementation(libs.ktor.jackson)
    implementation(libs.commons.codec)

    implementation(libs.bundles.exposed)
    implementation(libs.bundles.database)
    implementation(libs.bundles.koin)
    implementation(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
