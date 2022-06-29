package com.company;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private final long bookID;
    private final Author author;
    private final String title;
    private Boolean status; //is Borrowed?
    private final double price; //lepiej w intach


    //constructor
    public Book(Author author, String title, double price, long id) {
        this.bookID = id;
        this.author = author;
        this.title = title;
        this.price = price;
        this.status = false;
    }

    //getters
    public long getBookID() {
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

    public void setStatus(String status) {
        this.status = Objects.equals(status, "borrowed");
    }


    //methods

    @Override
    public String toString() {
        return getTitle() + " " + getBookID() + " " + getAuthor() + " " + getPrice() + " " + getStatus();
    }

}
