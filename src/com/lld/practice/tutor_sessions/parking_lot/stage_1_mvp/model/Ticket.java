package com.lld.practice.tutor_sessions.parking_lot.stage_1_mvp.model;

import java.time.LocalDateTime;

public class Ticket {

    private String ticketId;
    private String licensePlateNumber;
    private Integer slotNumber;
    private LocalDateTime entry;
    private LocalDateTime exit;
    private Double fee;
    private TicketStatus status;

    public Ticket() {
    }

    public Ticket(String ticketId, String licensePlateNumber, Integer slotNumber, LocalDateTime entry, LocalDateTime exit, Double fee, TicketStatus status) {
        this.ticketId = ticketId;
        this.licensePlateNumber = licensePlateNumber;
        this.slotNumber = slotNumber;
        this.entry = entry;
        this.exit = exit;
        this.fee = fee;
        this.status = status;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public LocalDateTime getEntry() {
        return entry;
    }

    public void setEntry(LocalDateTime entry) {
        this.entry = entry;
    }

    public LocalDateTime getExit() {
        return exit;
    }

    public void setExit(LocalDateTime exit) {
        this.exit = exit;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", licensePlateNumber='" + licensePlateNumber + '\'' +
                ", slotNumber=" + slotNumber +
                ", entry=" + entry +
                ", exit=" + exit +
                ", fee=" + fee +
                ", status=" + status +
                '}';
    }
}
