package com.lld.practice.tutor_sessions.parking_lot.implementation.service.impl;

import com.lld.practice.tutor_sessions.parking_lot.implementation.model.*;
import com.lld.practice.tutor_sessions.parking_lot.implementation.service.ParkingLotService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLotServiceImpl implements ParkingLotService {

    private static final double LOST_PENALTY = 500.0;

    private final Map<String, Ticket> ticketsById = new HashMap<>();
    private final Map<Integer, Slot> slotsByNumber = new HashMap<>();
    private final Map<VehicleType, SlotType> vehicleToSlot = new EnumMap<>(VehicleType.class);
    private final Map<VehicleType, Double> rateMap = new EnumMap<>(VehicleType.class);

    /** Primary constructor: caller specifies how many slots of each type to create. */
    public ParkingLotServiceImpl(Map<SlotType, Integer> slotCounts) {
        seedCompatibility();
        seedRates();
        seedSlots(slotCounts);
    }

    /** Convenience: split totalSlots evenly across CAR/BIKE/TRUCK (remainder → CAR). */
    public ParkingLotServiceImpl(int totalSlots) {
        this(evenSplit(totalSlots));
    }

    private static Map<SlotType, Integer> evenSplit(int total) {
        int each = total / 3;
        int rem  = total % 3;
        Map<SlotType, Integer> m = new EnumMap<>(SlotType.class);
        m.put(SlotType.CAR,   each + rem);
        m.put(SlotType.BIKE,  each);
        m.put(SlotType.TRUCK, each);
        return m;
    }

    private void seedCompatibility() {
        vehicleToSlot.put(VehicleType.CAR,   SlotType.CAR);
        vehicleToSlot.put(VehicleType.BIKE,  SlotType.BIKE);
        vehicleToSlot.put(VehicleType.TRUCK, SlotType.TRUCK);
    }

    private void seedRates() {
        rateMap.put(VehicleType.BIKE,  10.0);
        rateMap.put(VehicleType.CAR,   20.0);
        rateMap.put(VehicleType.TRUCK, 40.0);
    }

    private void seedSlots(Map<SlotType, Integer> counts) {
        AtomicInteger nextSlotNum = new AtomicInteger(1);
        counts.forEach((type, count) -> {
            for (int i = 0; i < count; i++) {
                int n = nextSlotNum.getAndIncrement();
                slotsByNumber.put(n, new Slot(n, SlotStatus.FREE, type));
            }
        });
    }

    // ---------- Ops ----------

    @Override
    public Ticket entry(String licensePlate, VehicleType vehicleType) {
        if (licensePlate == null) throw new IllegalArgumentException("licensePlate is null");
        if (vehicleType == null)  throw new IllegalArgumentException("vehicleType is null");

        SlotType needed = vehicleToSlot.get(vehicleType);
        if (needed == null) throw new IllegalArgumentException("Unsupported vehicleType: " + vehicleType);

        Slot slot = findFreeSlotOrThrow(needed);
        slot.setStatus(SlotStatus.OCCUPIED);

        Ticket t = new Ticket(
                UUID.randomUUID().toString(),
                licensePlate,
                slot.getSlotNumber(),
                LocalDateTime.now(),
                null,               // exit
                0.0,                // fee (Ticket.fee is primitive double)
                TicketStatus.ISSUED,
                vehicleType);
        ticketsById.put(t.getTicketId(), t);
        return t;
    }

    @Override
    public Ticket pay(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.ISSUED)
            throw new IllegalStateException("pay requires ISSUED, was: " + t.getStatus());

        LocalDateTime now = LocalDateTime.now();
        long hours = Math.max(1, (long) Math.ceil(
                Duration.between(t.getEntry(), now).toMinutes() / 60.0));
        t.setFee(hours * rateMap.get(t.getVehicleType()));
        t.setExit(now);
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
        t.setExit(LocalDateTime.now());
        t.setStatus(TicketStatus.PAID);   // funnels into normal exit() flow
        return t;
    }

    @Override
    public Slot findFreeSlot(SlotType slotType) {
        return slotsByNumber.values().stream()
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