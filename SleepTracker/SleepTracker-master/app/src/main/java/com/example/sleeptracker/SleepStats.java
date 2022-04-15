package com.example.sleeptracker;

import java.util.Date;

//A SleepStats object stores various information about a single sleep entry, which includes
//sleep time, wake up time, number of hours slept, and quality of sleep. This class also contains
//helper methods for dealing with and manipulating dates.
public class SleepStats {
    private static final int MILLISECONDS_PER_HOUR = 3600000;
    private long sleepTime;
    private long wakeTime;
    private int rating;
    private double hoursSlept;

    //creates a new SleepStats object from a given sleepTime, wakeTime, and rating.
    //sleepTime and wakeTime represent the exact time of sleeping/waking up in milliseconds that have
    //passed since January 1, 1970 00:00:00 UTC
    public SleepStats(long sleepTime, long wakeTime, int rating) {
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.rating = rating;
        this.hoursSlept = (double)((wakeTime - sleepTime) / MILLISECONDS_PER_HOUR);
    }

    //returns the exact time of sleep
    public long getSleepTime() {
        return this.sleepTime;
    }

    //returns the exact time of waking up
    public long getWakeTime() {
        return this.wakeTime;
    }

    //returns the quality of sleep
    public int getRating() {
        return this.rating;
    }

    //returns the number of hours slept
    public double getHoursSlept() {
        return this.hoursSlept;
    }

    //converts a long representing the number of milliseconds from January 1, 1970 00:00:00 UTC to
    //a Date object
    public static Date getDateFromLong(long dateInMilliseconds) {
        return new Date(dateInMilliseconds);
    }

    //given an integer day, month, and year, returns a long representing the number of milliseconds
    //that have passed from January 1, 1970 00:00:00 UTC to the beginning of that date
    public static long getBeginningOfDay(int day, int month, int year) {
        Date date = new Date(year - 1900, month - 1, day, 0, 0, 0);
        return date.getTime();
    }

    //given an integer day, month, and year, returns a long representing the number of milliseconds
    //that have passed from January 1, 1970 00:00:00 UTC to the end of that date.
    public static long getEndOfDay(int day, int month, int year) {
        Date date = new Date(year - 1900, month - 1, day, 23, 59, 59);
        return date.getTime();
    }
}
