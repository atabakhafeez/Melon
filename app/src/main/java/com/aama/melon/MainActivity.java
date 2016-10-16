package com.aama.melon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends FragmentActivity {

    private TwitterLoginButton loginButton;
    private boolean twitter_logged_in;
    public static final String LOGIN_PREFS = "LoginPrefs";
    public static final String TWITTER = "TwitterLoggedIn";
    public static final String TWITTER_KEY = "YvuU7TFgDRfLXgqixW3RDTYht";
    public static final String TWITTER_SECRET = "ExODI9VwYzSQ4scaIztjZRqGXM9goyOpISW48L7T0VxIlChdLr";

    private String username;
    private Long id;
    TwitterSession twitterSession = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
//        SharedPreferences sharedPrefLogin = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
//        twitter_logged_in = false;
//        twitter_logged_in = sharedPrefLogin.getBoolean(TWITTER, false);
        twitterSession = null;

        if (twitterSession == null) {
            twitterLogin();
        } else {
            loginButton.setVisibility(View.GONE);
            ToggleButton twitter_switch = (ToggleButton) findViewById(R.id.twitterToggle);
            twitter_switch.setVisibility(View.VISIBLE);
        }

    }

    private void twitterLogin() {
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
                username = twitterSession.getUserName();
                id = twitterSession.getUserId();
                String msg = "@" + twitterSession.getUserName() + " logged in! (#" +
                        twitterSession.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                loginButton.setVisibility(View.GONE);
                ToggleButton twitter_switch = (ToggleButton) findViewById(R.id.twitterToggle);
                twitter_switch.setVisibility(View.VISIBLE);
//                SharedPreferences settings = getSharedPreferences(LOGIN_PREFS, 0);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putBoolean(TWITTER, true);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
