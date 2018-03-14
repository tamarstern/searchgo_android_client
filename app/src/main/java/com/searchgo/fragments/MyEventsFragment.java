package com.searchgo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.searchgo.R;
import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.service.EmergencyEventRepository;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.searchgo.listAdapters.ActivitiesAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

        return inflate;
    }

    private void initListView(ArrayList<EmergencyEventServiceDto> dtos) {
        Activity activity = getActivity();
        ActivitiesAdapter adapter = new ActivitiesAdapter(activity, dtos);
        listView.setAdapter(adapter);
    }


    private void callServerOnStart() {
        final SearchGoApplication app = (SearchGoApplication)getActivity().getApplication();
        HashSet<EmergencyEventServiceDto> myEvents = app.getMyEvents();
        if(myEvents.size() > 0) {
            initEventsFromServer(myEvents);
        }
        EmergencyEventRepository emergencyEventRepository = EmergencyEventServiceDtoFactory.getEmergencyEventRepository(app);

        /*emergencyEventRepository.findEventsUserCreated(new ListCallback<EmergencyEventServiceDto>() {
            @Override
            public void onSuccess(List<EmergencyEventServiceDto> dtos) {
                initEventsFromServer(dtos);
                app.resetMyEvents(dtos);
            }

            @Override
            public void onError(Throwable t) {

            }
        });*/
    }

    private void initEventsFromServer(Collection<EmergencyEventServiceDto> myEvents) {
        ArrayList<EmergencyEventServiceDto> listDtos = new ArrayList<>();
        listDtos.addAll(myEvents);
        initListView(listDtos);
    }


}
