package org.androidtown.shaketest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Editprofile extends Fragment {
    private FragmentManager fragmentManager;
    String displayUserPhoneNumber;
    CircleImageView mPicture;
    userData userdata;
    MainMenu activity;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userName, userPhoneNum, userEmail;
    public static Editprofile newInstance() {
        Bundle args = new Bundle();
        Editprofile fragment = new Editprofile();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainMenu)getActivity();
        userdata = new userData(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_editprofile, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        Button Customize = (Button)mView.findViewById(R.id.edit_customize);
        userdata.getPhonenum();
        getinfo();

        TextView name = (TextView) mView.findViewById(R.id.edit_name);
        TextView phone = (TextView) mView.findViewById(R.id.edit_phone);
        TextView email = (TextView) mView.findViewById(R.id.edit_email);
        mPicture = mView.findViewById(R.id.user_picture);

        name.setText(userName);
        phone.setText(userPhoneNum);
        email.setText(userEmail);
        Customize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentManager.beginTransaction().replace(R.id.frameLayout, Customized_main.newInstance()).commit();
                Toast.makeText(getActivity(), "커스터마이징 수정하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }

    private void getinfo() {
        userdata.mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (userdata.mUser != null) {
            userName = userdata.mUser.getDisplayName();
            userEmail = userdata.mUser.getEmail();
            userdata.databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userdata.mUser.getUid()).child("userImg");
            userPhoneNum = userdata.displayUserPhoneNumber;
        }
    }
    private void changeImage() {
        if (userdata.databaseReference != null) {
            userdata.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = (String) dataSnapshot.getValue();
                    if (value != null) {
                        mPicture.setImageBitmap(userdata.stringToBitmap(value));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        changeImage();
    }
}
