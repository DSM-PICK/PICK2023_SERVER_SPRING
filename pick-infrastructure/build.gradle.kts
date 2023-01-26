plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

dependencyManagement {
    imports {
        mavenBom(Dependencies.SPRING_CLOUD)
    }
}

dependencies {
    implementation(Dependencies.WEB)
    implementation(Dependencies.VALIDATION)
    implementation(Dependencies.SPRING_SECURITY)
    implementation(Dependencies.JPA)
    runtimeOnly(Dependencies.MYSQL)
    implementation(Dependencies.CLOUD_CONFIG)
    implementation(Dependencies.OPENFEIGN)
    implementation(Dependencies.JACKSON)
    implementation(Dependencies.UUID_V7)
    implementation(Dependencies.QUERYDSL)
    kapt(Dependencies.QUERYDSL_PROCESSOR)

    implementation(project(":pick-application"))
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
