// settings.gradle.kts (Perbaikan)
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal() // Baris ini adalah kunci untuk memperbaiki error
    }
}

rootProject.name = "mutasi-stok-app"