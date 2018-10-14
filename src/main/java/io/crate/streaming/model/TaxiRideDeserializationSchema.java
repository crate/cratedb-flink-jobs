package io.crate.streaming.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxiRideDeserializationSchema implements DeserializationSchema<TaxiRide> {

    public static final TaxiRideDeserializationSchema INSTANCE = new TaxiRideDeserializationSchema();

    private static final Logger LOGGER = Logger.getLogger(TaxiRideDeserializationSchema.class.getName());

    private final ObjectMapper mapper;

    private TaxiRideDeserializationSchema() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"));
    }

    @Override
    public TaxiRide deserialize(byte[] bytes) {
        try {
            return mapper.readValue(bytes, TaxiRide.class);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public boolean isEndOfStream(TaxiRide nextElement) {
        return false;
    }

    @Override
    public TypeInformation<TaxiRide> getProducedType() {
        return TypeInformation.of(new TypeHint<TaxiRide>() {
        });
    }
}
