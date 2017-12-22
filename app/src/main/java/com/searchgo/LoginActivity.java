package com.searchgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.searchgo.backgroundServices.BackgroundServiceScheduler;
import com.searchgo.tasks.TokenRetrievalTask;
import com.searchgo.utils.LoginUtils;

public class LoginActivity extends AppCompatActivity implements ITokenRetrieveEnd {

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isLoggedIn()) {
            startMainFlow();
        } else {
            doFacebookLogin();
        }



    }

    private void doFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new TokenRetrievalTask(LoginActivity.this, LoginActivity.this).execute(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("errFaceLogin", "error on facebook login", exception);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void tokenRetrieveEnd(String response) {
        startMainFlow();

    }

    private void startMainFlow() {
        //TODO - Get current user by user ID in shared preferences before openning home page
        String accessToken = LoginUtils.getAccessToken(this);
        LoginUtils.initAccessTokenForRestCalls(accessToken, this);
        Intent intent = new Intent(this, HomePageActivity.class);
        this.startActivity(intent);
        BackgroundServiceScheduler.scheduleGetMyEventsService(this);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
