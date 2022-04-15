package com.example.sleeptracker;

import android.content.Context;

import java.util.Date;
import java.util.Random;

//AddingFakeData is a class that adds randomized sleep data into the database to show what a sample user might input over the course of 9 days.
//We recommend using this fake data to test the functionality of our app, since the app uses the current calendar time for logging the user's
//sleep and wake times. However, if you are interested in viewing the app without the fake data, you can comment out the function call in the
//getInstance() method in the SleepStatsDatabase class.
//We hope you enjoy our app!
public class AddingFakeData {
    public static void addDataIntoDatabase(Context context) {
        SleepStatsDatabase sleepStatsDatabase = SleepStatsDatabase.getInstance(context);
        Random myRandom = new Random();
        for(int i = 1; i <= 9; i++) {
            Date fakeSleepTime = new Date(120, 11, i, myRandom.nextInt(4) + 20, myRandom.nextInt(60), myRandom.nextInt(60));
            Date fakeWakeUpTime = new Date(120, 11, i + 1, myRandom.nextInt(6) + 5, myRandom.nextInt(60), myRandom.nextInt(60));
            int rating = myRandom.nextInt(11);
            sleepStatsDatabase.insert(fakeSleepTime.getTime(), fakeWakeUpTime.getTime(), rating);
        }
    }
}
