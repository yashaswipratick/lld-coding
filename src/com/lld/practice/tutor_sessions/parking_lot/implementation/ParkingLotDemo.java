package com.lld.practice.tutor_sessions.parking_lot.implementation;

import com.lld.practice.tutor_sessions.parking_lot.implementation.model.Ticket;
import com.lld.practice.tutor_sessions.parking_lot.implementation.service.ParkingLotService;
import com.lld.practice.tutor_sessions.parking_lot.implementation.service.impl.ParkingLotServiceImpl;

/**
 * Stage 1 MVP demo — asserts happy path + every guard.
 * Run: main() should print all PASS lines and exit 0.
 */
public class ParkingLotDemo {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        happyPath_singleVehicle();
        happyPath_twoVehicles_freeSlotReused();
        entry_throws_whenLotFull();
        pay_throws_forUnknownTicket();
        pay_throws_whenAlreadyPaid();
        exit_throws_whenNotPaid();
        exit_throws_forUnknownTicket();
        entry_throws_onNullPlate();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
        if (failed > 0) System.exit(1);
    }

    // ---------- tests ----------

    static void happyPath_singleVehicle() {
        ParkingLotService svc = new ParkingLotServiceImpl(2);
        Ticket t = svc.entry("KA-01-AB-1234");
        expect("entry issues ticket", t != null && "ISSUED".equals(t.getStatus().name()));
        expect("entry sets entryTime", t.getEntry() != null);
        expect("entry assigns a slot", t.getSlotNumber() != null);

        Ticket paid = svc.pay(t.getTicketId());
        expect("pay -> PAID", "PAID".equals(paid.getStatus().name()));
        expect("pay computes fee (min 1 hour)", paid.getFee() >= 100.0);

        svc.exit(t.getTicketId());
        expect("exit -> EXITED", "EXITED".equals(t.getStatus().name()));
        expect("exit sets exitTime", t.getExit() != null);
    }

    static void happyPath_twoVehicles_freeSlotReused() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);   // only 1 slot
        Ticket t1 = svc.entry("V1");
        svc.pay(t1.getTicketId());
        svc.exit(t1.getTicketId());
        Ticket t2 = svc.entry("V2");   // should succeed — slot freed on exit
        expect("slot reused after exit", t2 != null && t2.getSlotNumber().equals(t1.getSlotNumber()));
    }

    static void entry_throws_whenLotFull() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);
        svc.entry("V1");
        expectThrows("entry throws when lot full", IllegalStateException.class,
                () -> svc.entry("V2"));
    }

    static void pay_throws_forUnknownTicket() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);
        expectThrows("pay throws on unknown ticketId", IllegalArgumentException.class,
                () -> svc.pay("bogus-id"));
    }

    static void pay_throws_whenAlreadyPaid() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);
        Ticket t = svc.entry("V1");
        svc.pay(t.getTicketId());
        expectThrows("pay throws when already PAID", IllegalStateException.class,
                () -> svc.pay(t.getTicketId()));
    }

    static void exit_throws_whenNotPaid() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);
        Ticket t = svc.entry("V1");
        expectThrows("exit throws when ticket ISSUED (not paid)", IllegalStateException.class,
                () -> svc.exit(t.getTicketId()));
    }

    static void exit_throws_forUnknownTicket() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);
        expectThrows("exit throws on unknown ticketId", IllegalArgumentException.class,
                () -> svc.exit("bogus-id"));
    }

    static void entry_throws_onNullPlate() {
        ParkingLotService svc = new ParkingLotServiceImpl(1);
        expectThrows("entry throws on null plate", IllegalArgumentException.class,
                () -> svc.entry(null));
    }

    // ---------- tiny assert helpers ----------

    static void expect(String name, boolean cond) {
        if (cond) { System.out.println("PASS: " + name); passed++; }
        else      { System.out.println("FAIL: " + name); failed++; }
    }

    static void expectThrows(String name, Class<? extends Throwable> expected, Runnable r) {
        try {
            r.run();
            System.out.println("FAIL: " + name + " (no exception thrown)");
            failed++;
        } catch (Throwable ex) {
            if (expected.isInstance(ex)) { System.out.println("PASS: " + name); passed++; }
            else { System.out.println("FAIL: " + name + " (got " + ex.getClass().getSimpleName() + ": " + ex.getMessage() + ")"); failed++; }
        }
    }
}

