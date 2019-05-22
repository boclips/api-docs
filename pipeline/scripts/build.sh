#!/usr/bin/env bash

set -eu

export GRADLE_USER_HOME="$(pwd)/.gradle"

version=$(cat version/version)

(
cp player-source/docs/player-guide.adoc source/src/docs/asciidoc/

cd source
./gradlew -Pversion=${version} clean build --rerun-tasks --no-daemon
)

cp -a source/* dist/
