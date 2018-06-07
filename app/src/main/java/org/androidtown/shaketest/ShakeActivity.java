package org.androidtown.shaketest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;

public class ShakeActivity extends AppCompatActivity {
    /* Variables for Nfc communication */
    private String userName, userPhoneNum, userEmail;
    private int userTemplate;
    userData userdata;
    SharedPrefManager mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        userdata = new userData(ShakeActivity.this);
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());
        init();

        ((ServiceApplication)getApplication()).isShaking = true;
        Log.d("isShaking", ((ServiceApplication)getApplication()).isShaking + "");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((ServiceApplication)getApplication()).isShaking = false;
        Log.d("isShaking", ((ServiceApplication)getApplication()).isShaking + "");
    }
}