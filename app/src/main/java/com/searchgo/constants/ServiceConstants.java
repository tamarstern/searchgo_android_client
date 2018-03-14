package com.searchgo.constants;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class ServiceConstants {

    public static final String SERVER_URL = "http://10.0.2.2:3000/";
    public static final String SERVER_API_URL = SERVER_URL + "api";
    public static final String EVENT_URI_TO_SAVE = "EmergencyEvents";
    public static final String EVENT_PICTURE_URL_POST =  SERVER_API_URL + "/attachments/container1/upload";

    public static final String SERVER_FACEBOOK_TOKEN_CALLBACK = SERVER_URL + "auth/facebook-token/callback?access_token=";
    public static final String LIST_ITEMS_ELEM = "listObjects";
    public static final String COOKIE_HEADER = "set-cookie";
    public static final String UTF_8 = "UTF-8";
    public static final String POST_REQUEST_TYPE = "POST";
    public static final String PUT_REQUEST_TYPE = "PUT";
    public static final String GET_REQUEST_TYPE = "GET";
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPTS = "Accept";
    public static final String APPLICATION_JSON = "\"application/json\"";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String AUTHORIZATION = "Authorization";
}
