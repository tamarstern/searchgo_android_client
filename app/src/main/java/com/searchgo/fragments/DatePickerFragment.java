package com.searchgo.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.searchgo.fragments.com.searchgo.fragments.interfaces.IDateSelectorListener;

/**
 * Created by tamar.twena on 11/23/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private List<IDateSelectorListener> dateSelectorListeners = new ArrayList<IDateSelectorListener>();

    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

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

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Date date = new GregorianCalendar(year, month, day).getTime();
        for (IDateSelectorListener listener : dateSelectorListeners) {
            listener.OnDateSelected(date);
        }
    }


    public void registerListener(IDateSelectorListener listener) {

        dateSelectorListeners.add(listener);

    }
}