package com.example.autosms;

public class Contact {
    private String name;
    private String number;
    private boolean selected;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}