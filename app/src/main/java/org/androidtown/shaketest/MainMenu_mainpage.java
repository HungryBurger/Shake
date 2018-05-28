package org.androidtown.shaketest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenu_mainpage extends Fragment {
    FragmentManager fragmentManager;

    public static MainMenu_mainpage newInstance() {
        Bundle args = new Bundle();
        MainMenu_mainpage fragment = new MainMenu_mainpage();

        fragment.setArguments(args);
        return fragment;
    }
    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_main_menu_mainpage, container, false);
        SharedPrefManager mSharedPrefs = SharedPrefManager.getInstance(getActivity());
        mSharedPrefs.setUI_ItemNo(1);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String userName = mUser.getDisplayName();
        String userEmail = mUser.getEmail();
        String phoneNumber = getPhoneNum();

            return mView;
    }
    private String getPhoneNum() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String phoneNum = telephonyManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            } return PhoneNumberUtils.formatNumber(phoneNum);

        } catch (SecurityException e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        } return null;
    }
}