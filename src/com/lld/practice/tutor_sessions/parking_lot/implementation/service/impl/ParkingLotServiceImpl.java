package com.lld.practice.tutor_sessions.parking_lot.implementation.service.impl;

import com.lld.practice.tutor_sessions.parking_lot.design_patterns.factory.ticket.TicketFactory;
import com.lld.practice.tutor_sessions.parking_lot.design_patterns.factory.vehicle.Vehicle;
import com.lld.practice.tutor_sessions.parking_lot.design_patterns.factory.vehicle.VehicleFactory;
import com.lld.practice.tutor_sessions.parking_lot.design_patterns.strategy.PricingStrategy;
import com.lld.practice.tutor_sessions.parking_lot.implementation.model.*;
import com.lld.practice.tutor_sessions.parking_lot.implementation.service.ParkingLotService;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;

public class ParkingLotServiceImpl implements ParkingLotService {

    private static final double LOST_PENALTY = 500.0;

    private final ParkingLot parkingLot;                                   // full hierarchy
    private final Map<String, Ticket> ticketsById = new HashMap<>();
    private final Map<Integer, Slot> slotsByNumber = new HashMap<>();      // flat map for O(1) lookup in exit()
    private final Map<VehicleType, SlotType> vehicleToSlot = new EnumMap<>(VehicleType.class);

    private final PricingStrategy pricingStrategy;

    /**
     * Primary constructor: accepts a fully-built ParkingLot (with floors and typed slots).
     * The flat slotsByNumber map is built by flattening all floors — used for O(1) slot lookup.
     */
    public ParkingLotServiceImpl(ParkingLot parkingLot, PricingStrategy pricingStrategy) {
        this.parkingLot = parkingLot;
        this.pricingStrategy = pricingStrategy;
        // flatten ParkingLot → Floor → Slot into the lookup map
        parkingLot.getFloor().forEach(floor ->
                floor.getSlot().forEach(slot -> slotsByNumber.put(slot.getSlotNumber(), slot)));
        seedCompatibility();
    }

    private void seedCompatibility() {
        vehicleToSlot.put(VehicleType.CAR,   SlotType.CAR);
        vehicleToSlot.put(VehicleType.BIKE,  SlotType.BIKE);
        vehicleToSlot.put(VehicleType.TRUCK, SlotType.TRUCK);
    }


    // ---------- Ops ----------

    @Override
    public Ticket entry(String licensePlate, VehicleType vehicleType) {
        if (licensePlate == null) throw new IllegalArgumentException("licensePlate is null");
        if (vehicleType == null)  throw new IllegalArgumentException("vehicleType is null");

        Vehicle vehicle = VehicleFactory.create(vehicleType, licensePlate);
        SlotType needed = vehicleToSlot.get(vehicleType);
        if (needed == null) throw new IllegalArgumentException("Unsupported vehicleType: " + vehicleType);

        Slot slot = findFreeSlotOrThrow(needed);
        slot.setStatus(SlotStatus.OCCUPIED);

        Ticket t = TicketFactory.issueTicket(vehicle, slot.getSlotNumber());
        ticketsById.put(t.getTicketId(), t);
        return t;
    }

    @Override
    public Ticket pay(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.ISSUED)
            throw new IllegalStateException("pay requires ISSUED, was: " + t.getStatus());


        t.setExit(now());
        t.setFee(pricingStrategy.calculateFee(t));
        t.setStatus(TicketStatus.PAID);
        return t;
    }

    @Override
    public void exit(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.PAID)
            throw new IllegalStateException("exit requires PAID, was: " + t.getStatus());

        t.setStatus(TicketStatus.EXITED);
        slotsByNumber.get(t.getSlotNumber()).setStatus(SlotStatus.FREE); // release slot
    }

    @Override
    public Ticket reportLost(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.ISSUED)
            throw new IllegalStateException("reportLost requires ISSUED, was: " + t.getStatus());
        t.setStatus(TicketStatus.LOST);
        return t;
    }

    @Override
    public Ticket payLostTicketPenalty(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.LOST)
            throw new IllegalStateException("payLostTicketPenalty requires LOST, was: " + t.getStatus());

        t.setFee(LOST_PENALTY);
        t.setExit(now());
        t.setStatus(TicketStatus.PAID);   // funnels into normal exit() flow
        return t;
    }

    /**
     * Iterates ParkingLot → Floor → Slot to find the first free slot of the given type.
     * Floor-order traversal means lower floors are preferred (natural fairness).
     */
    @Override
    public Slot findFreeSlot(SlotType slotType) {
        return parkingLot.getFloor().stream()
                .flatMap(floor -> floor.getSlot().stream())
                .filter(s -> s.getStatus() == SlotStatus.FREE && s.getSlotType() == slotType)
                .findFirst()
                .orElse(null);
    }

    // ---------- Helpers ----------

    private Slot findFreeSlotOrThrow(SlotType slotType) {
        Slot s = findFreeSlot(slotType);
        if (s == null) throw new IllegalStateException("Lot full for slotType: " + slotType);
        return s;
    }

    private Ticket requireTicket(String id) {
        if (id == null) throw new IllegalArgumentException("ticketId is null");
        Ticket t = ticketsById.get(id);
        if (t == null) throw new IllegalArgumentException("Unknown ticket: " + id);
        return t;
    }
}