object Dependencies {
    // ktlint
    const val KTLINT = "com.pinterest:ktlint:${DependencyVersions.KTLINT_VERSION}"

    // validation
    const val VALIDATION = "org.springframework.boot:spring-boot-starter-validation"

    // kotlin
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin"

    // security
    const val SPRING_SECURITY = "org.springframework.boot:spring-boot-starter-security"

    // cloud
    const val SPRING_CLOUD = "org.springframework.cloud:spring-cloud-dependencies:${DependencyVersions.SPRING_CLOUD_VERSION}"

    // sentry
    const val SENTRY = "io.sentry:sentry-spring-boot-starter:${DependencyVersions.SENTRY_VERSION}"

    // cloud config
    const val CLOUD_CONFIG = "org.springframework.cloud:spring-cloud-config-client"

    // web
    const val WEB = "org.springframework.boot:spring-boot-starter-web"

    // jpa
    const val JPA = "org.springframework.boot:spring-boot-starter-data-jpa"

    // queryDSL
    const val QUERYDSL = "com.querydsl:querydsl-jpa:${DependencyVersions.QUERYDSL}"
    const val QUERYDSL_PROCESSOR = "com.querydsl:querydsl-apt:${DependencyVersions.QUERYDSL}:jpa"

    // database
    const val MYSQL = "mysql:mysql-connector-java"

    // open feign
    const val OPENFEIGN = "org.springframework.cloud:spring-cloud-starter-openfeign:${DependencyVersions.OPENFEIGN}"

    // message
    const val AWS_MESSAGING = "org.springframework.cloud:spring-cloud-starter-aws-messaging:${DependencyVersions.AWS_MESSAGING}"
}
