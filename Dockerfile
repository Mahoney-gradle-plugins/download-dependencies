# syntax=docker/dockerfile:1.1.7-experimental
ARG username=worker
ARG work_dir=/home/$username/work

FROM openjdk:14.0.1-jdk-slim as builder
ARG username
ARG work_dir

RUN addgroup --system $username --gid 1000 && \
    adduser --system $username --ingroup $username --uid 1001

USER $username
RUN mkdir -p $work_dir
WORKDIR $work_dir

ENV GRADLE_OPTS='-Dorg.gradle.daemon=false -Xms256m -Xmx2g --illegal-access=deny'

COPY --chown=$username . .

# Can't use docker ARG values in the --mount argument: https://github.com/moby/buildkit/issues/815
RUN --mount=type=cache,target=/home/worker/.gradle,gid=1000,uid=1001 \
    set +e; \
    ./gradlew build; \
    echo $? > build_result;

# The previous step is guaranteed not to fail, so that the worker output can be tagged and its
# contents (build reports) extracted.
# You run this as:
# `docker build . --target builder -t downloadeps-builder:$GITHUB_SHA && docker build . --target checker`
# and you can then use
# `docker cp $(docker create "downloadeps-builder:$GITHUB_SHA"):/home/worker/work/build/reports reports`
# to retrieve them whether or not the previous line exited successfully.
# Workaround for https://github.com/moby/buildkit/issues/1421
FROM builder as checker
RUN build_result=$(cat build_result); \
    if [ "$build_result" -gt 0 ]; then >&2 echo "The build failed, check output of builder stage"; fi; \
    exit "$build_result"
