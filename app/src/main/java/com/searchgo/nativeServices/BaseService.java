package com.searchgo.nativeServices;


import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.searchgo.constants.ServiceConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public abstract class BaseService {


    public JsonElement service() throws Exception {
        InputStream is = null;
        InputStream caInput = null;
        OutputStream os  = null;
        try {

            URL url = new URL(getUrl());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            addHeaderProperties(conn);

            conn.setReadTimeout(ServiceConstants.READ_TIMEOUT);
            conn.setConnectTimeout(ServiceConstants.CONNECTION_TIMEOUT);
            setHttpMethod(conn);

            if (!conn.getRequestMethod().equals(ServiceConstants.GET_REQUEST_TYPE)) {
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                addBodyParameters(builder);
                String requestBody = getRequestBody();
                if (!TextUtils.isEmpty(requestBody)) {
                    os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, ServiceConstants.UTF_8));
                    writer.write(requestBody);
                    writer.flush();
                    writer.close();
                    os.close();
                }

            }

            conn.connect();
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is);
            contentAsString.replaceAll("\\\\", "");
            JsonElement loginResponse = new Gson().fromJson(contentAsString, JsonElement.class);
            return loginResponse;
        } finally {
            if (caInput != null) {
                caInput.close();
            }
            if (is != null) {
                is.close();
            }
            if(os != null) {
                os.close();
            }
        }
    }

    protected String getRequestBody() {
        return null;
    }

    protected abstract void addBodyParameters(Uri.Builder builder);

    protected abstract String getUrl();

    protected abstract void addHeaderProperties(HttpURLConnection conn);

    protected abstract void setHttpMethod(HttpURLConnection conn) throws ProtocolException;

    private String readIt(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }

    public static String toCurlRequest(HttpURLConnection connection, byte[] body) {
        StringBuilder builder = new StringBuilder("curl -v ");

        // Method
        builder.append("-X ").append(connection.getRequestMethod()).append(" \\\n  ");

        // Headers
        for (Map.Entry<String, List<String>> entry : connection.getRequestProperties().entrySet()) {
            builder.append("-H \"").append(entry.getKey()).append(":");
            for (String value : entry.getValue())
                builder.append(" ").append(value);
            builder.append("\" \\\n  ");
        }

        // Body
        if (body != null)
            builder.append("-d '").append(new String(body)).append("' \\\n  ");

        // URL
        builder.append("\"").append(connection.getURL()).append("\"");

        return builder.toString();
    }

}
