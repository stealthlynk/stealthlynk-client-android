buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
}

// Note: repositories are now configured in settings.gradle.kts

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
