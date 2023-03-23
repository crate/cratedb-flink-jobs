package io.crate.streaming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.crate.streaming.model.TaxiRide;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.types.Row;

public class TaxiRideToRowStringFunction extends RichMapFunction<TaxiRide, Row> {

    private volatile ObjectMapper mapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        mapper = new ObjectMapper();
        mapper.setDateFormat(new ISO8601DateFormat());
    }

    @Override
    public Row map(TaxiRide taxiRide) throws JsonProcessingException {
        String taxiRideObjectAsMap = mapper.writeValueAsString(taxiRide);
        return Row.of(taxiRideObjectAsMap, DataTypes.STRING());
    }
}
