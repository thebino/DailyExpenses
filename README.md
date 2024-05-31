# Daily Expenses

[![Continuous Delivery Pipeline](https://img.shields.io/github/actions/workflow/status/thebino/DailyExpenses/continuous-delivery-pipeline.yml?style=for-the-badge)](https://github.com/thebino/DailyExpenses/actions/workflows/continuous-delivery-pipeline.yml)
[![Ktor](https://img.shields.io/badge/ktor-2.3.11-blue.svg?color=087CFA&logo=ktor&style=for-the-badge)](https://https://ktor.io)
![AGP](https://img.shields.io/badge/agp-8.2.0-blue?color=34A853&logo=android&style=for-the-badge)
![kotlin-version](https://img.shields.io/badge/kotlin-2.0.0-blue?color=7F52FF&logo=kotlin&style=for-the-badge)
![Compos Multiplatform](https://img.shields.io/badge/compose-1.6.10-blue?color=4285F4&logo=jetpackcompose&style=for-the-badge)

Track your daily expenses and share it with your self-hosted backend.

<p align="center">
<img src="/docs/preview.png" />
</p>

## Tech
 - Written with [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
 - Based on [Kotlin Multiplatform](https://www.jetbrains.com/kotlin-multiplatform/)
 - Persistence is using [Room Multiplatform](https://developer.android.com/kotlin/multiplatform/room)
 - Dependency Injection with [Koin](https://insert-koin.io/docs/quickstart/kmp/)


## App Actions
> Hey Google, add Groceries to DailyExpenses.

## Sharing

Share expenses with friends and family via a self-hosted [backend](./server)

1. Setup you server address
2. create & enter credentials
3. enter expenses
4. synchronize expenses with backend
5. see expenses from others 

## Server
Run the server by calling the following command:
```shell
./gradlew :server:run
```
it will spin up the server on http://127.0.0.1:8080


## Android
Build and deploy an debug version of the application to a connected device or emulator by running:
```shell
./gradlew :composeApp:installDebug
```


## TODO:

 * [ ] Test coverage
 * [ ] Paparazzi
 * [ ] Fastlane setup
   * [ ] Google Play
   * [ ] F-Droid
 * [ ] Make the app resizable
   * [ ] https://developer.android.com/jetpack/compose/layouts/adaptive

## Notes
 * Design idea
   * https://dribbble.com/shots/17198286-My-finances-Mobile-App
 * Font
   * https://www.dafont.com/de/neuropolitical.font?text=Categories&l[]=10&l[]=1&back=theme#null
   * https://typodermicfonts.com/neuropolitical-science/
