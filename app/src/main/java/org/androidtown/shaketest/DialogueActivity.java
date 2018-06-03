package org.androidtown.shaketest;

import android.app.Activity;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogueActivity extends AppCompatActivity {
    TextView get_name, get_pNum, get_email;
    CircleImageView mPicture,convertQRButton;
    private String userName, userPhoneNum, userEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Activity activity;

    /* Instance Variable for SaveContact method */
    private ArrayList<ContentProviderOperation> operationLists;
    private String DisplayName;
    private String MobileNumber;
    private String emailAddress;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shake);
        init();

        convertQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
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

        mDatabase = FirebaseDatabase.getInstance().getReference().
                child("users").child(mUser.getUid()).child("contact_list");

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                if (result.getContents().startsWith("shake")) {
                    Log.d("RESULTRESULT", result.getContents());
                    String[] arr = result.getContents().split("#");
                    //
                    saveContacts(arr[1], arr[2], arr[3]);
                    // 파이어베이스 들어갈 정보 객체 생성
//                    ContactData current_data =  new ContactData(
//                            arr[1], //이름
//                            arr[2], //번호
//                            arr[3], //이메일
//                            Integer.parseInt(arr[4]) //템플릿 넘버
//                    );
                    // 실제 파이어베이스에 저장 arr[5] 유저의 Uid 값
                    //mDatabase.child(arr[5]).setValue(current_data);

                } else {
                    /* Wrong Value */
                }
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

        SharedPrefManager mSharedPrefs = SharedPrefManager.getInstance(this);
        //setContentCard(mSharedPrefs.getUI_ItemNo());

        convertQRButton = findViewById(R.id.convertQR);
        mPicture = findViewById(R.id.user_picture);
        get_name = findViewById(R.id.user_name);
        get_pNum = findViewById(R.id.user_phone_num);
        get_email = findViewById(R.id.user_email);

        get_name.setText(userName);
        get_pNum.setText(userPhoneNum);
        get_email.setText(userEmail);
    }

    /**
     * Get user's phonenumber
     * @return
     */
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

    /**
     * Start Scanning
     */
    private void startScanning () {

        new IntentIntegrator(activity).
                setBeepEnabled(false).
                setOrientationLocked(false).
                setCaptureActivity(CustomScannerActivity.class).
                initiateScan();
    }

    /**
     * Receive the data from NFC or QR code
     * and then Insert received data into the phone's contact DB
     * @param name
     * @param num
     * @param email
     */
    public void saveContacts(String name, String num, String email) {
        DisplayName = name;
        MobileNumber = num;
        emailAddress = email;

        operationLists = new ArrayList<>();
        operationLists.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (DisplayName != null) {
            operationLists.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName
                    ).build());
        }
        if (MobileNumber != null) {
            operationLists.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        if (emailAddress != null) {
            operationLists.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, emailAddress)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationLists);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("AppLifeCycle", "DialogueActivity");
    }
}
