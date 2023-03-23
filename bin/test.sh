#!/bin/bash
# Print job information.

# Initialize Gradle and print version.
./gradlew --version

# Run all checks.
# Note: This will also initialize the Gradle daemon, which is important,
# because, otherwise, the next `printVersion` command will splatter the
# stdout with corresponding messages.
./gradlew check

# Version of the jar file.
VERSION=$(./gradlew printVersion | head -1)
JARFILE="cratedb-flink-jobs-${VERSION}.jar"

# Build JAR file.
./gradlew shadowJar

# Print job information.
docker run --rm \
  --volume=$(pwd)/build/libs/${JARFILE}:/${JARFILE} docker.io/library/flink:1.16.1 \
  flink info /${JARFILE} \
    --kafka.servers foo:9092 \
    --kafka.topic foo \
    --crate.hosts foo:5432 \
    --crate.table foo
