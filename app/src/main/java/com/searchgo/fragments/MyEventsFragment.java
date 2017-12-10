package com.searchgo.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.searchgo.R;
import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.searchgo.dto.service.EmergencyEventRepository;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.searchgo.listAdapters.ActivitiesAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.remoting.adapters.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tamar.twena on 11/21/2017.
 */

public class MyEventsFragment extends Fragment {

    private ListView listView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.my_events_fragment, container, false);

        listView = inflate.findViewById(R.id.activities_list);

        callServerOnStart();

        /*ArrayList<EmergencyEventActivityDto> activities = new ArrayList<>();
        EmergencyEventActivityDto activity1 = new EmergencyEventActivityDto();
        activity1.setName("name1");
        activities.add(activity1);
        EmergencyEventActivityDto activity2 = new EmergencyEventActivityDto();
        activity2.setName("name2");
        activities.add(activity2);

        ActivitiesAdapter adapter = new ActivitiesAdapter(getActivity(), activities);
        listView.setAdapter(adapter);*/
        return inflate;
    }

    private void initListView(ArrayList<EmergencyEventServiceDto> dtos) {
        Activity activity = getActivity();
        ActivitiesAdapter adapter = new ActivitiesAdapter(activity, dtos);
        listView.setAdapter(adapter);
    }


    private void callServerOnStart() {
        SearchGoApplication app = (SearchGoApplication)getActivity().getApplication();
        EmergencyEventRepository emergencyEventRepository = EmergencyEventServiceDtoFactory.getEmergencyEventRepository(app);

        emergencyEventRepository.findWithFilter("{\"where\" : {\"name\" : { \"like\" : \"name\"}} }", new ListCallback<EmergencyEventServiceDto>() {
            @Override
            public void onSuccess(List<EmergencyEventServiceDto> dtos) {
                ArrayList<EmergencyEventServiceDto> listDtos = new ArrayList<>();
                listDtos.addAll(dtos);
                initListView(listDtos);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }


}
