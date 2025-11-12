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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repository.map.naver.com/archive/maven")  // 여기 값도 넣어야 받아옴
    }
}

rootProject.name = "Android7Hours"
include(":app")
include(":domain")
include(":feature")
include(":feature:common")
include(":feature:home")
include(":feature:monitor")
include(":feature:community")
include(":feature:mypage")
include(":feature:trail")
include(":data")
include(":feature:auth")
