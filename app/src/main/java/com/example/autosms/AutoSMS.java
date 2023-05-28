package com.example.autosms;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class AutoSMS {
    private String title;
    private String message;
    private ArrayList<String> simCards;
    private ArrayList<String> numbers;
    private String days;
    private String time;
    private long timestamp;

    public AutoSMS(String title, String message, ArrayList<String> simCards, ArrayList<String> numbers, String days, String time) {
        this.title = title;
        this.message = message;
        this.simCards = simCards;
        this.numbers = numbers;
        this.days = days;
        this.time = time;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getSimCards() {
        return simCards;
    }

    public void setSimCards(ArrayList<String> simCards) {
        this.simCards = simCards;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
