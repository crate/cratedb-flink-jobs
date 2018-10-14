package io.crate.streaming;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.crate.streaming.model.TaxiRide;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.Map;

public class TaxiRideToRowMapFunction extends RichMapFunction<TaxiRide, Row> {

    private volatile TypeReference<HashMap<String, Object>> typeRef;
    private volatile ObjectMapper mapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        mapper = new ObjectMapper();
        mapper.setDateFormat(new ISO8601DateFormat());

        typeRef = new TypeReference<HashMap<String, Object>>() {};
    }

    @Override
    public Row map(TaxiRide taxiRide) {
        Map<String, Object> taxiRideObjectAsMap = mapper.convertValue(taxiRide, typeRef);
        return Row.of(taxiRideObjectAsMap);
    }
}
