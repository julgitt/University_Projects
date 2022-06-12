package com.company;

import java.io.Serializable;

public class User implements Serializable {
    private final long userID;
    private final String name;
    private final String secondName;
    private final int phoneNumber;

    //constructor
    public User(String name, String secondName, int phoneNumber, long id)  {
        this.userID = id;
        this.name = name;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
    }

    //getters
    public long getUserID() {
        return this.userID;
    }

    public String getName() {
        return this.name;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    //methods

    @Override
    public String toString() {
        return getName() + " " + getSecondName() + " " + getPhoneNumber() + " " + getUserID();
    }
}
