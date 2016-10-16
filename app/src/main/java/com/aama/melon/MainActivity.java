package com.aama.melon;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    Beamer beamer;
    NfcAdapter nfcAdapter;
    TextView textView;

    User current;

    public void SendString(String s)
    {
        Log.d("Send: ", "Attempting to Send ");

        NdefMessage msg = beamer.createMessage(s);
        nfcAdapter.setNdefPushMessage(msg,this);
    }

    public void ButtonNfc(View v)
    {
        String text = ((EditText) findViewById(R.id.editText)) . getText() .toString();

        SendString(text);
    }

    public void SendUser(View v)
    {
        String text = current.toJSON();

        SendString(text);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present

        String text = new String(msg.getRecords()[0].getPayload());

        ProcessReceived(text);
    }

    public void ProcessReceived(String s)
    {
        User r = current.fromJSON(s);
        Toast.makeText(this,r.getFb(),Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.Text1);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        beamer = new Beamer();
        beamer.parent = this;
        nfcAdapter.setOnNdefPushCompleteCallback(beamer,this);

        current = new User();
        current.setFb("tester");
    }
}