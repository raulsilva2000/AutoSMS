package com.example.autosms;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AutoSMS {
    private String title;
    private String message;
    private String simCard;
    private List<String> numbers;
    private Boolean[] days;
    private String timeFrom;
    private String timeTo;
    private long timestamp;

    public AutoSMS(String title, String message, String simCard, List<String> numbers, Boolean[] days, String timeFrom, String timeTo, long timestamp) {
        this.title = title;
        this.message = message;
        this.simCard = simCard;
        this.numbers = numbers;
        this.days = days;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
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

    public String getSimCard() {
        return simCard;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
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

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
