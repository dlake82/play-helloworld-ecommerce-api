plugins {
    java
}

subprojects {
    apply(plugin = "java")

    dependencies {
        implementation(project(":common"))
        implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    }
}
