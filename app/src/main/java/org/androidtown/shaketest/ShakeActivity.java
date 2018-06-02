package org.androidtown.shaketest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ShakeActivity extends AppCompatActivity {

    private String userName, userPhoneNum, userEmail;
    private int userTemplate;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    /* Instance Variable for SaveContact method */
    private ArrayList<ContentProviderOperation> operationLists;
    private String DisplayName;
    private String MobileNumber;
    private String emailAddress;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        init();

        DialogFragment fragment = DialogFragment.newInstance(10, 5, false, false,2);
        fragment.show(getFragmentManager(), "blur_sample");
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
        userTemplate = mSharedPrefs.getUI_ItemNo();
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
                    ContactData current_data =  new ContactData(
                            arr[1], //이름
                            arr[2], //번호
                            arr[3], //이메일
                            Integer.parseInt(arr[4]) //템플릿 넘버
                    );
                    // 실제 파이어베이스에 저장 arr[5] 유저의 Uid 값
                    mDatabase.child(arr[5]).setValue(current_data);

                } else {
                    /* Wrong Value */
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

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
