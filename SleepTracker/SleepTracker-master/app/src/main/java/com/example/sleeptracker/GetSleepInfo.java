package com.example.sleeptracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;

import java.util.Calendar;

//GetSleepInfo manages the user's ability to log sleep entries. This includes giving them the option
//to store when they sleep, when they wake up, and how they rate their quality of sleep
public class GetSleepInfo extends AppCompatActivity implements RatingDialog.RatingDialogListener {
    private SleepStatsDatabase sleepStatsDatabase;
    private Button sleepWakeButton;
    private boolean hasStoredSleepTime;
    private long storedSleepTime;
    private long storedWakeTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_sleep_info);
        AddingFakeData.addDataIntoDatabase(this);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);
        sleepWakeButton = this.findViewById(R.id.sleep_wake_button);
        hasStoredSleepTime = false;
        storedSleepTime = 0;
    }

    @Override
    //when the app is closed, store the current state of the activity, such as whether a sleep time has already
    //been logged
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("LogTimeButtonText", sleepWakeButton.getText().toString());
        savedInstanceState.putBoolean("HasStoredSleepTime", hasStoredSleepTime);
        savedInstanceState.putLong("StoredSleepTime", storedSleepTime);
    }

    @Override
    //when the app is opened, retrieve the previous state of the activity, such as whether a sleep time
    //had already been logged
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sleepWakeButton.setText(savedInstanceState.getString("LogTimeButtonText"));
        hasStoredSleepTime = savedInstanceState.getBoolean("HasStoredSleepTime");
        storedSleepTime = savedInstanceState.getLong("StoredSleepTime");
    }

    //when the user clicks the sleep/wake up button, log either the sleep time or the wake time
    //based on how many times the button had been clicked previously
    //if the user is logging a wake up time, give them the option to rate the quality of their sleep
    public void setTimes(View view) {
        long currTime = Calendar.getInstance().getTimeInMillis();
        if(hasStoredSleepTime) {
            setRating();
            storedWakeTime = currTime;
            sleepWakeButton.setText("Sleep");
        } else {
            storedSleepTime = currTime;
            sleepWakeButton.setText("Wake Up");
        }
        hasStoredSleepTime = !hasStoredSleepTime;
    }

    //creates a RatingDialog to ask the user to rate their quality of sleep
    private void setRating() {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.show(getSupportFragmentManager(), "submit rating");
    }

    @Override
    //once the user has finished rating their sleep quality, store that information, along with the
    //sleep and wake up times, in the database
    public void applyRating(int rating) {
        sleepStatsDatabase.insert(storedSleepTime, storedWakeTime, rating);
    }

    //change activities to the DisplayCustomStats activity
    public void getSleepInfoToDisplayStats(View view) {
        Intent activitySwitchIntent = new Intent(GetSleepInfo.this, DisplayCustomStats.class);
        startActivity(activitySwitchIntent);
    }

    //change activities to the Recommendations activity
    public void getSleepInfoToRecommendations(View view) {
        Intent activitySwitchIntent = new Intent(GetSleepInfo.this, Recommendations.class);
        startActivity(activitySwitchIntent);
    }
}