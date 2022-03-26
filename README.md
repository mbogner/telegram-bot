# telegram-bot

This project consists of multiple gradle modules as followed:

- `bot`: This is the main module with the executable Spring Boot application
- The "modules" folder contains different encapsulated parts:
    - `common`: Shared module that is part of all modules.
    - `common-test`: Shared test module that is part of all modules.
    - `api`: A simple API to interact with the running bot.
    - `updater`: An async worker to get updates via polling.

The OpenAPI template is taken from https://github.com/mbogner/kotlin-spring-feign-client-generator.