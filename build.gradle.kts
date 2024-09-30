plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "6.0.6"
  kotlin("plugin.spring") version "2.0.20"
  id("org.jetbrains.kotlin.plugin.noarg") version "2.0.20"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

repositories {
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}

dependencyCheck {
  suppressionFiles.add("dependency-check-suppress-json.xml")
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.0.7")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-cache")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.8.0")

  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
  implementation("org.springdoc:springdoc-openapi-starter-common:2.6.0")

  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.22")
  testImplementation("org.wiremock:wiremock-standalone:3.9.1")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.jsonwebtoken:jjwt:0.12.6")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}
