package com.lld.practice.tutor_sessions.parking_lot.design_patterns.strategy.impl;

import com.lld.practice.tutor_sessions.parking_lot.design_patterns.strategy.PricingStrategy;
import com.lld.practice.tutor_sessions.parking_lot.implementation.model.Ticket;
import com.lld.practice.tutor_sessions.parking_lot.implementation.model.VehicleType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

public class FlatHourlyPricingStrategy implements PricingStrategy {

    private final Map<VehicleType, Double> pricing = new EnumMap<>(VehicleType.class);

    public FlatHourlyPricingStrategy() {
        seedRates();
    }

    @Override
    public double calculateFee(Ticket ticket) {
        LocalDateTime now = LocalDateTime.now();
        long hours = Math.max(1, (long) Math.ceil(
                Duration.between(ticket.getEntry(), now).toMinutes() / 60.0));
        Double rate = pricing.get(ticket.getVehicleType());
        if (rate == null) throw new IllegalArgumentException("No rate for: " + ticket.getVehicleType());
        return hours * rate;
    }

    private void seedRates() {
        pricing.put(VehicleType.BIKE,  10.0);
        pricing.put(VehicleType.CAR,   20.0);
        pricing.put(VehicleType.TRUCK, 40.0);
    }
}
