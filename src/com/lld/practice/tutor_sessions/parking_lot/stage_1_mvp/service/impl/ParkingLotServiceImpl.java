package com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.service.impl;

import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model.Slot;
import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model.SlotStatus;
import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model.Ticket;
import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model.TicketStatus;
import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.service.ParkingLotService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParkingLotServiceImpl implements ParkingLotService {
    private static final double HOURLY_RATE = 20.0;
    private final Map<String, Ticket> ticketsById = new HashMap<>();
    private final Map<Integer, Slot> slotsByNumber = new HashMap<>();

    public ParkingLotServiceImpl(int totalSlots) {
        for (int i = 1; i <= totalSlots; i++)
            slotsByNumber.put(i, new Slot(i, SlotStatus.FREE));
    }

    @Override
    public Ticket entry(String licensePlate) {
        if (licensePlate == null) throw new IllegalArgumentException("licensePlate is null");
        Slot slot = findFreeSlotOrThrow();
        slot.setStatus(SlotStatus.OCCUPIED);                          // ← missing side-effect
        Ticket t = new Ticket(UUID.randomUUID().toString(), licensePlate,
                slot.getSlotNumber(), LocalDateTime.now(),
                null, null, TicketStatus.ISSUED);       // fee = null initially
        ticketsById.put(t.getTicketId(), t);
        return t;
    }

    @Override
    public Ticket pay(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.ISSUED)
            throw new IllegalStateException("Ticket not in ISSUED state: " + t.getStatus());
        LocalDateTime now = LocalDateTime.now();
        long hours = Math.max(1, (long) Math.ceil(
                Duration.between(t.getEntry(), now).toMinutes() / 60.0));
        t.setFee(hours * HOURLY_RATE);
        t.setExit(now);
        t.setStatus(TicketStatus.PAID);
        return t;
    }

    @Override
    public void exit(String ticketId) {
        Ticket t = requireTicket(ticketId);
        if (t.getStatus() != TicketStatus.PAID)
            throw new IllegalStateException("Ticket must be PAID before exit, was: " + t.getStatus());
        t.setStatus(TicketStatus.EXITED);
        slotsByNumber.get(t.getSlotNumber()).setStatus(SlotStatus.FREE);  // coupled state machine
    }

    @Override
    public Slot findFreeSlot() {
        return slotsByNumber.values().stream()
                .filter(s -> s.getStatus() == SlotStatus.FREE)
                .findFirst()
                .orElse(null);
    }

    private Slot findFreeSlotOrThrow() {
        return slotsByNumber.values().stream()
                .filter(s -> s.getStatus() == SlotStatus.FREE)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lot full"));
    }

    private Ticket requireTicket(String id) {
        if (id == null) throw new IllegalArgumentException("ticketId is null");
        Ticket t = ticketsById.get(id);
        if (t == null) throw new IllegalArgumentException("Unknown ticket: " + id);
        return t;
    }
}