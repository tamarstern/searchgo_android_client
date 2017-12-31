package com.searchgo.constants;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class ServiceConstants {

    public static final String SERVER_URL = "http://10.0.2.2:3000/";
    public static final String SERVER_API_URL = SERVER_URL + "api";
    public static final String EVENT_PICTURE_URL_POST =  SERVER_API_URL + "/attachments/search_event_files/upload";
    public static final String SERVER_FACEBOOK_TOKEN_CALLBACK = SERVER_URL + "auth/facebook-token/callback?access_token=";
    public static final String LIST_ITEMS_ELEM = "listObjects";
    public static final String COOKIE_HEADER = "set-cookie";
    public static final String X_ACCESS_TOKEN = "x-access-token";
}
