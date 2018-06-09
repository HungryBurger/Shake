package org.androidtown.shaketest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ReadNFC extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private TextView textView;
    private PendingIntent pIntent;
    private IntentFilter[] filters;
    Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        getNFCData(getIntent());
        Log.d("tag", "onCreate: readfirst");
        //  textView = findViewById(R.id.text);
//        intent = new Intent(getApplicationContext(), ReadNFC.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                intent, 0);
//
//        // set an intent filter for all MIME data
//        IntentFilter ndefIntent = new IntentFilter(
//                NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            ndefIntent.addDataType("*/*");
//            filters = new IntentFilter[]{ndefIntent};
//        } catch (Exception e) {
//            Log.e("TagDispatch", e.toString());
//        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        nfcAdapter.enableForegroundDispatch(this, pIntent, filters, null);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        nfcAdapter.disableForegroundDispatch(this);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//            Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            if (data != null) {
//                NdefMessage[] messages = new NdefMessage[data.length];
//                for (int i = 0; i < data.length; ++i) {
//                    messages[i] = (NdefMessage) data[i];
//                }
//                byte[] payload = messages[0].getRecords()[0].getPayload();
//                textView.append("\n" + new String(payload));
//            }
//        }
//        Log.d("onNewIntent", "onNewIntent");
//        setIntent(intent);
//
//        return;
//    }


    private void getNFCData(Intent intent) {
        Log.d("tag", "ReadNFCgetNFCData: ");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (data != null) {
                NdefMessage[] messages = new NdefMessage[data.length];
                for (int i = 0; i < data.length; ++i) {
                    messages[i] = (NdefMessage) data[i];
                }
                byte[] payload = messages[0].getRecords()[0].getPayload();
                textView.append("\n" + new String(payload));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag", "onResume: read");

        Intent i = new Intent(this, ReadNFC.class);
        Log.d("tag", "onResume: intent");
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(this, 0, i, 0);

        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            //filter.addDataType("");
            Log.d("tag", "onResume: filter");
        } catch (Exception e) {
            Log.d("tag", "onResume:error ");
        }
        Log.d("tag", "onResume: check");
        filters = new IntentFilter[]{filter,};
        nfcAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("tag", "onNewIntent: ");
        setIntent(intent);
        getNFCData(getIntent());
    }
}


