##########################################
Backlog for example Flink jobs for CrateDB
##########################################


*******
General
*******

Next steps
==========
- [o] Demonstrate all examples, including the ``runJob`` examples, with `CrateDB Cloud`_.
- [o] Transfer (updated?) article `Build a data ingestion pipeline using Kafka, Flink, and CrateDB`_
  to https://community.crate.io/.
- [o] Write introduction/summary at https://community.crate.io/t/cratedb-community-day-2/1415,
  cross-linking all relevant resources together, and giving a concise introduction into the topic.
- [o] Follow up with the `Flink JDBC Connector » CrateDB support`_ patch.
- [o] Q: Should the infrastructure from ``kafka-flink`` be just hosted within this repository?
- [o] Currently, the test harness for the ``runJob`` examples need to spin up a CrateDB
  instance using Docker, both locally and on CI. What about using the new "testcontainer"-based
  implementation?

Actionable
==========
- [o] ``crate-jdbc``: Address Dependabot updates at https://github.com/crate/crate-jdbc/pulls.
- [o] ``crate-jdbc``: Update to recent pgJDBC, see https://github.com/crate/crate-jdbc/tree/master/pg.
- [o] Examples: How to generate test data using the ``datagen`` and ``flink-faker`` connectors.

  - https://nightlies.apache.org/flink/flink-docs-master/docs/connectors/table/datagen/
  - https://flink-packages.org/packages/flink-faker
  - https://www.ververica.com/blog/flink-sql-match_recognize
- [o] Examples: How to import Parquet files, and how to ingest from AMQP and MQTT.
- [o] Examples: Demonstrate using PyFlink.
- [o] Examples: Because CrateDB does not have a CDC interface, the `CDC Connectors for
  Apache Flink`_ can't be used. So, at least demonstrate how to use the *GetInData
  CDC by JDBC Connector*, or something equivalent, see `Change Data Capture by JDBC with FlinkSQL`_.

Research
========
- [o] In most cases, the ``CREATE DATABASE AS`` statement is suitable for fully automated
  data integration scenarios.

  - https://www.alibabacloud.com/help/en/realtime-compute-for-apache-flink/latest/create-database-as-statement
  - https://www.alibabacloud.com/help/en/realtime-compute-for-apache-flink/latest/create-table-as-statement
- [o] Upsert vs. append modes?

  - https://www.ververica.com/blog/streaming-modes-of-flink-kafka-connectors
- [o] It would be so sweet if "DDL ``auto.create``" would be supported, no?

  - https://docs.confluent.io/kafka-connectors/jdbc/current/sink-connector/sink_config_options.html#ddl-support
- [o] Evaluate how this topic relates to "Kafka Connect".
- [o] Would it be possible to submit connectivity instructions to upstream
  cloud providers, like outlined in the next section?
- [o] Is it possible to use CrateDB's advanced data types in any way?
- [o] Have a look at IntelliJ's Big Data Tools, it has support for Apache Flink
- [o] Something with Debezium and/or Iceberg?

  - https://github.com/memiiso/debezium-server-iceberg
  - https://github.com/getindata/kafka-connect-iceberg-sink
- [o] Infrastructure: Don't stop at Docker, also demonstrate usage with Podman


***************
Cloud readiness
***************

An example how to use `CrateDB Cloud`_ can be found at `Apache Kafka, Apache
Flink, and CrateDB Cloud`_.

Todo: Demonstrate how to run **all elements** on hosted infrastructure.


- Aiven for Apache Flink

  - https://aiven.io/flink
  - https://docs.aiven.io/docs/products/flink/howto/connect-pg

- Alibaba Cloud

  - https://www.alibabacloud.com/help/en/realtime-compute-for-apache-flink
  - https://www.alibabacloud.com/help/en/realtime-compute-for-apache-flink/latest/development-reference-upstream-and-downstream-storage
  - https://www.alibabacloud.com/help/en/clickhouse/latest/use-jdbc-connector-to-write-data-to-an-apsaradb-for-clickhouse-cluster

- Amazon EMR

  - https://docs.aws.amazon.com/emr/latest/ReleaseGuide/emr-flink.html

- Confluent Cloud

  - https://docs.confluent.io/cloud/current/connectors/
  - https://www.confluent.io/blog/cloud-kafka-meets-cloud-flink-with-confluent-and-immerok/

- Upstash

  - https://upstash.com/
  - https://docs.upstash.com/kafka


.. _Apache Kafka, Apache Flink, and CrateDB Cloud: https://github.com/crate/cratedb-examples/tree/main/stacks/kafka-flink#cratedb-cloud
.. _Build a data ingestion pipeline using Kafka, Flink, and CrateDB: https://dev.to/crate/build-a-data-ingestion-pipeline-using-kafka-flink-and-cratedb-1h5o
.. _CDC Connectors for Apache Flink: https://ververica.github.io/flink-cdc-connectors/master/content/about.html
.. _Change Data Capture by JDBC with FlinkSQL: https://getindata.com/blog/change-data-capture-JDBC-Flink-FlinkSQL/
.. _CrateDB Cloud: https://crate.io/products/cratedb-cloud
.. _CrateDB Cloud Console: https://console.cratedb.cloud/
.. _Flink JDBC Connector » CrateDB support: https://github.com/apache/flink-connector-jdbc/pull/29
