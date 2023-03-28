#!/bin/bash
# Build job and submit to Flink

# Import settings.
source .env

# Version of the jar file.
JAR_VERSION=$(./gradlew printVersion | grep "ProjectVersion:" | awk '{print $2}')
JARFILE="cratedb-flink-jobs-${JAR_VERSION}.jar"

# Build job
./gradlew build

# Upload and invoke job
docker run -it \
  --network=scada-demo \
  --volume=$(pwd)/build/libs/${JARFILE}:/${JARFILE} flink:${FLINK_VERSION} \
    \
    flink run --jobmanager=flink-jobmanager:8081 /${JARFILE} \
      --kafka.servers kafka-broker:9092 \
      --kafka.topic rides \
      --crate.hosts cratedb:5432 \
      --crate.table taxi_rides
