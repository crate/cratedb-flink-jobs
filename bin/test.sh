#!/bin/bash
# Print job information.

# Print Gradle and environment versions.
./gradlew --version

# Version of the jar file.
VERSION=$(./gradlew printVersion | grep "ProjectVersion:" |awk '{print $2}')
JARFILE="cratedb-flink-jobs-${VERSION}.jar"

# Build JAR file.
./gradlew build

# Print job information.
docker run --rm \
  --volume=$(pwd)/build/libs/${JARFILE}:/${JARFILE} docker.io/library/flink:1.16.1 \
  flink info /${JARFILE} \
    --kafka.servers foo:9092 \
    --kafka.topic foo \
    --crate.hosts foo:5432 \
    --crate.table foo
