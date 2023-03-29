#!/bin/bash
# Build job and submit to Flink

NC='\033[0m'
BRED='\033[1;31m'
BYELLOW='\033[1;33m'
BCYAN='\033[1;36m'
BGREEN='\033[1;32m'
BWHITE='\033[1;37m'

# Import version settings.
source .env


function build() {
  # Define JAR file name.
  JARFILE="cratedb-flink-jobs-${JOB_VERSION}.jar"

  # Build job
  echo -e "${BCYAN}INFO:  ${BWHITE}Building job${NC}"
  ./gradlew build
  echo
}


function probe_backend() {

  echo -e "${BCYAN}INFO:  ${BWHITE}Probing backend${NC}"

  # Check if backend is available by probing designated network.
  docker run -it --rm --network=scada-demo hello-world > /dev/null 2>&1
  #echo $?
  #exit

  # Error handling
  exitcode=$?
  if [ ${exitcode} != 0 ]; then
    echo -e "${BRED}ERROR: Unable to connect to Flink cluster.${NC}"
    echo
    echo -e "${BYELLOW}NOTE${NC}"
    echo -e "${BWHITE}"
    echo "There is an executable end-to-end tutorial for Apache Kafka, Apache Flink, and CrateDB."
    echo "It will spin up a Flink cluster easily using Docker Compose."
    echo
    echo -e "${BCYAN}    https://github.com/crate/cratedb-examples/tree/main/stacks/kafka-flink#readme${NC}"
    echo
    echo -e "${BWHITE}Just follow the installation instructions, and invoke::"
    echo
    echo -e "${BCYAN}    bash test.sh start-services${NC}"
    echo
    exit ${exitcode}
  fi
}


function submit() {

  echo -e "${BCYAN}INFO:  ${BWHITE}Launching job${NC}"

  # Invoke job
  docker run -it --rm \
    --network=scada-demo \
    --volume=$(pwd)/build/libs/${JARFILE}:/${JARFILE} flink:${FLINK_VERSION} \
      \
      flink run --jobmanager=flink-jobmanager:8081 /${JARFILE} \
        --kafka.servers kafka-broker:9092 \
        --kafka.topic rides \
        --crate.hosts cratedb:5432 \
        --crate.table taxi_rides

}


function main() {
  build
  probe_backend
  submit
}

main
