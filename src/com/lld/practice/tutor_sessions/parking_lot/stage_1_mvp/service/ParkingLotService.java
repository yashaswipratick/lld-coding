package com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.service;

import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model.Slot;
import com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model.Ticket;

public interface ParkingLotService {

    Ticket entry(String licensePlate);         // ISSUED → returns ticket with slotNum + entryTime

    Ticket pay(String ticketId);               // ISSUED → PAID, computes fee, sets exitTime

    void exit(String ticketId);                // PAID → EXITED, releases slot

    Slot findFreeSlot();                       // helper (package-private also fine)
}
