plugins {
    java
}

subprojects {
    group = "com.saysimple"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")

    dependencies {
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
