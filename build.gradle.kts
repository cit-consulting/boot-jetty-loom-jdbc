plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("com.google.cloud.tools.jib") version "3.3.1"
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
            /* Attempt to use Jetty 12 fails Boot autoconfiguration
            if (moduleSelector?.group == "org.eclipse.jetty") {
                val notation = when (moduleSelector.module) {
                    "jetty-servlets" -> "org.eclipse.jetty.ee10:jetty-ee10-servlets"
                    "jetty-servlet" -> "org.eclipse.jetty.ee10:jetty-ee10-servlet"
                    "jetty-annotations" -> "org.eclipse.jetty.ee10:jetty-ee10-annotations"
                    "jetty-webapp" -> "org.eclipse.jetty.ee10:jetty-ee10-webapp"
                    else -> "${moduleSelector.group}:${moduleSelector.module}"
                }
                useTarget("$notation:12.0.0.beta0")
            }
            */
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
    // TODO https://github.com/spring-projects/spring-framework/issues/29585
    runtimeOnly("org.eclipse.jetty.toolchain:jetty-jakarta-servlet-api:5.0.2")

    runtimeOnly("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.zonky.test:embedded-database-spring-test:2.3.0")
    testImplementation(platform("io.zonky.test.postgres:embedded-postgres-binaries-bom:15.3.0"))
    testImplementation("io.zonky.test:embedded-postgres:2.0.4")

    testImplementation("net.javacrumbs.json-unit:json-unit:2.38.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.3.0")
}

java {
    toolchain {
        // TODO use 20 when kotlin supports it
        languageVersion.set(JavaLanguageVersion.of(19))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

jib {
    from {
        image = "azul/zulu-openjdk:20"
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
