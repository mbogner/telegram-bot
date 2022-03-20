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

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("java-library")
    id("io.spring.dependency-management")
}

dependencies {
    api(project(":common"))

    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("io.github.openfeign:feign-httpclient")
    api("io.github.openfeign:feign-jackson")
    api("com.google.code.findbugs:jsr305")

    api("org.springframework.boot:spring-boot-starter-validation")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    api("io.swagger:swagger-annotations")

    testImplementation(project(":common-test"))
}

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin", "${project.buildDir}/generated/source/openapi/src/main/kotlin")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}