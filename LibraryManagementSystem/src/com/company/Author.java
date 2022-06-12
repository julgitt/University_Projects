package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Author implements Serializable {
    private final long authorID;
    private final String name;
    private final String secondName;
    private final List<Book> booksInLibrary;


    //constructor
    public Author(String name, String secondName, long id) {
        this.authorID = id;
        this.name = name;
        this.secondName = secondName;
        this.booksInLibrary = new ArrayList<>();
    }

    //getters
    public long getAuthorID() {
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
        return getName() + " " + " " + getSecondName() + " " + getAuthorID();
    }
}

