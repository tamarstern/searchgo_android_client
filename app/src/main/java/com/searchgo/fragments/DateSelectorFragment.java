package com.searchgo.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.searchgo.R;
import com.searchgo.fragments.com.searchgo.fragments.interfaces.IDateSelectorListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tamar.twena on 11/25/2017.
 */

public class DateSelectorFragment extends Fragment implements IDateSelectorListener {



    private Button changeDateButton;
    private EditText enterDate;
    private Date selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.date_selector_fragment, container, false);
        changeDateButton = inflate.findViewById(R.id.enter_date_btn);
        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
        enterDate = inflate.findViewById(R.id.enter_date);
        return inflate;
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.registerListener(this);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public void OnDateSelected(Date selectedDate) {
        String formatedDate = new SimpleDateFormat("dd/MM/yyyy").format(selectedDate);
        enterDate.setText(formatedDate);
        this.selectedDate = selectedDate;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }
}
