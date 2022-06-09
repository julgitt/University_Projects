package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Author implements Serializable {
    private final int authorID;
    static int authorID_counter = 0;
    private final String name;
    private final String secondName;
    private final List<Book> booksInLibrary;


    //constructor
    public Author(String name, String secondName) {
        this.authorID = authorID_counter;
        authorID_counter++;
        this.name = name;
        this.secondName = secondName;
        this.booksInLibrary = new ArrayList<>();
    }

    //getters
    public int getAuthorID() {
        return this.authorID;
    }

    public String getName() {
        return this.name;
    }

    public String getSecondName() {
        return this.secondName;
    }


    //methods
    public void addBook(Book newBook) {
        booksInLibrary.add(newBook);
    }

    public void deleteBook(Book deletedBook) {
        booksInLibrary.remove(deletedBook);
    }

    public int HowManyBooks() {
        return booksInLibrary.size();
    }

    @Override
    public String toString() {
        return getName() + " " + " " + getSecondName() + " " + getAuthorID() + " " + authorID_counter;
    }
}

