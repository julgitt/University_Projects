package com.company;

import java.io.Serializable;
import java.time.LocalDate;

public class Bill implements Serializable {
    private final int billID;
    static int billID_counter;
    private final double prizeForDay;
    private final LocalDate returnDeadline;
    private final Borrow borrow;

    //constructor
    public Bill(Borrow borrow, LocalDate returnDeadline, double prize) {
        this.billID = billID_counter;
        billID_counter++;
        this.borrow = borrow;
        this.returnDeadline = returnDeadline;
        this.prizeForDay = prize * 0.1;
    }

    //getters
    public int getBillID() {
        return this.billID;
    }

    public double getPrize() {
        return this.prizeForDay * daysAfterDeadline();
    }

    public Borrow getBorrow() {
        return this.borrow;
    }

    public LocalDate getReturnDeadline() {
        return this.returnDeadline;
    }

    //methods
    public long daysAfterDeadline() {
        return java.time.temporal.ChronoUnit.DAYS.between(returnDeadline, LocalDate.now());
    }
    @Override
    public String toString(){
        return billID + " " + getPrize() + " " + getReturnDeadline() + " " + getBorrow() + " " + billID_counter;
    }
}
