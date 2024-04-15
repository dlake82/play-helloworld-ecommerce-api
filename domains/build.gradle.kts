plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

subprojects {
    apply(plugin = "java")

    dependencies {
        implementation(project(":utils"))
    }
}
