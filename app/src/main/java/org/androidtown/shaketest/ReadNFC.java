package org.androidtown.shaketest;

import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadNFC extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pIntent;
    private IntentFilter[] filters;
    private SharedPrefManager mSharedPrefManager;
    Intent intent;
    String templateNum,name,Email,phoneNum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());

        setContentView(R.layout.activity_read);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Log.d("getNFC 순서 확인", "앙 기모띠1");
        if (getNFCData(getIntent())) {
            finish();
        }
        Log.d("tag", "onCreate: readfirst");
    }

    private boolean getNFCData(Intent intent) {
        Log.d("tag", "ReadNFCgetNFCData: ");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (data != null) {
                NdefMessage[] messages = new NdefMessage[data.length];
                for (int i = 0; i < data.length; ++i) {
                    messages[i] = (NdefMessage) data[i];
                }
                byte[] payload = messages[0].getRecords()[0].getPayload();

                final String opponent = new String(payload);
                if (opponent == null) return true;

                DatabaseReference contactListRef = FirebaseDatabase.getInstance().getReference().child("contact_list").child(mSharedPrefManager.getUserUid());
                if (((ServiceApplication) getApplication()).myContactList == null) {
                    ((ServiceApplication) getApplication()).myContactList = new ArrayList<>();
                    ((ServiceApplication) getApplication()).person = new HashMap<>();
                }
                if (((ServiceApplication) getApplication()).myContactList.contains(opponent)) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 사람 입니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                ((ServiceApplication) getApplication()).myContactList.add(opponent);
                contactListRef.setValue(((ServiceApplication) getApplication()).myContactList);

                DatabaseReference infoRef = FirebaseDatabase.getInstance().getReference().child("myInfo").child(opponent);
                infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ContactData data = dataSnapshot.getValue(ContactData.class);
                            if (data != null) {
                                saveContacts(
                                        data.getName(),
                                        data.getPhoneNum(),
                                        data.getEmail()
                                );
                                ((ServiceApplication) getApplication()).person.put(opponent, data);
                                DialogFragment fragment = DialogFragment.newInstance(
                                        10, 5, false, false, data.getTemplate_no(), data, 3
                                );
                                fragment.show(getFragmentManager(), "blur_sample");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } return false;
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
        if(getNFCData(getIntent())) finish();
    }

    public void saveContacts(String name, String phonNum, String email) {
        ArrayList<ContentProviderOperation> operationLists;

        operationLists = new ArrayList<>();
        operationLists.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // 상대방 이름 저장
        if (name != null) {
            operationLists.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name
                    ).build());
        }
        // 휴대폰 번호 저장
        if (phonNum != null) {
            operationLists.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phonNum)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        // 이메일 저장
        if (email != null) {
            operationLists.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationLists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


