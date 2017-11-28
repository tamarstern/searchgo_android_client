package com.searchgo;


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
import android.widget.Toast;

import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.searchgo.fragments.DateSelectorFragment;
import com.searchgo.utils.ImageSelectorUtils;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.RestAdapter;

import java.util.Date;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

public class CreateEventActivity extends AppCompatActivity {

    private Button saveAndContinue;
    private Button addImageButton;
    private ImageView eventImage;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private EmergencyEventActivityDto event;
    private RadioGroup radioCategoryGroup;
    private EditText eventNameEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Intent intent = getIntent();
        event = (EmergencyEventActivityDto)intent.getExtras().get(EVENT_DTO);

        radioCategoryGroup = (RadioGroup) findViewById(R.id.radio_category_group);

        saveAndContinue = (Button)findViewById(R.id.save_and_continue);
        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initEventNameOnSave();
                initCategoryOnSave();
                initLastSeenOnSave();
                sendSaveEventRequest();
                startEventPageActivity();
            }
        });


        eventNameEditText = (EditText) findViewById(R.id.enter_event_name);

        addImageButton = (Button) findViewById(R.id.add_picture_btn);
        addImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changePictureAction();

            }

        });

        eventImage = (ImageView)findViewById(R.id.event_picture);
    }

    private void sendSaveEventRequest() {
        SearchGoApplication application = (SearchGoApplication)getApplication();
        RestAdapter adapter = application.getLoopBackAdapter();
        EmergencyEventServiceDto serviceDto = EmergencyEventServiceDtoFactory.generateEmergencyEventService(adapter, event);
        serviceDto.save(new Model.Callback() {

            @Override
            public void onSuccess() {
                showResult("Saved!");
            }

            @Override
            public void onError(Throwable t) {
                Log.e("ErroOnSave", "Cannot save Note model.", t);
                showResult("Failed.");
            }
        });

    }

    void showResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    private void startEventPageActivity(){
         Intent intent = new Intent(this, SearchEventActivity.class);
         intent.putExtra(EVENT_DTO, event);
         startActivity(intent);
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
