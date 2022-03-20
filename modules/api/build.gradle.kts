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
    kotlin("kapt") // for mapstruct

    id("java-library")
    id("io.spring.dependency-management")

    id("com.rameshkp.openapi-merger-gradle-plugin") // merge openapi specs
    id("org.openapi.generator")
}

dependencies {
    api(project(":common"))

    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.boot:spring-boot-starter-validation")

    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    api("io.swagger:swagger-annotations")

    api("org.mapstruct:mapstruct")
    kapt("org.mapstruct:mapstruct-processor")

    testImplementation(project(":common-test"))
}

val openAPIBasePackage: String by project
val openAPISpecParts: String by project
val openAPISpecFilePath: String by project
val openAPIGenOutBase: String by project
val openAPISpecFileNamePrefix: String by project
val openAPISpecFileName = "${openAPISpecFileNamePrefix}${project.name}"
val openAPISpecFileExt: String by project
val openAPISpecFile = "$projectDir/$openAPISpecFilePath/$openAPISpecFileName.$openAPISpecFileExt"

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin", "${project.buildDir}/$openAPIGenOutBase/src/main/kotlin")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

val cleanOpenApiFile by tasks.register<Delete>("cleanOpenApiFile") {
    group = "open api merger"
    delete(openAPISpecFile)
}

openApiMerger {
    inputDirectory.set(file("${projectDir.path}/$openAPISpecParts"))
    output {
        directory.set(file("${projectDir.path}/$openAPISpecFilePath/"))
        fileName.set(openAPISpecFileName)
        fileExtension.set(openAPISpecFileExt)
    }
    openApi {
        openApiVersion.set("3.0.3")
        info {
            title.set("telegram bot api")
            description.set("api to work with the bot")
            version.set("v1")
            license {
                name.set("Apache License v2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }
        servers {
            register("dev") {
                url.set("https://localhost:6170")
                description.set("dev")
            }
        }
    }
}
val mergeOpenApiFilesTask = tasks.getByName("mergeOpenApiFiles")
mergeOpenApiFilesTask.dependsOn(cleanOpenApiFile)

openApiGenerate {
    // https://openapi-generator.tech/docs/generators/kotlin-spring
    generatorName.set("kotlin-spring")
    library.set("spring-boot")
    templateDir.set("$projectDir/$openAPISpecFilePath/templates")

    inputSpec.set(openAPISpecFile)
    outputDir.set("$buildDir/$openAPIGenOutBase")

    packageName.set(openAPIBasePackage)
    apiPackage.set("$openAPIBasePackage.api")
    invokerPackage.set("$openAPIBasePackage.client")
    modelPackage.set("$openAPIBasePackage.model")

    modelNameSuffix.set("Dto")
    typeMappings.set(
        mapOf(
            "DateTime" to "Instant"
        )
    )
    importMappings.set(
        mapOf(
            "Instant" to "java.time.Instant"
        )
    )
    configOptions.set(
        mapOf(
            "useBeanValidation" to "true",
            "delegatePattern" to "true",
            "useTags" to "true",
            "exceptionHandler" to "false",
            "gradleBuildFile" to "false",
            "reactive" to "true",
            "serviceInterface" to "true",
            "sortModelPropertiesByRequiredFlag" to "true",
            "sortParamsByRequiredFlag" to "true",
            "swaggerAnnotations" to "true",
        )
    )
}

val openApiGenerateTask = tasks.getByName("openApiGenerate")
openApiGenerateTask.dependsOn(mergeOpenApiFilesTask)

// get rid of deprecation warning that kaptGenerateStubsKotlin
// depends on openApiGeneratebut doesn't declare a dependency
tasks.matching { it.name == "kaptGenerateStubsKotlin" }
    .configureEach { dependsOn(openApiGenerateTask) }

val processResourcesTask = tasks.getByName("processResources")
processResourcesTask.dependsOn(openApiGenerateTask)
