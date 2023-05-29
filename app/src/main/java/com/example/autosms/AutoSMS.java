package com.example.autosms;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AutoSMS {
    private String title;
    private String message;
    private List<String> simCards;
    private List<String> numbers;
    private Boolean[] days;
    private String time;
    private long timestamp;

    public AutoSMS(String title, String message, List<String> simCards, List<String> numbers, Boolean[] days, String time, long timestamp) {
        this.title = title;
        this.message = message;
        this.simCards = simCards;
        this.numbers = numbers;
        this.days = days;
        this.time = time;
        this.timestamp = timestamp;
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

    public List<String> getSimCards() {
        return simCards;
    }

    public void setSimCards(List<String> simCards) {
        this.simCards = simCards;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public Boolean[] getDays() {
        return days;
    }

    public void setDays(Boolean[] days) {
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
