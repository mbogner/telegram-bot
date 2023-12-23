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
    api("com.squareup.okhttp3:okhttp")
    testImplementation(project(":common-test"))
}

// ---------------------------
val openApiBasePackage = "dev.mbo.telegrambot.client.telegram"
val openApiGenOutBase = "${layout.buildDirectory.get()}/generated/openapi"
val openApiSrcDir = "$projectDir/src/gen/kotlin"
val openApiPath = "$openApiSrcDir/${openApiBasePackage.replace(".", "/")}"

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin", openApiSrcDir)
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

openApiGenerate {
    // https://openapi-generator.tech/docs/generators/kotlin
    generatorName.set("kotlin")
    library.set("jvm-okhttp4")
    templateDir.set("$rootDir/modules/clients/client-common/src/main/openapi/kotlin")

    inputSpec.set("$projectDir/src/main/openapi/spec-client-telegram.yml")
    outputDir.set(openApiGenOutBase)

    packageName.set(openApiBasePackage)
    apiPackage.set("$openApiBasePackage.api")
    invokerPackage.set("$openApiBasePackage.client")
    modelPackage.set("$openApiBasePackage.model")

    modelNameSuffix.set("Dto")
    typeMappings.set(
        mapOf(
            "DateTime" to "Instant",
            "Timestamp" to "Instant"
        )
    )
    importMappings.set(
        mapOf(
            "Instant" to "java.time.Instant"
        )
    )
    // https://openapi-generator.tech/docs/generators/kotlin
    configOptions.set(
        mapOf(
            "serializationLibrary" to "jackson",
            "useSpringBoot3" to "true",
        )
    )
}

val openApiClean = tasks.create<Delete>("openApiClean") {
    group = "openapi tools"
    delete(openApiGenOutBase)
    delete(openApiPath)
}

val openApiGenerateTask = tasks.getByName("openApiGenerate")
openApiGenerateTask.dependsOn(openApiClean)

// copy generated files to src/gen folder
val copyGeneratedApiTask = tasks.register<Copy>("copyGeneratedApiTask") {
    from("$openApiGenOutBase/src/main/kotlin")
    into(openApiSrcDir)
    dependsOn(openApiGenerateTask)
}

// and get rid of stuff that isn't needed
val removeUnnecessaryApiFileTask = tasks.register<Delete>("removeUnnecessaryApiFileTask") {
    delete("$openApiPath/client")
    dependsOn(copyGeneratedApiTask)
}

// make compile dependent on the generation
val compileKotlinTask = tasks.getByName("compileKotlin")
compileKotlinTask.dependsOn(removeUnnecessaryApiFileTask)
