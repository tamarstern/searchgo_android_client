package com.searchgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;

import java.util.HashMap;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

public class CreateEventOrch extends AppCompatActivity {
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_orch);
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String address = place.getAddress().toString();
                Intent intent = new Intent(this, CreateEventActivity.class);
                SearchGoApplication app = (SearchGoApplication)this.getApplication();
                EmergencyEventServiceDto emergencyEventServiceDto = EmergencyEventServiceDtoFactory.generateEmergencyEventServiceDto(app);
                emergencyEventServiceDto.setAddress(address);
                HashMap<String, Double> geoMap = new HashMap<>();
                geoMap.put("lat", place.getLatLng().latitude);
                geoMap.put("lng", place.getLatLng().longitude);
                emergencyEventServiceDto.setStartingPoint(geoMap);
                intent.putExtra(EVENT_DTO, emergencyEventServiceDto);
                startActivity(intent);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("FailedToGetPlace", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {
                // TODO: Handle the error.
            }
        }
    }
}
