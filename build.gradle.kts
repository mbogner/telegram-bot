/*
 * Copyright 2022 mbodev @ https://mbo.dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // https://kotlinlang.org/docs/releases.html
    kotlin("jvm") version "1.9.22" apply (false)
    kotlin("plugin.spring") version "1.9.22" apply (false)
    kotlin("kapt") version "1.9.22" apply (false)

    // https://plugins.gradle.org/plugin/org.springframework.boot
    id("org.springframework.boot") version "3.2.1" apply (false)
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.1.4"

    // https://plugins.gradle.org/plugin/org.openapi.generator
    id("org.openapi.generator") version "7.2.0" apply (false)
    // https://plugins.gradle.org/plugin/com.rameshkp.openapi-merger-gradle-plugin
    id("com.rameshkp.openapi-merger-gradle-plugin") version "1.0.5" apply (false)
}

val javaVersion = JavaVersion.VERSION_21
val javaVersionStr = "21"

val springCloudVersion: String by project

allprojects {
    group = findProperty("mavenGroup")!!

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }

    apply(plugin = "io.spring.dependency-management")
    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
        resolutionStrategy {
            cacheChangingModulesFor(0, "seconds")
        }
        dependencies {
            // https://mvnrepository.com/artifact/org.mapstruct/mapstruct
            dependency("org.mapstruct:mapstruct:1.5.5.Final")
            // https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor
            dependency("org.mapstruct:mapstruct-processor:1.5.5.Final")

            // https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations
            dependency("io.swagger.core.v3:swagger-annotations:2.2.19")
            // https://mvnrepository.com/artifact/io.swagger/swagger-annotations
            dependency("io.swagger:swagger-annotations:1.6.12")
            // https://mvnrepository.com/artifact/com.google.code.findbugs/jsr305
            dependency("com.google.code.findbugs:jsr305:3.0.2")
            // https://mvnrepository.com/artifact/org.openapitools/jackson-databind-nullable
            dependency("org.openapitools:jackson-databind-nullable:0.2.6")
            // https://mvnrepository.com/artifact/com.github.scribejava/scribejava-core
            dependency("com.github.scribejava:scribejava-core:8.3.3")

            // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
            dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-reactor
            dependency("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")

        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersionStr
        }
    }

    tasks.withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

tasks.withType<Wrapper> {
    // https://gradle.org/releases
    gradleVersion = "8.5"
    distributionType = Wrapper.DistributionType.BIN
}