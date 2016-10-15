package com.aama.melon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends FragmentActivity {

    private TwitterLoginButton loginButton;
    private boolean twitter_logged_in;
    public static final String LOGIN_PREFS = "LoginPrefs";
    public static final String TWITTER = "TwitterLoggedIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        twitterLogin();
        SharedPreferences sharedPrefLogin = getSharedPreferences(LOGIN_PREFS,
                Context.MODE_PRIVATE);
        boolean twitter_logged_in = sharedPrefLogin.getBoolean(TWITTER, false);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        if (twitter_logged_in) {
            twitterLogin();
        } else {
            loginButton.setVisibility(View.GONE);
        }

    }

    private void twitterLogin() {
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                SharedPreferences settings = getSharedPreferences(LOGIN_PREFS, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(TWITTER, true);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    updateUI() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
