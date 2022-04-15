package com.example.sleeptracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Calendar;

//assuming that a week is sunday -> saturday, provides recommendations for the rest of the week based on what you've already slept
public class Recommendations extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;  // sleep stats
    private static final int MAXHOURS = 9;  // maximum hours of sleep recommended each night
    private static final int MINHOURS = 7;  // minimum hours of sleep recommended each night

    // text components
    private TextView displayAvgSleep;
    private TextView displayRecommendations;
    private TextView displayDate;

    // days of week
    private long beginningOfWeek;
    private long currDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recommendations);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);
        displayAvgSleep = this.findViewById(R.id.display_avg_sleep);
        displayRecommendations = this.findViewById(R.id.display_recommendation);
        displayDate = this.findViewById(R.id.display_date);

        Calendar calendar = Calendar.getInstance();
        currDate = calendar.getTimeInMillis();
        displayDate.setText(SleepStats.getDateFromLong(currDate).toString());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        beginningOfWeek = calendar.getTimeInMillis();
        getSleepRecommendation();
    }

    // if the average sleep is below the minimum recommended hours, state how many hours are need each night
    //   to reach the recommended average
    private void getSleepRecommendation() {
        List<SleepStats> weeklyStats = sleepStatsDatabase.getBetweenDates(beginningOfWeek, currDate);
        if(weeklyStats.size() <= 0) {
            displayAvgSleep.setText("We haven't found any data for this week.");
            return;
        }
        double totalHoursOfSleep = 0.0;
        for(SleepStats stats : weeklyStats) {
            totalHoursOfSleep += stats.getHoursSlept();
        }
        int numDaysPassed = SleepStats.getDateFromLong(currDate).getDay() - SleepStats.getDateFromLong(beginningOfWeek).getDay() + 1;
        int numDaysRemaining = 7 - numDaysPassed;
        if(numDaysRemaining > 0) {
            double recommendedSleep = ((MINHOURS * 7) - totalHoursOfSleep) / numDaysRemaining;
            if(recommendedSleep <= MAXHOURS) {
                displayRecommendations.setText(String.format("Congratulations! You've been sleeping just fine. Make sure to continue getting at least %d hours of sleep a day!", MINHOURS));
            } else {
                displayRecommendations.setText(String.format("We recommend sleeping at least %.2f hours a day for the remainder of the week to get your ideal amount of sleep!", recommendedSleep));
            }
        }
        totalHoursOfSleep /= numDaysPassed;
        displayAvgSleep.setText((String.format("You've slept an average of %.2f hours each day this week.", totalHoursOfSleep)));
    }

    // switches page to DisplayStats
    public void recommendationsToDisplayStats(View view) {
        Intent activitySwitchIntent = new Intent(Recommendations.this, DisplayCustomStats.class);
        startActivity(activitySwitchIntent);
    }

    // switches page to GetSleepInfo
    public void recommendationsToGetSleepInfo(View view) {
        Intent activitySwitchIntent = new Intent(Recommendations.this, GetSleepInfo.class);
        startActivity(activitySwitchIntent);
    }
}
