package io.crate.streaming.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxiRideDeserializationSchema implements KafkaRecordDeserializationSchema<TaxiRide> {

    public static final TaxiRideDeserializationSchema INSTANCE = new TaxiRideDeserializationSchema();

    private static final Logger LOGGER = Logger.getLogger(TaxiRideDeserializationSchema.class.getName());

    private final ObjectMapper mapper;

    private TaxiRideDeserializationSchema() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"));
    }

    @Override
    public TypeInformation<TaxiRide> getProducedType() {
        return TypeInformation.of(new TypeHint<TaxiRide>() {
        });
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        KafkaRecordDeserializationSchema.super.open(context);
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<TaxiRide> out) throws IOException {
        try {
            out.collect(mapper.readValue(record.value(), TaxiRide.class));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
        }
    }
}
