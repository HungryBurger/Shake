package org.androidtown.shaketest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.nio.charset.Charset;

public class ShakeActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback,NfcAdapter.OnNdefPushCompleteCallback {
    /* Variables for Nfc communication */
    private static final int MESSAGE_SENT = 1;
    SharedPrefManager mSharedPrefs;
    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());

        ((ServiceApplication)getApplication()).isShaking = true;
        Log.d("isShaking", ((ServiceApplication)getApplication()).isShaking + "");

        DialogFragment fragment = DialogFragment.newInstance(10, 5, false, false, mSharedPrefs.getUserTemplateNo(), null);
        fragment.show(getFragmentManager(), "blur_sample");

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            Toast.makeText(getApplicationContext(), "NFC 단말기를 접촉해주세요" + mNfcAdapter + "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "NFC 기능이 꺼져있습니다. 켜주세요" + mNfcAdapter + "", Toast.LENGTH_SHORT).show();
        }
        NFCWrite();
        mNfcAdapter.setNdefPushMessageCallback(this,this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this,this);
    }
    public NdefRecord createMimeRecord(String mimeType, byte[] payload){
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes,new byte[0],payload);

        return mimeRecord;
    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String ID = mSharedPrefs.getUserUid();
        NdefMessage msg = new NdefMessage(new NdefRecord[]{createMimeRecord("shake/nfc",ID.getBytes()),NdefRecord.createApplicationRecord("org.androidtown.shaketest")
        });
        return msg;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(), "NFC 태그에 데이터를 작성했습니다.", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    void NFCWrite() {
        if (CheckNFCEnabled()) {
            Log.d("tag", "NFCWrite: ");
            mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), getApplicationContext().getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            enableTagWriteMode();
        } else {
            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "NFC를 켜주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean CheckNFCEnabled() {
        Log.d("tag", "CheckNFCEnabled: write");
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdpt != null) {
            if (!nfcAdpt.isEnabled())
                return false;
        }
        return true;
    }

    private void enableTagWriteMode() {
        mWriteMode = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), ShakeService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(getApplicationContext(), ShakeService.class));
    }
}