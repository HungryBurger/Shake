package org.androidtown.shaketest;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NFCActivity extends AppCompatActivity {
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    TextView tagDesc;
    Button r,w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        tagDesc = findViewById(R.id.tagNFC);
        nfcAdapter = NfcAdapter.getDefaultAdapter(NFCActivity.this);
        Intent intent = new Intent(NFCActivity.this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(NFCActivity.this,0,intent,0);
        r = findViewById(R.id.read);
        w = findViewById(R.id.write);

        /*
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ReadActivity.class);
                startActivity(intent);
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),WriteActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    protected void onPause() {
        if(nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(NFCActivity.this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(NFCActivity.this,pendingIntent,null,null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag != null){
            byte[] tagId = tag.getId();
            tagDesc.setText("TagID : " + toHexString(tagId));
        }
    }
    public static final String CHARS = "0123456789ABCDE";

    public static String toHexString(byte[] data){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<data.length;++i){
            stringBuilder.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(CHARS.charAt(data[i] & 0x0F));
        }
        return stringBuilder.toString();
    }
}
