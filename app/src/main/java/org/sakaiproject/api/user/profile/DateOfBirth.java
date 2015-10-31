package org.sakaiproject.api.user.profile;

/**
 * Created by vasilis on 10/31/15.
 * user's date of birth
 */
public class DateOfBirth {

    // day of birth
    private int date;
    // current day of birth, 0 for Sunday, 1 for Monday an so on
    private int day;
    // month of birth, starts from 0
    private int month;
    // birth in milliseconds
    private long time;
    private int timezoneOffset;
    // year of birth, eg 93
    private int year;

    public DateOfBirth(int date, int day, int month, long time, int timezoneOffset, int year) {
        this.date = date;
        this.day = day;
        this.month = month;
        this.time = time;
        this.timezoneOffset = timezoneOffset;
        this.year = year;
    }

    public DateOfBirth() {
    }

    public int getDate() {
        return date;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public long getTime() {
        return time;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public int getYear() {
        return year;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
