#!/usr/bin/env bash

set -eu

GRADLE_USER_HOME="$(pwd)/.gradle"
export GRADLE_USER_HOME

version=$(cat version/tag)

(
cp player-source/docs/player-guide.adoc source/src/docs/asciidoc/

cd source
./gradlew -Pversion="$version" clean build --rerun-tasks --no-daemon
)

cp -a source/* dist/
