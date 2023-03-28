#!/bin/bash
# Run in-process Flink example jobs.
# Provision database beforehand.

crash="docker run --interactive --rm --network=host crate crash"


function SimpleJdbcSinkJob() {
  $crash <<EOF
  DROP TABLE IF EXISTS my_schema.books;
  CREATE TABLE my_schema.books(id long, title string, authors string, year int);
EOF

  ./gradlew runApp -P mainClass=io.crate.flink.demo.SimpleJdbcSinkJob
  echo

  $crash <<EOF
  REFRESH TABLE my_schema.books;
  SELECT * FROM my_schema.books;
EOF
}


function SimpleTableApiJob() {
  $crash <<EOF
CREATE TABLE t1(s string) with (refresh_interval=1);
CREATE TABLE t2(a int) with (refresh_interval=1);
CREATE TABLE t3(name string, cnt int) with(refresh_interval=1);
CREATE TABLE t4(name string, avg int, min int, max int) with (refresh_interval=1);
INSERT INTO t2(a) SELECT * from generate_series(1, 5, 1);
INSERT INTO t3(name, cnt) VALUES('Apache', 1), ('Apache', 2), ('Flink', 11), ('Flink', 22), ('Flink', 33), ('CrateDB', 111), ('CrateDB', 333);
EOF

  ./gradlew runApp -P mainClass=io.crate.flink.demo.SimpleTableApiJob
  echo

  $crash <<EOF
  REFRESH TABLE t4;
  SELECT * FROM t4;
EOF
}


function start() {
  if [ -z "${JOB}" ]; then
    echo "ERROR: Job name missing, use either 'SimpleJdbcSinkJob' or 'SimpleTableApiJob'"
    exit 1
  fi
  ${JOB}
}


JOB=$1
shift
start
