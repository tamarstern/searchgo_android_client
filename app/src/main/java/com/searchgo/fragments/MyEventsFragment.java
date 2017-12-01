package com.searchgo.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.searchgo.R;
import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.searchgo.listAdapters.ActivitiesAdapter;

import java.util.ArrayList;

/**
 * Created by tamar.twena on 11/21/2017.
 */

public class MyEventsFragment extends Fragment {

    private ListView listView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.my_events_fragment, container, false);

        listView = inflate.findViewById(R.id.activities_list);

        ArrayList<EmergencyEventActivityDto> activities = new ArrayList<>();
        EmergencyEventActivityDto activity1 = new EmergencyEventActivityDto();
        activity1.setName("name1");
        activities.add(activity1);
        EmergencyEventActivityDto activity2 = new EmergencyEventActivityDto();
        activity2.setName("name2");
        activities.add(activity2);

        ActivitiesAdapter adapter = new ActivitiesAdapter(getActivity(), activities);
        listView.setAdapter(adapter);
        return inflate;
    }
}
