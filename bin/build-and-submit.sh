#!/bin/bash
# Build job and submit to Flink

# Version of the jar file.
# TODO: Extract from `build.gradle`.
VERSION="0.2"
JARFILE="cratedb-flink-jobs-${VERSION}.jar"

# Build job
./gradlew build

# Upload and invoke job
docker run -it \
  --network=scada-demo \
  --volume=$(pwd)/build/libs/${JARFILE}:/${JARFILE} flink:1.12 \
    \
    flink run --jobmanager=flink-jobmanager:8081 /${JARFILE} \
      --kafka.servers kafka-broker:9092 \
      --kafka.topic rides \
      --crate.hosts cratedb:5432 \
      --crate.table taxi_rides
