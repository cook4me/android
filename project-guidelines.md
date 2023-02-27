# Project guidelines

## Sources
This document is inspired by our [friends work](https://github.com/epfl-SDP/android/blob/main/wiki/Home.md) and some parts are copied. The project structure follows this [android project](https://github.com/google-developer-training/basic-android-kotlin-compose-training-cupcake).

## Workflow
### Branch naming
All branches must follow this structure : `type/branch_name`
   - The `type` can be : `feature`, `fix` or `refactor`
   - The `branch_name` must be meaningful and be separated by an underscore

### Pull request
- Each PR must be linked to the corresponding issue.
- You can use the Draft PR, if you want an early review
- For the reviewer, you can prefix your comments with a keyword that indicating the kind of comment
  it is, here is a short list of keywords:
    - Important
    - Nitpick
    - Question
    - Bug
    - Proposition

## Meetings
Each week, we have 2 stand-up meetings:
- Tuesday at 16.00
- Thursday at 16.00

The Scrum master is responsible for the meeting location/reservation. 

In addition to the stand-up, we have a sprint review with our TAs Friday at 11:15. At the end of this meeting, the Scrum Team will plan the content of the next sprint backlog.

## Scrum master rotation order
A different Scrum master is chosen for each sprint, in the following cyclic order starting from
Sprint 1:

1. Daniel Bucher
2. Nino Gerber
3. Pau Romeu
4. Haolong Li
5. Dayan Massonnet
6. John Taylor

## Kotlin/Android Code Style Guide
We write code in a functional way. Views should be separated from state and other sideffects. Functions should be written as much as possible in a pure fashion. The following Kotlin features support this approach:
   - use `val` (~ `final` in Java) keyword instead of `var`
   - use immutable datastructures (e.g. `listOf`, `mapOf` etc.) instead of mutuable ones (e.g. `mutableListOf`)
   - use [data classes](https://kotlinlang.org/docs/data-classes.html) with immutable fields (`val` fields) to model value objects (objects without state/identity)
   - use the [copy function of data classes](https://kotlinlang.org/docs/data-classes.html#copying) to create copies of objects instead of changing its fields.
   - use the [extensive standard library](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/) or functions like `map`, `fold` etc. instead of writing c-style while/for loops.

Never use the `!!` operator in Kotlin. This bypasses the null checks of the compiler and we risk null pointer exceptions. Be cautious while using (java) libaries. We don't have any null safety guarantees when interopting with java oder bytecode languages.

Be economical in the usage of inheritance. Inheritance introduces tight coupling between classes. Better use delegation.

Strings targeting users should be placed in a [separate xml file](https://github.com/cook4me/android/blob/main/app/src/main/res/values/strings.xml), to simplify reusage and multilanguage support in the future. 
