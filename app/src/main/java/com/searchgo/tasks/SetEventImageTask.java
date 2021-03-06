package com.searchgo.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.searchgo.nativeServices.PostEventImageService;

/**
 * Created by tamar.twena on 5/2/2016.
 */
public class SetEventImageTask extends AsyncTask<Object, Void, JsonElement> {

    private String eventId;
    private Bitmap bitmap;

    public SetEventImageTask(String detailsId, Bitmap bitmap) {
        this.eventId = detailsId;
        this.bitmap = bitmap;
    }


    @Override
    protected JsonElement doInBackground(Object... params) {
        try {
            String token = (String) params[0];
            String path = (String) params[1];
            PostEventImageService service = new PostEventImageService(bitmap, token, eventId, path);
            service.uploadFile();
            return null;
        } catch (Exception e) {
            Log.e("failedSavePicture", "fail to save picture", e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(JsonElement result) {
        if (result == null) {
            return;
        }
        JsonObject resultJsonObject = result.getAsJsonObject();
    }
}
