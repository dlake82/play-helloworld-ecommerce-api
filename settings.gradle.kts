rootProject.name = "decosk"
include("gateway:gateway")
include("gateway:discovery")

include("domains:users")
include("domains:products")
include("domains:catalogs")
include("domains:orders")
include("domains:favorites")
include("domains:histories")
include("domains:payments")
include("domains:reviews")
include("domains:supports")
include("utils")

include("configs")

//pluginManagement {
//    val SPRING_BOOT_VERSION: String by settings
//    val SPRING_BOOT_DEPENDENCY_MANAGEMENT_VERSION: String by settings
//    val KOTLIN_VERSION: String by settings
//
//    plugins {
//        id("org.springframework.boot") version SPRING_BOOT_VERSION
//        id("io.spring.dependency-management") version SPRING_BOOT_DEPENDENCY_MANAGEMENT_VERSION
//        kotlin("jvm") version KOTLIN_VERSION
//        kotlin("plugin.spring") version KOTLIN_VERSION
//    }
//}
include("utils")
