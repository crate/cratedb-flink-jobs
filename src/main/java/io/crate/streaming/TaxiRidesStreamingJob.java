package io.crate.streaming;

import io.crate.streaming.model.TaxiRide;
import io.crate.streaming.model.TaxiRideDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.core.datastream.sink.JdbcSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.ParameterTool;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

public class TaxiRidesStreamingJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameters = ParameterTool.fromArgs(args);

        env
                .fromSource(createStreamSource(parameters), WatermarkStrategy.noWatermarks(), "kafka")
                .map(new TaxiRideToRowStringFunction())
                .sinkTo(
                    JdbcSink.<Row>builder()
                            .withQueryStatement(
                                    String.format("INSERT INTO doc.%s (payload) VALUES (?)", parameters.getRequired("crate.table")),
                                    (statement, row) -> statement.setString(1, (String) row.getField(0)))
                            .withExecutionOptions(JdbcExecutionOptions.builder()
                                    .withBatchSize(1000)
                                    .withBatchIntervalMs(200)
                                    .withMaxRetries(5)
                                    .build())
                            .buildAtLeastOnce(new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                    .withUrl(String.format("jdbc:postgresql://%s/", parameters.getRequired("crate.hosts")))
                                    .withDriverName("org.postgresql.Driver")
                                    .withUsername(parameters.get("crate.user", "crate"))
                                    .withPassword(parameters.get("crate.password", ""))
                                    .build()));

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
                .setTopics(parameters.getRequired("kafka.topic"))
                .setDeserializer(TaxiRideDeserializationSchema.INSTANCE)
                .setProperties(properties)
                .build();
    }
}
