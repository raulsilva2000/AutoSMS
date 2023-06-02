package com.example.autosms;

public class BackupItem {
    private String date;
    private boolean checked;

    public BackupItem(String date) {
        this.date = date;
        this.checked = false;
    }

    public String getDate() {
        return date;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
