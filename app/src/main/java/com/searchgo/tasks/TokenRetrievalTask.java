package com.searchgo.tasks;

/**
 * Created by tamar.twena on 12/15/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.searchgo.ITokenRetrieveEnd;
import com.searchgo.application.SearchGoApplication;
import com.searchgo.constants.ApplicationConstants;
import com.searchgo.constants.ServiceConstants;

import org.json.JSONArray;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import com.strongloop.android.loopback.RestAdapter;
import java.util.List;
import java.util.Map;

public class TokenRetrievalTask extends AsyncTask<String, String, String> {

    private ITokenRetrieveEnd tokenRetrieved;
    private static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    private RestAdapter adapter;
    private Activity activity;

    public TokenRetrievalTask(ITokenRetrieveEnd activityContext, Activity context) {
        this.tokenRetrieved=activityContext;
        SearchGoApplication app = (SearchGoApplication) context.getApplication();
        this.adapter = app.getLoopBackAdapter();
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

                    if (HttpCookie.parse(cookie).get(0).getName().equalsIgnoreCase("access_token")) {
                        adapter.setAccessToken(java.net.URLDecoder.decode(HttpCookie.parse(cookie).get(0).getValue(),"UTF-8").split("\\.")[0].split(":")[1]);
                    }
                    if (HttpCookie.parse(cookie).get(0).getName().equalsIgnoreCase("userId")) {
                        result=java.net.URLDecoder.decode(HttpCookie.parse(cookie).get(0).getValue(),"UTF-8").split("\\.")[0].split(":")[1];
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
        SharedPreferences.Editor editor = activity.getApplicationContext().getSharedPreferences(ApplicationConstants.USER_DETAILS_PREFERENCES, Context.MODE_PRIVATE).edit();
        String json = new JSONArray().put(result).toString();
        editor.putString(ApplicationConstants.USER_ID, json);
        editor.commit();
        tokenRetrieved.tokenRetrieveEnd(ApplicationConstants.SUCCESS_RESPONSE_CODE);
    }



}
