import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "1.9.0"
}

group = "dev.abhaycloud.jsonvisualizer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.google.code.gson:gson:2.12.1")
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "JSONVisualizer"
            packageVersion = "1.0.0"
            // Windows specific options
            windows {
                // Menu entries for Windows
                menu = true
                // Optional: Specify icon for Windows
//                iconFile.set(project.file("src/main/resources/icons/app-logo.ico"))
                // MSI package options
                upgradeUuid = "89758E3B-9B72-4A25-9D6B-6BD0FB550AC2"
            }
        }

    }
}

tasks.register("runCurrentOs") {
    dependsOn("run")
}
