package org.androidtown.shaketest;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

public class ShakeActivity extends AppCompatActivity {
    /* Variables for Nfc communication */
    private String userName, userPhoneNum, userEmail;
    private int userTemplate;
    userData userdata;
    /* Instance Variable for SaveContact method */
    private ArrayList<ContentProviderOperation> operationLists;
    private String DisplayName;
    private String MobileNumber;
    private String emailAddress;
    SharedPrefManager mSharedPrefs = SharedPrefManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        userdata = new userData(ShakeActivity.this);
        mSharedPrefs = SharedPrefManager.getInstance(this);
        init();

        DialogFragment fragment = DialogFragment.newInstance(10, 5, false, false,mSharedPrefs.getUI_ItemNo());
        fragment.show(getFragmentManager(), "blur_sample");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    /**
     * Get the current user's information by using FirebaseAuth
     */
    private void init () {
        userdata.mAuth = FirebaseAuth.getInstance();
        userdata.mUser = userdata.mAuth.getCurrentUser();
        userName = userdata.mUser.getDisplayName();
        userEmail = userdata.mUser.getEmail();
        userPhoneNum = userdata.displayUserPhoneNumber;

        SharedPrefManager mSharedPrefs = SharedPrefManager.getInstance(this);
        userTemplate = mSharedPrefs.getUI_ItemNo();
    }
    /**
     * Interpreting data from QR code
     * Save Received Data into USER's Contact
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {
                if (result.getContents().startsWith("shake")) {
                    String[] arr = result.getContents().split("#");

                    final String uid = arr[1];
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference contactListRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("contact_list");

                    /*
                    * Get Target user's data via QR Scanner
                    * Save received data into Firebase DB and User Contact list
                    */
                    contactListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> contactList = null;
                            if (dataSnapshot.exists()) {
                                contactList = (ArrayList<String>) dataSnapshot.getValue();
                            } else {
                                contactList = new ArrayList<>();
                            }
                            contactList.add(uid);
                            contactListRef.setValue(contactList);

                            DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("myInfo");
                            newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ContactData data = dataSnapshot.getValue(ContactData.class);
                                    if (data != null) {
                                        saveContacts(
                                                data.getName(),
                                                data.getPhoneNum(),
                                                data.getEmail()
                                        );
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "잘 못된 형식의 데이터 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Save 상대방 연락처 into user's device contact list
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

        // 상대방 이름 저장
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
        // 휴대폰 번호 저장
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
        // 이메일 저장
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

}