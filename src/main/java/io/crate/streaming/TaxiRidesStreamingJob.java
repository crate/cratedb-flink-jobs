package io.crate.streaming;

import io.crate.streaming.model.TaxiRide;
import io.crate.streaming.model.TaxiRideDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

public class TaxiRidesStreamingJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameters = ParameterTool.fromArgs(args);

        env
                .fromSource(createStreamSource(parameters), WatermarkStrategy.noWatermarks(), "kafa")
                .map(new TaxiRideToRowStringFunction())
                .addSink(
                    JdbcSink.sink(
                            String.format("INSERT INTO doc.%s (payload) VALUES (?)", parameters.getRequired("crate.table")),
                            (statement, row) -> statement.setString(1, (String) row.getField(0)),
                            JdbcExecutionOptions.builder()
                                    .withBatchSize(1000)
                                    .withBatchIntervalMs(200)
                                    .withMaxRetries(5)
                                    .build(),
                            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                    .withUrl(String.format("jdbc:postgresql://%s/", parameters.getRequired("crate.hosts")))
                                    .withDriverName("org.postgresql.Driver")
                                    .withUsername(parameters.get("crate.user", "crate"))
                                    .withPassword(parameters.get("crate.password", ""))
                                    .build()
                    ));

        env.execute();
    }

    private static Source<TaxiRide, ?, ?> createStreamSource(ParameterTool parameters) {
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

        return KafkaSource.<TaxiRide>builder()
                .setTopics("kafka.topic")
                .setDeserializer(TaxiRideDeserializationSchema.INSTANCE)
                .setProperties(properties)
                .build();
    }
}
