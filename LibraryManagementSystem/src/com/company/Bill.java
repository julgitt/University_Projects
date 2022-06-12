package com.company;

import java.io.Serializable;
import java.time.LocalDate;

public class Bill implements Serializable {
    private final long billID;
    private final double priceForDay;
    private final LocalDate returnDeadline;
    private final Borrow borrow;

    //constructor
    public Bill(Borrow borrow, LocalDate returnDeadline, double price, long id) {
        this.billID = id;
        this.borrow = borrow;
        this.returnDeadline = returnDeadline;
        this.priceForDay = price * 0.1;
    }

    //getters
    public long getBillID() {
        return this.billID;
    }

    public double getPrice() {
        return this.priceForDay * daysAfterDeadline();
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
        return billID + " " + getPrice() + " " + getReturnDeadline() + " " + getBorrow();
    }
}
