package com.searchgo;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.searchgo.application.SearchGoApplication;
import com.searchgo.backgroundServices.BackgroundServiceScheduler;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.searchgo.fragments.DateSelectorFragment;
import com.searchgo.utils.ActivityUtils;
import com.searchgo.utils.ImageSelectorUtils;
import com.strongloop.android.loopback.Model;

import java.util.Date;
import java.util.HashSet;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

public class CreateEventActivity extends AppCompatActivity {

    private Button saveAndContinue;
    private Button addImageButton;
    private ImageView eventImage;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private EmergencyEventServiceDto event;
    private RadioGroup radioCategoryGroup;
    private EditText eventNameEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Intent intent = getIntent();
        event = (EmergencyEventServiceDto)intent.getExtras().get(EVENT_DTO)
        ;

        radioCategoryGroup = findViewById(R.id.radio_category_group);

        final Activity activity = this;

        saveAndContinue = (Button)findViewById(R.id.save_and_continue);
        final SearchGoApplication application = (SearchGoApplication)this.getApplication();
        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initEventNameOnSave();
                initCategoryOnSave();
                initLastSeenOnSave();
                EmergencyEventServiceDto emergencyEventServiceDto = EmergencyEventServiceDtoFactory.generateEmergencyEventServiceDto(application, event);
                sendSaveEventRequest(emergencyEventServiceDto);
                ActivityUtils.startEventPageActivity(activity, event);
            }
        });


        eventNameEditText = findViewById(R.id.enter_event_name);

        addImageButton = findViewById(R.id.add_picture_btn);
        addImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changePictureAction();

            }

        });

        eventImage = findViewById(R.id.event_picture);
    }

    private void sendSaveEventRequest(final EmergencyEventServiceDto serviceDto) {
        final Activity activity = this;
        serviceDto.save(new Model.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Throwable t) {
                Log.e("ErrorOnSave", "Cannot save Emergency Event model.", t);
                HashSet<EmergencyEventServiceDto> searchEventsToSave = ((SearchGoApplication) activity.getApplication()).getSearchEventsToSave();
                searchEventsToSave.add(serviceDto);
                if(!BackgroundServiceScheduler.isSaveEventServiceRunning()) {
                    BackgroundServiceScheduler.scheduleSaveEmergencyEventService(activity);
                }
            }
        });

    }


    private void changePictureAction() {

        int permissionCheckWriteExternal = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckReadExternal = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);

        if((permissionCheckWriteExternal != PackageManager.PERMISSION_GRANTED)
                || (permissionCheckReadExternal != PackageManager.PERMISSION_GRANTED)
                || (permissionCheckCamera != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        ImageSelectorUtils.selectImage(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap thumbnail = ImageSelectorUtils.initializeImage(requestCode, resultCode, data, this.eventImage, this.addImageButton,
                this);
    }



    private void initEventNameOnSave() {
        String name = eventNameEditText.getText().toString();
        event.setName(name);
    }

    private void initLastSeenOnSave() {
        DateSelectorFragment fragment = (DateSelectorFragment) getSupportFragmentManager().findFragmentById(R.id.enter_last_seen);
        Date lastSeen = fragment.getSelectedDate();
        event.setLastSeen(lastSeen);
    }


    private void initCategoryOnSave() {
        int selectedId = radioCategoryGroup.getCheckedRadioButtonId();
        RadioButton radioCategoryButton = (RadioButton) findViewById(selectedId);
        String categoryText = radioCategoryButton.getText().toString();
        event.setCategory(categoryText);
    }

}
