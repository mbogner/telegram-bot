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
    kotlin("kapt")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.openapi.generator")
    id("com.rameshkp.openapi-merger-gradle-plugin")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":updater"))

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation(project(":common-test"))
}

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin", "${project.buildDir}/generated/source/openapi/src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

val openAPIBasePackage: String by project
val openAPISpecParts: String by project
val openAPISpecFilePath: String by project
val openAPISpecFileName: String by project
val openAPISpecFileExt: String by project
val openAPISpecFile = "$projectDir/$openAPISpecFilePath/$openAPISpecFileName.$openAPISpecFileExt"

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
tasks.named("mergeOpenApiFiles") {
    dependsOn(cleanOpenApiFile)
}

openApiGenerate {
    // https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md
    generatorName.set("spring")
    library.set("spring-boot")
    templateDir.set("$projectDir/src/main/openapi/templates")

    inputSpec.set(openAPISpecFile)
    outputDir.set("$buildDir/generated/source/openapi")

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
    // https://openapi-generator.tech/docs/generators/spring/
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "useBeanValidation" to "true",
            "performBeanValidation" to "true",
            "delegatePattern" to "true",
            "hideGenerationTimestamp" to "true",
            "useTags" to "true",
            "configPackage" to "$openAPIBasePackage.config",
        )
    )
}

tasks.named("openApiGenerate") {
    dependsOn(tasks.named("mergeOpenApiFiles"))
}
