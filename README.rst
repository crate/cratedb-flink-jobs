===================================
Kafka, Flink, CrateDB: How-To Guide
===================================

The sample `Apache Flink`_ job that uses `Apache Kafka`_ as a source and
`CrateDB`_ as sink. The data stream is represented by records that contain
information about `NYC Yellow Taxi Trips`_.

Build Flink Job
===============

.. code:: console

    $ ./gradlew build

Submit Flink Job
================

.. code:: console

    make run


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
.. _Apache Kafka: https://kafka.apache.org/
.. _CrateDB: https://crate.io/
.. _NYC Yellow Taxi Trips: https://data.cityofnewyork.us/Transportation/2017-Yellow-Taxi-Trip-Data/biws-g3hs/