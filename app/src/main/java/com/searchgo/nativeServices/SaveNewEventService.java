package com.searchgo.nativeServices;

import android.app.Activity;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.searchgo.constants.ServiceConstants;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.utils.LoginUtils;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * Created by tamar.twena on 1/7/2018.
 */

public class SaveNewEventService extends BaseService {

    private EmergencyEventServiceDto dto;
    private Activity activity;

    public SaveNewEventService(EmergencyEventServiceDto dto, Activity activity) {
        this.dto = dto;
        this.activity = activity;
    }

    @Override
    protected String getRequestBody() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String data = gson.toJson(dto);

        return data;
    }

    @Override
    protected void addBodyParameters(Uri.Builder builder) {

    //    builder.appendQueryParameter(ServiceConstants.ACCESS_TOKEN, LoginUtils.getAccessToken(activity));
    }

    @Override
    protected String getUrl() {
        return ServiceConstants.SERVER_API_URL + "/" + ServiceConstants.EVENT_URI_TO_SAVE;
    }

    @Override
    protected void addHeaderProperties(HttpURLConnection conn) {
        conn.setRequestProperty(ServiceConstants.CONTENT_TYPE, ServiceConstants.APPLICATION_JSON);
        conn.setRequestProperty(ServiceConstants.ACCEPTS, ServiceConstants.APPLICATION_JSON);
        conn.setRequestProperty(ServiceConstants.AUTHORIZATION, LoginUtils.getAccessToken(activity));


    }

    @Override
    protected void setHttpMethod(HttpURLConnection conn) throws ProtocolException {
        conn.setRequestMethod(ServiceConstants.POST_REQUEST_TYPE);
    }
}
