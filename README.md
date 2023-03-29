# Cook4Me

[![Maintainability](https://api.codeclimate.com/v1/badges/d63d2fbee57ddc9e8ffc/maintainability)](https://codeclimate.com/github/cook4me/android/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/d63d2fbee57ddc9e8ffc/test_coverage)](https://codeclimate.com/github/cook4me/android/test_coverage)

An app for epfl students to enhance their food experience

## Team

 Name                 | Github | Email |
|----------------------|-------|-------|
| John Taylor     | [JohnTomasTaylor](https://github.com/JohnTomasTaylor) | john.taylor@epfl.ch |
| Dayan Massonnet     | [dayan9265](https://github.com/dayan9265) | dayan.massonnet@epfl.ch |
| Daniel Bucher   | [bu-da](https://github.com/bu-da) | daniel.bucher@epfl.ch |
| Haolong Li        | [Tachi-67](https://github.com/Tachi-67) | haolong.li@epfl.ch |
| Pau Romeu        | [pauromeu](https://github.com/pauromeu) | pau.romeu@epfl.ch |
| Nino Gerber   | [nino-gerb](https://github.com/nino-gerb) | nino.gerber@epfl.ch |

## Setup

This project depends on a specific release of the Jacoco library, which has been updated to provide coverage support for Jetpack Compose. This release is available on [GitHub](https://github.com/epfl-SDP/jacoco-compose), and requires the use of the GitHub Apache Maven Package Repository. Therefore, you are required to add the following to your `~/.gradle/gradle.properties` file to access the Maven package.

```properties
githubJacocoUsername=YourGitHubUsername
# Requires at least the read:packages scope.
githubJacocoPassword=YourGitHubPersonalAccessToken
```
To create the a personal access token go to your github profile and to Settings / Developer Settings / Tokens (classic). Click on generate new token (classic). Then add a note, expiration date and click on read:packages. Then replace the placeholder with the generated token.

As this project uses Google Firebase, you will also need to provide your own google-services.json.
This file can be generated from the Firebase Console (Project -> Project Settings -> Your apps -> SDK setup and configuration -> google-services.json).
It must be placed at ./app/google-services.json.

[firebase emulator](https://firebase.google.com/docs/emulator-suite) is used for some tests. To install it run the following: `curl -sL https://firebase.tools | bash`. To run the tests in android studio execute: `firebase emulators:start`. To run the tests as well as other checks on the cli execute `firebase emulators:exec './gradlew check connectedCheck'`. 

As this project integrates Google Maps, a Google Maps API key is needed and needs to be added to the local.properties file. It can be downloaded [here](https://console.cloud.google.com/apis/credentials?project=cook4me-adb46).

```properties
MAPS_API_KEY=KEYVALUE
```


### Static code analysis
In the CI pipeline we use two static code analysis tools:
* [detekt](https://detekt.dev/), can be triggered with `./gradlew detekt`
* [Android Studio lint](https://developer.android.com/studio/write/lint), can be triggered with `./gradlew lintDebug` 

It's recommended to run these two commands locally before pushing a commit or creating a pull request, because the ci is slower. To automatically fix formatting errors discovered by detekt run `./gradlew ktlintFormat`. 

## Project structure
This project uses Jetpack Compose and is written in Kotlin. The main packages are organized as follows:

- `ch.epfl.sdp.cook4me.persistence.repository` contains the [repositories](https://martinfowler.com/eaaCatalog/repository.html) (classes which take care of the database access). All the classes have the prefix `Repository`; 
- `ch.epfl.sdp.cook4me.persistence.model` contains model classes, which represent the stored format; 
- `ch.epfl.sdp.mobile.application` contains the business logic. All the classes have the postfix `Service`;
- `ch.epfl.sdp.mobile.ui` contains the user interface (where all the `@Composable` functions live);
