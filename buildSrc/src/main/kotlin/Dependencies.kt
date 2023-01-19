object Dependencies {
    // ktlint
    const val KTLINT = "com.pinterest:ktlint:${DependencyVersions.KTLINT_VERSION}"

    // webflux
    const val WEBFLUX = "org.springframework.boot:spring-boot-starter-webflux"

    // validation
    const val VALIDATION = "org.springframework.boot:spring-boot-starter-validation"

    // Coroutine & Reactor
    const val COROUTINE_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${DependencyVersions.COROUTINE_VERSION}"
    const val REACTOR_COROUTINE_EXTENSION = "io.projectreactor.kotlin:reactor-kotlin-extensions"
    const val COROUTINE_REACTOR = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${DependencyVersions.COROUTINE_VERSION}"
    const val COROUTINE_JDK = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${DependencyVersions.COROUTINE_VERSION}"
    const val KOTLINX_COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependencyVersions.COROUTINE_VERSION}"
    const val REACTOR_TEST = "io.projectreactor:reactor-test"

    // jdsl
    const val MUTINY_KOTLIN = "io.smallrye.reactive:mutiny-kotlin:${DependencyVersions.MUTINY_VERSION}"
    const val MUTINY_REACTOR = "io.smallrye.reactive:mutiny-reactor:${DependencyVersions.MUTINY_VERSION}"
    const val REACTIVE_JDSL = "com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:${DependencyVersions.JDSL_VERSION}"
    const val REACTIVE_MYSQL = "io.vertx:vertx-mysql-client:${DependencyVersions.REACTIVE_MYSQL_VERSION}"
    const val REACTIVE_HIBERNATE = "org.hibernate.reactive:hibernate-reactive-core:${DependencyVersions.HIBERNATE_REACTIVE_VERSION}"
    const val SPRING_DATA_COMMON = "org.springframework.data:spring-data-commons"

    // kotlin
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin"

    // time base uuid
    const val UUID_V7 = "com.github.f4b6a3:uuid-creator:${DependencyVersions.UUID_V7_VERSION}"

    // redis
    const val REACTIVE_REDIS = "org.springframework.boot:spring-boot-starter-data-redis-reactive"

    // security
    const val SPRING_SECURITY = "org.springframework.boot:spring-boot-starter-security"

    // cloud
    const val SPRING_CLOUD = "org.springframework.cloud:spring-cloud-dependencies:${DependencyVersions.SPRING_CLOUD_VERSION}"

    // sentry
    const val SENTRY = "io.sentry:sentry-spring-boot-starter:${DependencyVersions.SENTRY_VERSION}"

    // cloud config
    const val CLOUD_CONFIG = "org.springframework.cloud:spring-cloud-config-client"
}
