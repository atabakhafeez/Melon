package com.aama.melon;

import android.app.Activity;
import android.nfc.NfcAdapter;


/**
 * Created by mike on 10/15/16.
 */

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class Beamer extends Activity
        implements OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;
    TextView textView;
    Activity parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mNfcAdapter.setOnNdefPushCompleteCallback(this,this);
    }

    public NdefMessage createMessage(String text)
    {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "application/com.aama.melon", text.getBytes())
                        , NdefRecord.createApplicationRecord("com.aama.melon")
                });
        return msg;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event)
    {
        Log.e("Sending ","Sent !!!");
        mNfcAdapter.setNdefPushMessage(null,parent);
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

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        textView.setText(new String(msg.getRecords()[0].getPayload()));
    }

}