plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

group = "dev.citc.samples"
version = "0.0.1-SNAPSHOT"

configurations {
    all {
        resolutionStrategy.dependencySubstitution.all {
            val moduleSelector = requested as? ModuleComponentSelector
            if (moduleSelector?.module == "spring-boot-starter-tomcat") {
                useTarget(
                    "${moduleSelector.group}:spring-boot-starter-jetty:${moduleSelector.version}",
                    "We need Jetty"
                )
            }
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")

    runtimeOnly("org.postgresql:postgresql")

    runtimeOnly("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.zonky.test:embedded-database-spring-test:2.4.0")
    testImplementation(platform("io.zonky.test.postgres:embedded-postgres-binaries-bom:16.1.1"))
    testImplementation("io.zonky.test:embedded-postgres:2.0.6")

    testImplementation("net.javacrumbs.json-unit:json-unit:3.2.2")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

jib {
    from {
        image = "azul/zulu-openjdk:21"
    }
    to {
        setImage(provider { "ghcr.io/cit-consulting/$name:$version" })
    }
    container {
        environment = mapOf(
            "JAVA_TOOL_OPTIONS" to listOf(
                "-Xshare:off",
                "-XX:MaxRAMPercentage=40",
                "--enable-preview",
            ).joinToString(" ")
        )
        user = "nobody"
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            javaParameters = true
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        }
    }
    withType<Test> {
        jvmArgs("--enable-preview")
        useJUnitPlatform()
    }
    bootRun {
        jvmArgs("--enable-preview", "-Xmx512M", "-Dspring.profiles.active=development")
    }
}

repositories {
    mavenCentral()
}
