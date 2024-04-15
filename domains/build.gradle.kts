plugins {
    java
}

subprojects {
    apply(plugin = "java")

    dependencies {
        implementation(project(":utils"))
    }
}
