pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
rootProject.name = "host"
includeFlat("exchange")
includeFlat("repository-spring")
includeFlat("master-spring")