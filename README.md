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

## Setup

This project depends on a specific release of the Jacoco library, which has been updated to provide coverage support for Jetpack Compose. This release is available on [GitHub](https://github.com/epfl-SDP/jacoco-compose), and requires the use of the GitHub Apache Maven Package Repository. Therefore, you are required to add the following to your `~/.gradle/gradle.properties` file to access the Maven package.

```properties
githubJacocoUsername=YourGitHubUsername
# Requires at least the read:packages scope.
githubJacocoPassword=YourGitHubPersonalAccessToken
```