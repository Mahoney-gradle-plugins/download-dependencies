# Download Dependencies Gradle Plugin

## Usage

Add to the root project build.gradle.kts:
```kotlin
id("uk.org.lidalia.downloaddependencies")
```

Run:
```bash
./gradlew downloadDependencies
```

## Rationale

I consider it desirable for a build to run offline; it allows 
development in situations without a reliable internet connection, 
and by ensuring that the build does not rely on any external service 
means the build will not fail due to any unreliability in those 
services.

Obviously a gradle build needs to have all of its dependencies locally
in order to be able to run offline. This plugin allows gradle to 
download all of them in a single command that does nothing else, so that
from then on the build can be run offline.

If you are using the [Docker frontend experimental syntaxes](https://github.com/moby/buildkit/blob/docker-19.03/frontend/dockerfile/docs/experimental.md)
you can take advantage of this as so:
```Dockerfile
# syntax=docker/dockerfile:experimental

RUN ./gradlew downloadDependencies

RUN --network=none \
    ./gradlew --offline build
```
to ensure that the actual build occurs without a network connection and
so will fail if, for instance, a test is checked in that depends on a 
remote service.

## Development

Build locally:
```bash
./gradlew build
```
Output will be under `build` & if successful the binary will be under
`build/libs`.

Build in docker:
```bash
docker build . -t plugin:latest && \
docker cp "$(docker create plugin:latest):/home/worker/work/build" .
```
Output will be under `build` & if successful the binary will be under
`build/libs`.
