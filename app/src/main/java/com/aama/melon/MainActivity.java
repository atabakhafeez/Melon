package com.aama.melon;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.facebook.login.LoginManager;
import com.facebook.FacebookCallback;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends FragmentActivity {

    Beamer beamer;
    NfcAdapter nfcAdapter;
    TextView textView;

    private TwitterLoginButton loginButton;
    private boolean twitter_logged_in;
    public static final String LOGIN_PREFS = "LoginPrefs";
    public static final String TWITTER = "TwitterLoggedIn";
    public static final String TWITTER_KEY = "YvuU7TFgDRfLXgqixW3RDTYht";
    public static final String TWITTER_SECRET = "ExODI9VwYzSQ4scaIztjZRqGXM9goyOpISW48L7T0VxIlChdLr";

    public static CallbackManager callbackManager = null;
    public static AccessToken accessToken = null;
    public static final String FB_LOG_TAG = "Facebook Login";

    private String username;
    private Long id;
    TwitterSession twitterSession = null;

    public void SendNfc(View v)
    {
        String text = ((EditText) findViewById(R.id.editText)) . getText() .toString();
        ((TextView) findViewById(R.id.Text1)) . setText("Hello World!"); // TODO
        NdefMessage msg = beamer.createMessage(text);
        nfcAdapter.setNdefPushMessage(msg,this);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        textView.setText(new String(msg.getRecords()[0].getPayload()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        processIntent(intent);
    }

    public void cancel()
    {
        nfcAdapter.setNdefPushMessage(null,this,this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken();
                        Log.d(FB_LOG_TAG, "Successful sigin");
                    }

                    @Override
                    public void onCancel() {
                        Log.d(FB_LOG_TAG, "Logout");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(FB_LOG_TAG, "Sign-in error");
                    }
                });

        // twitter auth
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));

        // NFC
        textView = (TextView) findViewById(R.id.Text1);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        beamer = new Beamer();
        beamer.parent = this;
        nfcAdapter.setOnNdefPushCompleteCallback(beamer,this);
    }
}