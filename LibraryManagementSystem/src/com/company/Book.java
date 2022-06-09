package com.company;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private final int bookID;
    static int bookID_counter = 0;
    private final Author author;
    private final String title;
    private Boolean status; //is Borrowed?
    private final double price;


    //constructor
    public Book(Author author, String title, double price) {
        this.bookID = bookID_counter;
        bookID_counter++;
        this.author = author;
        this.title = title;
        this.price = price;
        this.status = false;
    }

    //getters
    public int getBookID() {
        return this.bookID;
    }

    public String getTitle() {
        return this.title;
    }

    public Author getAuthor() {
        return this.author;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public double getPrice() {
        return this.price;
    }

    //setters
    public void changeStatus() {
        this.status = !status;
    }

    public void setStatus(String status) {
        this.status = Objects.equals(status, "borrowed");
    }


    //methods

    @Override
    public String toString() {
        return getTitle() + " " + getBookID() + " " + getAuthor() + " " + getPrice() + " " + getStatus() + " " + bookID_counter;
    }

}
