*********
Changelog
*********


in progress
===========

2026-06-10 0.8
==============
- Upgrade to Flink 2.0.2 (with Java 17)
- Migrate the JDBC connector to the Flink 2.0 split artifacts ``flink-connector-jdbc-{core,postgres,cratedb}:4.0.0-2.0`` (the monolithic ``flink-connector-jdbc`` was retired at 3.3.0-1.20)


2026-06-09 0.7
==============
- Fix TaxiRidesStreamingJob Kafka source subscribing to the literal topic name
  "kafka.topic" instead of the value of the ``--kafka.topic`` parameter; regressed
  during the Flink 1.20 KafkaSource migration
- Pin flink-connector-kafka to the Flink 1.20 line (3.4.0-1.20); the previous
  4.0.1-2.0 targets Flink 2.0 and is incompatible with the 1.20.1 runtime

2025-03-22 0.6
==============
- Upgrade to latest flink (1.20.1)
- Updated org.postgresql:postgresql from 42.7.2 to 42.7.3
- Updated com.fasterxml.jackson.datatype:jackson-datatype-jsr310

2024-03-13 0.5
==============
- Upgrade to use Flink 1.18

2023-04-27 0.4
==============
- Upgrade to use Flink 1.17

2023-04-23 0.3
==============
- Modernize to use Flink 1.16


2021-04-27 0.2
==============
- Update to most recent software versions


2018-10-15 0.1
==============
- Initial release
