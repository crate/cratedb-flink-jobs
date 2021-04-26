package io.crate.streaming;

import io.crate.streaming.model.TaxiRide;
import io.crate.streaming.model.TaxiRideDeserializationSchema;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.connector.jdbc.JdbcOutputFormat;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.types.Row;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

public class TaxiRidesStreamingJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameters = ParameterTool.fromArgs(args);

        env
                .addSource(createStreamSource(parameters))
                .map(new TaxiRideToRowMapFunction())
                .writeUsingOutputFormat(createJDBCOutputFormat(parameters));

        env.execute();
    }

    private static SourceFunction<TaxiRide> createStreamSource(ParameterTool parameters) {
        Properties properties = new Properties();
        properties.setProperty(
                BOOTSTRAP_SERVERS_CONFIG,
                parameters.getRequired("kafka.servers"));
        properties.setProperty(
                GROUP_ID_CONFIG,
                parameters.get("kafka.group.id", "default"));
        properties.setProperty(
                AUTO_OFFSET_RESET_CONFIG,
                parameters.get("kafka.offset", "earliest")
        );

        return new FlinkKafkaConsumer<>(
                parameters.getRequired("kafka.topic"),
                TaxiRideDeserializationSchema.INSTANCE,
                properties
        );
    }

    private static OutputFormat<Row> createJDBCOutputFormat(ParameterTool parameters) {
        /*
         * TODO: Add compatibility for PostgreSQL JDBC.
         *       Currently, it raises `org.postgresql.util.PSQLException: No hstore extension installed.`.
         *       .setDrivername("org.postgresql.Driver")
         *       .setDBUrl(String.format("jdbc:postgresql://%s/", parameters.getRequired("crate.hosts")))
         */
        return JdbcOutputFormat.buildJdbcOutputFormat()
                .setDrivername("io.crate.client.jdbc.CrateDriver")
                .setBatchSize(parameters.getInt("batch.size", 5000))
                .setDBUrl(String.format("crate://%s/", parameters.getRequired("crate.hosts")))
                .setUsername(parameters.get("crate.user", "crate"))
                .setPassword(parameters.get("crate.password", ""))
                .setQuery(String.format(
                        "INSERT INTO %s (payload) VALUES (?)",
                        parameters.getRequired("crate.table"))
                )
                .finish();
    }
}
