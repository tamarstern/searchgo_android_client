package com.searchgo;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.searchgo.dto.service.EmergencyEventServiceDto;

import java.io.IOException;
import java.util.List;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

public class SearchEventActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String LAT = "lat";
    public static final String LONG = "long";
    private GoogleMap mMap;

    private static final String TAG = "ErrAddrDetails";

    private String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.search_event_details_ttl);

        Intent intent = getIntent();
        EmergencyEventServiceDto eventDto = (EmergencyEventServiceDto)intent.getExtras().get(EVENT_DTO);
        address = eventDto.getAddress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(!TextUtils.isEmpty(address)) {
            LatLng addressToDisplay = getLocationFromAddress(this, address);
            mMap.addMarker(new MarkerOptions().position(addressToDisplay).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(addressToDisplay));
            mMap.setMinZoomPreference(15);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(point.latitude, point.longitude)).title("New Marker");

                    mMap.addMarker(marker);

                    Log.i("MarkerAdded" , point.latitude+"---"+ point.longitude);
                }
            });
            Intent result = new Intent();
            result.putExtra(LAT, addressToDisplay.latitude);
            result.putExtra(LONG, addressToDisplay.longitude);
            setResult(1, result);
            finish();
        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {
            Log.e(TAG, "cannot get details of error",  ex);
        }

        return p1;
    }




}
