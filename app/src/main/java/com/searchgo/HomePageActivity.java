package com.searchgo;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.service.EmergencyEventRepository;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.searchgo.fragments.PageAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

public class HomePageActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public final static int SEARCH_FOR_LOCATION_FOR_EVENT_CREATION = 8000;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int DEFAULT_ZOOM = 15;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final String TAG = HomePageActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPlaceAutocompleteActivityIntent();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }


    private void callPlaceAutocompleteActivityIntent() {
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
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void getCurrentLocation() {
        int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if ((permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        executeFindLocationService();
    }

    private void executeFindLocationService() throws SecurityException {
        try {
//            if (mLocationPermissionGranted) {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, DEFAULT_ZOOM));
                        getNearByEvents(latLng, 100);
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
//            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showNearByEvents(List<EmergencyEventServiceDto> events) {
        if (events != null) {
            for (EmergencyEventServiceDto ev : events) {
                HashMap<String, Double> startingPoint = ev.getStartingPoint();
                if (startingPoint != null) {
                    Double lat = startingPoint.containsKey("lat") ? startingPoint.get("lat") : 0.0;
                    Double lng = startingPoint.containsKey("lng") ? startingPoint.get("lng") : 0.0;
                    LatLng loc = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions()
                            .title(ev.getName())
                            .position(loc)
                            .snippet(ev.getDescription()));
                }
            }
        }
    }

    private void getNearByEvents(LatLng latLng, int radius) {
        EmergencyEventRepository emergencyEventRepository = EmergencyEventServiceDtoFactory.getEmergencyEventRepository((SearchGoApplication) this.getApplication());
        emergencyEventRepository.getNearByEvents(latLng, radius, new ListCallback<EmergencyEventServiceDto>() {
            @Override
            public void onSuccess(List<EmergencyEventServiceDto> objects) {
                showNearByEvents(objects);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("failGetMyEvents", "failed to get nearby events", t);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    executeFindLocationService();
                } else {
                    String message = getResources().getString(R.string.need_permissions_to_get_location);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                intent.putExtra(EVENT_DTO, emergencyEventServiceDto);
                startActivity(intent);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("FailedToGetPlace", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home Page"));
        tabLayout.addTab(tabLayout.newTab().setText("My Activities"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("GetLocationFailed", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        getCurrentLocation();

    }
}
