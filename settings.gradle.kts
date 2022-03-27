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

rootProject.name = "telegram-bot"

include("bot")

include("common")
project(":common").projectDir = file("modules/common")

include("common-test")
project(":common-test").projectDir = file("modules/common-test")

include("client-common")
project(":client-common").projectDir = file("modules/clients/client-common")

include("client-telegram")
project(":client-telegram").projectDir = file("modules/clients/telegram")

include("client-mite")
project(":client-mite").projectDir = file("modules/clients/mite")

include("api")
project(":api").projectDir = file("modules/api")

include("updater")
project(":updater").projectDir = file("modules/updater")
