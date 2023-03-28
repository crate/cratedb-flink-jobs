===================================
Kafka, Flink, CrateDB: How-To Guide
===================================


About
=====

This repository accompanies the article `Build a data ingestion pipeline using
Kafka, Flink, and CrateDB`_ and the `CrateDB Community Day #2`_.

The sample `Apache Flink`_ job uses `Apache Kafka`_ as a source and `CrateDB`_
as a sink. The data stream is represented by records that contain data from the
venerable `NYC Yellow Taxi Trips`_ dataset.


Details
=======

The high-level article `Build a data ingestion pipeline using Kafka, Flink, and
CrateDB`_ is accompanied by a corresponding executable infrastructure tutorial
at `Apache Kafka, Apache Flink, and CrateDB`_, which pulls an Apache Flink job
JAR file from the assets on the `release page`_ of this repository.

It uses the `Apache Flink JDBC Connector`_, and currently bundles the patch
submitted at `Apache Flink JDBC Connector » CrateDB support`_.


Usage
=====

Kafka->CrateDB job
------------------

Build job Jar file.

.. code:: console

    $ ./gradlew build

Submit to Flink.

.. code:: console

    make submit

In-process jobs
---------------

At ``io.crate.flink.demo``, there are two Flink example jobs/programs which can
be launched interactively without needing to submit them. ``SimpleJdbcSinkJob``
demonstrates how to insert records into CrateDB using the official
``org.postgresql.Driver`` JDBC driver.

.. code:: console

    make run JOB=SimpleJdbcSinkJob

``SimpleTableApiJob`` demonstrates how to leverage the virtual
``TableEnvironment`` subsystem.

.. code:: console

    make run JOB=SimpleTableApiJob


Flink Job Configurations
========================

These settings for the sample Flink job.

**kafka.servers**
  | *required:* true

  The comma separated list of Kafka brokers.

**kafka.group.id**
  | *default:* default

  The Kafka consumer group ID.

**kafka.topic**
  | *required:* true

  The Kafka topic to consume.

**kafka.topic**
  | *default:* earliest

  The Kafka topic's offset.

**batch.interval.ms**
  | *default:* 5000

  The timeout in milliseconds to use for periodic flushing.

**crate.hosts**
  | *required:* true

  The comma separated list of CrateDB hosts. The format looks as
  following ``<hostname>:<psql_port> [, ...]``. E.g. crate-01:5432,crate-02:5432.

**crate.user**
  | *default:* "crate"

  The CrateDB user.

**crate.password**
  | *default:* ""

  The CrateDB user password.

**crate.schema**
  | *default:* "doc"

  The CrateDB schema.

**crate.table**
  | *required:* true

  The CrateDB table.


.. _Apache Flink: https://flink.apache.org/
.. _Apache Flink JDBC Connector: https://github.com/apache/flink-connector-jdbc
.. _Apache Flink JDBC Connector » CrateDB support: https://github.com/apache/flink-connector-jdbc/pull/29
.. _Apache Kafka: https://kafka.apache.org/
.. _Apache Kafka, Apache Flink, and CrateDB: https://github.com/crate/cratedb-examples/tree/main/stacks/kafka-flink
.. _Build a data ingestion pipeline using Kafka, Flink, and CrateDB: https://dev.to/crate/build-a-data-ingestion-pipeline-using-kafka-flink-and-cratedb-1h5o
.. _CrateDB: https://crate.io/
.. _CrateDB Community Day #2: https://community.crate.io/t/cratedb-community-day-2/1415
.. _NYC Yellow Taxi Trips: https://data.cityofnewyork.us/Transportation/2017-Yellow-Taxi-Trip-Data/biws-g3hs/
.. _release page: https://github.com/crate/cratedb-flink-jobs/releases
