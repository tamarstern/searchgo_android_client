package com.searchgo.tasks;

/**
 * Created by tamar.twena on 12/15/2017.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.searchgo.ITokenRetrieveEnd;
import com.searchgo.constants.ApplicationConstants;
import com.searchgo.constants.ServiceConstants;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;

import com.searchgo.utils.LoginUtils;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

public class TokenRetrievalTask extends AsyncTask<String, String, String> {

    public static final String ACCESS_TOKEN_HEADER = "access_token";
    public static final String USER_ID_HEADER = "userId";
    public static final String UTF_8_ENCODING = "UTF-8";
    private ITokenRetrieveEnd tokenRetrieved;
    private static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    private Activity activity;

    public TokenRetrievalTask(ITokenRetrieveEnd activityContext, Activity context) {
        this.tokenRetrieved=activityContext;
        this.activity = context;
    }
    protected String doInBackground(String... tokens) {
        String result="";
        try {
            URL url = new URL(ServiceConstants.SERVER_FACEBOOK_TOKEN_CALLBACK + tokens[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.connect();

            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(ServiceConstants.COOKIE_HEADER);
            System.out.println(urlConnection.getHeaderFields().toString());
            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    if (HttpCookie.parse(cookie).get(0).getName().equalsIgnoreCase(ACCESS_TOKEN_HEADER)) {
                        String accessToken = URLDecoder.decode(HttpCookie.parse(cookie).get(0).getValue(), UTF_8_ENCODING).split("\\.")[0].split(":")[1];
                        LoginUtils.initAccessTokenForRestCalls(accessToken, activity);
                        LoginUtils.StoreAccessToken(accessToken, activity);
                    }
                    if (HttpCookie.parse(cookie).get(0).getName().equalsIgnoreCase(USER_ID_HEADER)) {
                        result=java.net.URLDecoder.decode(HttpCookie.parse(cookie).get(0).getValue(), UTF_8_ENCODING).split("\\.")[0].split(":")[1];
                    }
                }
            }
            urlConnection.disconnect();

        }catch(Exception e) {
            String msg = "Messup when calling home";
            Log.e("LoopBack", msg, e);
        }
        return result;
    }

    protected void onPostExecute(String result) {
        LoginUtils.StoreUserId(result, activity);
        tokenRetrieved.tokenRetrieveEnd(ApplicationConstants.SUCCESS_RESPONSE_CODE);
    }



}
