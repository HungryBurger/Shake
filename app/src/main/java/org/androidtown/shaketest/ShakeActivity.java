package org.androidtown.shaketest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;

public class ShakeActivity extends AppCompatActivity {
    /* Variables for Nfc communication */
    SharedPrefManager mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());

        ((ServiceApplication)getApplication()).isShaking = true;
        Log.d("isShaking", ((ServiceApplication)getApplication()).isShaking + "");

        DialogFragment fragment = DialogFragment.newInstance(10, 5, false, false, mSharedPrefs.getUserTemplateNo(), null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((ServiceApplication)getApplication()).isShaking = false;
        Log.d("isShaking", ((ServiceApplication)getApplication()).isShaking + "");
    }
}