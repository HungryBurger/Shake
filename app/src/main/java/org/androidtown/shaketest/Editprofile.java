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
    private SharedPrefManager mSharedPrefManager;
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
        mSharedPrefManager = SharedPrefManager.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_editprofile, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        Button Customize = (Button)mView.findViewById(R.id.edit_customize);

        TextView name = (TextView) mView.findViewById(R.id.edit_name);
        TextView phone = (TextView) mView.findViewById(R.id.edit_phone);
        TextView email = (TextView) mView.findViewById(R.id.edit_email);
        mPicture = mView.findViewById(R.id.user_picture);
        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu)getActivity()).imageDialog();
                Log.d("이미지바꾸기", "버튼 클릭 ");
                changeUserImage();
            }
        });

        name.setText(mSharedPrefManager.getUserName());
        phone.setText(mSharedPrefManager.getUserPhonenum());
        email.setText(mSharedPrefManager.getUserEmail());
        Customize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentManager.beginTransaction().replace(R.id.frameLayout, Customized_main.newInstance()).commit();
                Toast.makeText(getActivity(), "커스터마이징 수정하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }

    private void changeUserImage () {

        Log.d("이미지바꾸기", "바꾸기 메소드");
        if (mSharedPrefManager.getUserImage() != null) {
            mPicture.setImageBitmap(mSharedPrefManager.getUserImage());
        } else {
            mPicture.setImageResource(R.drawable.user_profile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("이미지바꾸기", "온 리쥼 ");
        changeUserImage();
    }
}
