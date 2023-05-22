package com.example.autosms;

public class Contact {

    private String name;
    private String phoneNumber;
    private boolean selected;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isSelected() {
        return selected;
    }
}