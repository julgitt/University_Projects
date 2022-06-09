package com.company;

import java.io.Serializable;

public class User implements Serializable {
    private final int userID;
    static int userID_counter;
    private final String name;
    private final String secondName;
    private final int phoneNumber;

    //constructor
    public User(String name, String secondName, int phoneNumber)  {
        this.userID = userID_counter;
        userID_counter++;
        this.name = name;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
    }

    //getters
    public int getUserID() {
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
        return getName() + " " + getSecondName() + " " + getPhoneNumber() + " " + getUserID() + " " + userID_counter;
    }
}
