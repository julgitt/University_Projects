package com.company;

import java.io.Serializable;
import java.time.LocalDate;

public class Borrow implements Serializable {
    private final long borrowID;
    private final User user;
    private final int userID;
    private final Book book;
    private final int bookID;
    private boolean status; //if deadline
    private final LocalDate dateOfBorrow;
    private final LocalDate returnDeadline;

    //constructor
    public Borrow(User user, int userID, Book book, int bookID, LocalDate dateOfBorrow, int days, long id) {
        this.borrowID = id;
        this.user = user;
        this.book = book;
        this.userID = userID;
        this.bookID = bookID;
        this.dateOfBorrow = dateOfBorrow;//deadline
        this.returnDeadline = dateOfBorrow.plusDays(days);
        this.status = false;
    }

    //getters
    public long getBorrowID() {
        return this.borrowID;
    }

    public User getUser() {
        return this.user;
    }

    public Book getBook() {
        return this.book;
    }

    public int getUserID() {
        return this.userID;
    }

    public int getBookID() {
        return this.bookID;
    }

    public LocalDate getDateOfBorrow() {
        return this.dateOfBorrow;
    }

    public boolean getStatus() {
        return this.status;
    }

    public LocalDate getReturnDeadline() {
        return this.returnDeadline;
    }

    //setter
    public void changeStatus() {
        status = !status;
    }

    @Override
    public String toString() {
        return getBorrowID() + " " + getUser() + " " + getUserID() + " " + getBook() + " " + getBookID() + " " + getStatus() + " " + getDateOfBorrow() + " " + getReturnDeadline();
    }

}
