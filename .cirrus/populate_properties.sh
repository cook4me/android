#!/bin/sh

# Takes $GITHUB_USERNAME and $GITHUB_TOKEN and writes them to the ~/.gradle/gradle.properties
mkdir ~/.gradle
echo "githubJacocoUsername=$GITHUB_USERNAME\ngithubJacocoPassword=$GITHUB_TOKEN" > ~/.gradle/gradle.properties

echo "android.useAndroidX=true" > ~/.gradle/gradle.properties

# Takes $MAPS_API_KEY and writes it to local.properties
echo MAPS_API_KEY=$MAPS_API_KEY >> local.properties

# Takes $GOOGLE_SERVICES and writes it to ./app/google-services.json
echo $GOOGLE_SERVICES > ./app/google-services.json



