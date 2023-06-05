package com.example.autosms;

public class SIMCard {
    private String name;
    private String slotNumber;
    private boolean selected;

    public SIMCard(String name, String slotNumber) {
        this.name = name;
        this.slotNumber = slotNumber;
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
