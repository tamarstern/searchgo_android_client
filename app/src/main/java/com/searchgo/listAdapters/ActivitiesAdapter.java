package com.searchgo.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.searchgo.R;
import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.searchgo.dto.service.EmergencyEventServiceDto;

import java.util.ArrayList;

/**
 * Created by tamar.twena on 12/1/2017.
 */

public class ActivitiesAdapter extends ArrayAdapter<EmergencyEventServiceDto> {

    private final Context context;
    private ArrayList<EmergencyEventServiceDto> details;

    private TextView activityName;


    public ActivitiesAdapter(Context context, ArrayList<EmergencyEventServiceDto> details) {
        super(context, 0, details);
        this.context = context;
        this.details = details;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activities_list_item, parent, false);

        final EmergencyEventServiceDto activityDto = details.get(position);

        activityName = rowView.findViewById(R.id.activity_name);
        activityName.setText(activityDto.getName());

        return rowView;
    }

}
