#!/bin/bash
# Print job information.

# Import version settings.
source .env

# Print Gradle and environment versions.
./gradlew --version

# Define JAR file name.
JARFILE="cratedb-flink-jobs-${JOB_VERSION}.jar"

# Build JAR file.
./gradlew build

# Print job information.
docker run --rm \
  --volume=$(pwd)/build/libs/${JARFILE}:/${JARFILE} docker.io/library/flink:${FLINK_VERSION} \
  flink info /${JARFILE} \
    --kafka.servers foo:9092 \
    --kafka.topic foo \
    --crate.hosts foo:5432 \
    --crate.table foo
