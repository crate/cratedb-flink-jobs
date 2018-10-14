package io.crate.streaming.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class TaxiRide implements Serializable {

    @JsonProperty("vendor_id")
    private long vendorId;
    @JsonProperty("pickup_datetime")
    private Date pickupDatetime;
    @JsonProperty("dropoff_datetime")
    private Date dropoffDatetime;
    @JsonProperty("passenger_count")
    private int passengerCount;
    @JsonProperty("trip_distance")
    private float tripDistance;
    @JsonProperty("pickup_location_id")
    private int pickupLocationId;
    @JsonProperty("dropoff_location_id")
    private int dropoffLocationId;
    @JsonProperty("fare_amount")
    private float fareAmount;
    @JsonProperty("tip_amount")
    private float tipAmount;
    @JsonProperty("tolls_amount")
    private float tollsAmount;
    @JsonProperty("total_amount")
    private float totalAmount;

    public TaxiRide() {
    }

    public TaxiRide(long vendorId,
                    Date pickupDatetime,
                    Date dropoffDatetime,
                    int passengerCount,
                    float tripDistance,
                    int pickupLocationId,
                    int dropoffLocationId,
                    float fareAmount,
                    float tipAmount,
                    float tollsAmount,
                    float totalAmount) {
        this.vendorId = vendorId;
        this.pickupDatetime = pickupDatetime;
        this.dropoffDatetime = dropoffDatetime;
        this.passengerCount = passengerCount;
        this.tripDistance = tripDistance;
        this.pickupLocationId = pickupLocationId;
        this.dropoffLocationId = dropoffLocationId;
        this.fareAmount = fareAmount;
        this.tipAmount = tipAmount;
        this.tollsAmount = tollsAmount;
        this.totalAmount = totalAmount;
    }

    public long getVendorId() {
        return vendorId;
    }

    public Date getPickupDatetime() {
        return pickupDatetime;
    }

    public Date getDropoffDatetime() {
        return dropoffDatetime;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public float getTripDistance() {
        return tripDistance;
    }

    public int getPickupLocationId() {
        return pickupLocationId;
    }

    public int getDropoffLocationId() {
        return dropoffLocationId;
    }

    public float getFareAmount() {
        return fareAmount;
    }

    public float getTipAmount() {
        return tipAmount;
    }

    public float getTollsAmount() {
        return tollsAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }
}
