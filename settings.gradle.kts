pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io") // ⬅️ WAJIB
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // <- GANTI INI
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // ⬅️ HARUS ADA
    }
}

rootProject.name = "AplikasiStunting"
include(":app")