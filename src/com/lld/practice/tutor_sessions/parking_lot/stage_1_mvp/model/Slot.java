package com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model;

public class Slot {

    private Integer slotNumber;
    private SlotStatus status;

    public Slot() {
    }

    public Slot(Integer slotNumber, SlotStatus status) {
        this.slotNumber = slotNumber;
        this.status = status;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "slotNumber=" + slotNumber +
                ", status=" + status +
                '}';
    }
}
