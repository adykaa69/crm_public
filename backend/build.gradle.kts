val javaVersion: Int = project.property("javaVersion").toString().toInt()
val liquiBaseVersion: String by project
val postgresVersion: String by project
val mapstructVersion: String by project
val lombokVersion: String by project
val springDocVersion: String by project

plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "hu.bhr"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

tasks.jar{
    enabled = true
}

repositories {
    mavenCentral()
}


dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core:$liquiBaseVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // MapStruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // JUnit platform dependencies
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.test {
    useJUnitPlatform()
}