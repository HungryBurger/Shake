package org.androidtown.shaketest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogueActivity extends Activity {
    TextView get_name, get_pNum, get_email;
    CircleImageView mPicture,convertQRButton;
    private String userName, userPhoneNum, userEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogue);
        convertQRButton = findViewById(R.id.convertQR);
        mPicture = findViewById(R.id.dialogue_activity_picture);
        get_name = findViewById(R.id.dialogue_activity_name);
        get_pNum = findViewById(R.id.dialogue_activity_pNum);
        get_email = findViewById(R.id.dialogue_activity_email);
        /* 사용자 정보 얻기 */
        init();

        get_name.setText(userName);
        get_pNum.setText(userPhoneNum);
        get_email.setText(userEmail);
        Log.d("AtDialogueActivity", userName + " " + userEmail + " " + userPhoneNum);

        convertQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();
                Toast.makeText(DialogueActivity.this, "QR is Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Interpreting data from QR code
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {

                Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Get the current user's information by using FirebaseAuth
     */
    private void init () {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        userName = mUser.getDisplayName();
        userEmail = mUser.getEmail();
        userPhoneNum = getPhoneNum();
    }

    private String getPhoneNum() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String phoneNum = telephonyManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            } return PhoneNumberUtils.formatNumber(phoneNum);

        } catch (SecurityException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } return null;
    }
}
