package com.what.does.date.says.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.what.does.date.says.MainActivity;
import com.what.does.date.says.Utils.DataFetch;

import java.util.Calendar;

/**
 * Created by Nitin on 3/27/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    String dateSelected = "";
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateSelected = day+"/"+(month+1)+"/"+year;
        sendResult(0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra("DATE", dateSelected);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}
