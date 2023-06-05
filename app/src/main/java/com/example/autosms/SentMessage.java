package com.example.autosms;

import java.util.List;

public class SentMessage {
    private String title;
    private String time;
    private String number;
    private String message;

    public SentMessage(String title, String time, String number, String message) {
        this.title = title;
        this.time = time;
        this.number = number;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.title = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
