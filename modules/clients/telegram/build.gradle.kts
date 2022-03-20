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
    id("org.openapi.generator")
}

dependencies {
    api(project(":client-common"))
    testImplementation(project(":common-test"))
}

val openAPIBasePackage = "dev.mbo.telegrambot.client.telegram"
val openAPISpecFilePath: String by project
val openAPIGenOutBase: String by project
val openAPISpecFileNamePrefix: String by project
val openAPISpecFileName = "${openAPISpecFileNamePrefix}${project.name}"
val openAPISpecFileExt: String by project

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin", "${project.buildDir}/$openAPIGenOutBase/src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

openApiGenerate {
    // https://openapi-generator.tech/docs/generators/java/
    generatorName.set("java")
    library.set("feign")
    templateDir.set("$projectDir/$openAPISpecFilePath/templates")

    inputSpec.set("$projectDir/$openAPISpecFilePath/$openAPISpecFileName.$openAPISpecFileExt")
    outputDir.set("$buildDir/$openAPIGenOutBase")

    packageName.set(openAPIBasePackage)
    apiPackage.set("$openAPIBasePackage.api")
    invokerPackage.set("$openAPIBasePackage.client")
    modelPackage.set("$openAPIBasePackage.model")

    modelNameSuffix.set("Dto")
    typeMappings.set(
        mapOf(
            "OffsetDateTime" to "Instant"
        )
    )
    importMappings.set(
        mapOf(
            "java.time.OffsetDateTime" to "java.time.Instant"
        )
    )
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "useBeanValidation" to "true",
            "performBeanValidation" to "true",
            "delegatePattern" to "true",
            "hideGenerationTimestamp" to "true",
            "useTags" to "true",
            "configPackage" to "${openAPIBasePackage}.config",
        )
    )
}

val openApiGenerateTask = tasks.getByName("openApiGenerate")
val compileKotlinTask = tasks.getByName("compileKotlin")
compileKotlinTask.dependsOn(openApiGenerateTask)
