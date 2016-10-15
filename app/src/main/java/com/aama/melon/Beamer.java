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


public class Beamer
        implements OnNdefPushCompleteCallback {
    Activity parent;

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
        NfcAdapter.getDefaultAdapter(parent).setNdefPushMessage(null,parent,parent);
    }
}