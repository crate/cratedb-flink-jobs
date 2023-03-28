#########################################################################
Example Flink jobs for "Apache Kafka, Apache Flink, and CrateDB" tutorial
#########################################################################

*Building industrial IoT applications with open-source components.*


*****
About
*****

This repository accompanies the article `Build a data ingestion pipeline using
Kafka, Flink, and CrateDB`_ and material presented at the `CrateDB Community
Day #2`_.

It is supplemented by a corresponding `executable end-to-end tutorial for
Apache Kafka, Apache Flink, and CrateDB`_, which makes it easy to run a
Kafka/Flink infrastructure on your workstation, pulls the Flink job JAR file
from the assets on the `release page`_ and submits it to the Flink cluster.

For learning more details about the technologies used here, please follow up
reading the excellent documentation and resources around Apache Flink.

- `Apache Flink SQL Cookbook`_
- `Flink » Examples » Batch`_
- `Flink » Examples » DataStream`_
- `Flink » Examples » Table`_


*******
Details
*******

Most of the Flink jobs demonstrated here connect to CrateDB using the `Flink
JDBC Connector`_, using both the vanilla `PostgreSQL JDBC driver`_, and
the CrateDB adapter/dialect, which is currently available as a patch at `Flink
JDBC Connector » CrateDB support`_.

The first two jobs, both defined in ``io.crate.flink.demo``, can be launched
as standalone Java applications, without the need to submit them to a Flink
cluster. The other job, defined in ``io.crate.streaming``, is meant to be
submitted as a job to a Flink cluster for demonstration purposes, but can
also be invoked interactively.

- The ``SimpleJdbcSinkJob`` demonstrates a basic example which inserts a few
  records into CrateDB using Flink. It outlines how to use the fundamental
  Flink ``JdbcSink`` API, and how to adjust the corresponding
  ``JdbcExecutionOptions`` and ``JdbcConnectionOptions``.

- The ``SimpleTableApiJob`` demonstrates how to use the Flink Table API and
  the Flink DataStream API.

  The `Flink Table API`_ is a language-integrated query API for Java, Scala, and
  Python that allows the composition of queries from relational operators such as
  selection, filter, and join in a very intuitive way, and offers a unified
  interface for both stream and batch processing.

  The `Flink DataStream API`_ offers the primitives of stream processing (namely
  time, state, and dataflow management) in a relatively low-level imperative
  programming API. The Table API abstracts away many internals and provides a
  structured and declarative API.

  Both APIs can work with bounded and unbounded streams. Bounded streams need to
  be managed when processing historical data. Unbounded streams occur in
  real-time processing scenarios that might be initialized with historical data
  first.

- The ``TaxiRidesStreamingJob`` subscribes to an `Apache Kafka`_ topic as a data
  source, and stores received data into `CrateDB`_ as a data sink. The data stream
  is represented by records from the venerable `NYC Yellow Taxi Trips`_ dataset.


*****
Usage
*****

Prerequisites
=============

Acquire and build the source code.

.. code:: console

    git clone https://github.com/crate/cratedb-flink-jobs
    cd cratedb-flink-jobs
    make build

``TaxiRidesStreamingJob``
=========================

- ``make test`` will probe the job using ``flink info``.
- ``make submit`` will submit the job using ``flink run`` to a Flink
  cluster at ``localhost:8081`` .

``SimpleJdbcSinkJob`` and ``SimpleTableApiJob``
===============================================

- ``make run JOB=SimpleJdbcSinkJob``
- ``make run JOB=SimpleTableApiJob``


********
Appendix
********


JDBC drivers
============

- | ``SimpleJdbcSinkJob`` uses the `PostgreSQL JDBC driver`_
  | Driver class: ``org.postgresql.Driver``
  | URL schema: ``postgresql://``

- | ``TaxiRidesStreamingJob`` and ``SimpleTableApiJob`` use the `CrateDB JDBC driver`_
  | Driver class: ``io.crate.client.jdbc.CrateDriver``
  | URL schema: ``crate://``


Flink job configurations
========================

These are the settings for the ``TaxiRidesStreamingJob``.

Required settings
-----------------

.. list-table::
    :widths: 25 75
    :header-rows: 1

    * - Setting
      - Description
    * - **kafka.servers**
      - Comma-separated list of Kafka brokers to connect to.
    * - **kafka.topic**
      - Kafka topic to consume.
    * - **crate.hosts**
      - | Comma-separated list of CrateDB hosts. The format is ``<hostname>:<psql_port> [, ...]``.
        | Example: ``crate-01.example.net:5432,crate-02.example.net:5432``
    * - **crate.table**
      - CrateDB table name.

Optional settings
-----------------

.. list-table::
    :widths: 25 25 75
    :header-rows: 1

    * - Setting
      - Default
      - Description
    * - **kafka.group.id**
      - default
      - Kafka consumer group ID.
    * - **kafka.offset**
      - earliest
      - Kafka topic offset.
    * - **batch.interval.ms**
      - 5000
      - Timeout in milliseconds to use for periodic flushing.
    * - **crate.schema**
      - doc
      - CrateDB schema.
    * - **crate.user**
      - crate
      - CrateDB user.
    * - **crate.password**
      - <empty>
      - CrateDB user password.


.. _Apache Flink: https://flink.apache.org/
.. _Apache Flink SQL Cookbook: https://github.com/ververica/flink-sql-cookbook
.. _Apache Kafka: https://kafka.apache.org/
.. _Build a data ingestion pipeline using Kafka, Flink, and CrateDB: https://dev.to/crate/build-a-data-ingestion-pipeline-using-kafka-flink-and-cratedb-1h5o
.. _CrateDB: https://crate.io/
.. _CrateDB Community Day #2: https://community.crate.io/t/cratedb-community-day-2/1415
.. _CrateDB JDBC driver: https://crate.io/docs/jdbc/
.. _executable end-to-end tutorial for Apache Kafka, Apache Flink, and CrateDB: https://github.com/crate/cratedb-examples/tree/main/stacks/kafka-flink#readme
.. _Flink DataStream API: https://nightlies.apache.org/flink/flink-docs-stable/docs/dev/table/data_stream_api/
.. _Flink » Examples » Batch: https://github.com/apache/flink/tree/master/flink-examples/flink-examples-batch/src/main/java/org/apache/flink/examples/java
.. _Flink » Examples » DataStream: https://github.com/apache/flink/tree/master/flink-examples/flink-examples-streaming/src/main/java/org/apache/flink/streaming/examples
.. _Flink » Examples » Table: https://github.com/apache/flink/tree/master/flink-examples/flink-examples-table/src/main/java/org/apache/flink/table/examples/java
.. _Flink JDBC Connector: https://nightlies.apache.org/flink/flink-docs-stable/docs/connectors/table/jdbc/
.. _Flink JDBC Connector » CrateDB support: https://github.com/apache/flink-connector-jdbc/pull/29
.. _Flink Table API: https://nightlies.apache.org/flink/flink-docs-stable/docs/dev/table/overview/
.. _NYC Yellow Taxi Trips: https://data.cityofnewyork.us/Transportation/2017-Yellow-Taxi-Trip-Data/biws-g3hs/
.. _PostgreSQL JDBC Driver: https://github.com/pgjdbc/pgjdbc
.. _release page: https://github.com/crate/cratedb-flink-jobs/releases
