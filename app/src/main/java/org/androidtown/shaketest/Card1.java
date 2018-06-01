package org.androidtown.shaketest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.ViewfinderView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Card1 extends Fragment {
    private FragmentManager fragmentManager;
    String displayUserPhoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userName, userPhoneNum, userEmail;
    private Object mMyData;
    private FirebaseAuth.AuthStateListener mListener;
    private CircleImageView circleImageView;
    private FirebaseStorage firebaseStorage;
    private StorageReference mountainRef;
    StorageReference storageReference = null;
    FirebaseStorage storageRef = null;

    public static Card1 newInstance() {
        Bundle args = new Bundle();
        Card1 fragment = new Card1();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.card1, container, false);
        final MainMenu activity = (MainMenu) getActivity();
        getPhonenum();
        getinfo();
        Toast.makeText(getActivity(), "" + userName + userPhoneNum + userEmail, Toast.LENGTH_SHORT).show();
        circleImageView = mView.findViewById(R.id.user_picture);
        TextView name = (TextView) mView.findViewById(R.id.card1_name);
        TextView phone = (TextView) mView.findViewById(R.id.card1_phone_num);
        TextView email = (TextView) mView.findViewById(R.id.card1_email);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        name.setText(userName);
        phone.setText(userPhoneNum);
        email.setText(userEmail);
        return mView;
    }

    private void getPhonenum() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String phoneNum = telephonyManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
                userPhoneNum = phoneNum;
            }
            displayUserPhoneNumber = PhoneNumberUtils.formatNumber(phoneNum);
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void getinfo() {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        userName = mUser.getDisplayName();
        userEmail = mUser.getEmail();
    }



}