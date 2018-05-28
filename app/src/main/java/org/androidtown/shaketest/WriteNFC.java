package org.androidtown.shaketest;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.androidtown.shaketest.R;

import java.nio.charset.Charset;
import java.util.Locale;

public class WriteNFC extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private NdefMessage mNdeMessage;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writenfc);
        Intent intent = getIntent();
        Bundle myBundle = intent.getExtras();
        String mName = myBundle.getString("name");
        String mPhoneNum = myBundle.getString("phoneNum");
        String mMail = myBundle.getString("E-mail");
        text = findViewById(R.id.text);
        nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if(nfcAdapter != null)
            text.setText("Contact please." + nfcAdapter + "");
        else
            text.setText("Turn on please" + nfcAdapter + "");
        mNdeMessage = new NdefMessage(new NdefRecord[]{
                createNewTextRecord("이름 : " + mName, Locale.KOREAN, true),
                createNewTextRecord("전화번호 : " + mPhoneNum, Locale.KOREAN, true),
                createNewTextRecord("E-mail : " + mMail, Locale.ENGLISH, true),
        });
    }

    public static NdefRecord createNewTextRecord(String text, Locale locale, boolean encodelnUtf8){
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodelnUtf8 ? Charset.forName("UTF-8"):Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = encodelnUtf8 ? 0:(1<<7);
        char status = (char)(utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];

        data[0] = (byte)status;

        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter != null)
            nfcAdapter.enableForegroundNdefPush(this, mNdeMessage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundNdefPush(this);
        }
    }
}