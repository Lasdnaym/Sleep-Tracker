package com.example.sleeptracker;

import androidx.appcompat.app.AppCompatDialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;

//A RatingDialog is a pop-up dialog that allows the user to input information about their sleep
//by rating their sleep quality from 0 to 10
public class RatingDialog extends AppCompatDialogFragment {
    private EditText ratingInput;
    private RatingDialogListener listener;

    @Override
    //creates a dialog that prompts the user to rate their quality of sleep
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rating_dialog, null);

        ratingInput = view.findViewById(R.id.rating);
        builder.setView(view)
               .setTitle("Rate your sleep?")
                //if the user exits the dialog with the back button, set the rating to -1 to signify that
                //a rating does not exist for the current sleep entry
               .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        listener.applyRating(-1);
                    }
               })
                //if the user exits the dialog with the given "No thanks" button, set the rating to -1
                //to signify that a rating does not exist for the current sleep entry
               .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       listener.applyRating(-1);
                   }
               })
                //if the user chooses to submit a rating, make sure the input is valid and send to the activity
               .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       if(ratingInput.getText().toString().length() < 1) {
                           listener.applyRating(0);
                       } else {
                           int rating = Integer.parseInt(ratingInput.getText().toString());
                           listener.applyRating(Math.min(rating, 10));
                       }
                   }
               });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        return alert;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RatingDialogListener) context;
    }

    public interface RatingDialogListener {
        void applyRating(int rating);
    }
}
