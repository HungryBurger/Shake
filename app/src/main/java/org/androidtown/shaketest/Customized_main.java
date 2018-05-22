package org.androidtown.shaketest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Customized_main extends Fragment {
    RecyclerView mRecyclerView;
    CustomizedAdapter mAdapter;
    ArrayList<Item> list;

    public static Customized_main newInstance() {
        Bundle args = new Bundle();
        Customized_main fragment = new Customized_main();
        fragment.setArguments(args);
        return fragment;
    }

    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_customized_main, container, false);
        list = new ArrayList<Item>();
        setInitialData();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CustomizedAdapter(list, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return mView;
    }
    private void setInitialData() {
        list.add(new Item("Manish Rajput", "manish@gmail.com", R.drawable.card1));
        list.add(new Item("Vinit yadav", "vinit@gmail.com", R.drawable.card2));
        list.add(new Item("Anoop", "anoop@gmail.com", R.drawable.card3));
        list.add(new Item("Dheeraj", "Dheeraj@gmail.com", R.drawable.card4));
    }

    /**  getPhonenum();
     Toast.makeText(getActivity(), displayUserPhoneNumber, Toast.LENGTH_SHORT).show();
     FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
     final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("USER").child(mUser.getUid()).child(displayUserPhoneNumber).child("customized_num/value");

     ImageButton btn1 = (ImageButton) mView.findViewById(R.id.imageButton1);
     btn1.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
    Toast.makeText(getActivity(), "Set Style1", Toast.LENGTH_SHORT).show();
    mRef.push().setValue("1");
    }
    });
     ImageButton btn2 = (ImageButton) mView.findViewById(R.id.imageButton2);
     btn2.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
    Toast.makeText(getActivity(), "Set Style2", Toast.LENGTH_SHORT).show();
    mRef.push().setValue("2");
    }
    });

     ImageButton btn3 = (ImageButton) mView.findViewById(R.id.imageButton3);
     btn3.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
    Toast.makeText(getActivity(), "Set Style3", Toast.LENGTH_SHORT).show();
    mRef.push().setValue("3");
    }
    });
     ImageButton btn4 = (ImageButton) mView.findViewById(R.id.imageButton4);
     btn4.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
    Toast.makeText(getActivity(), "Set Style4", Toast.LENGTH_SHORT).show();
    mRef.push().setValue("4");
    }
    });
     return mView;
     }
     private void getPhonenum() {
     TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

     try {
     String phoneNum = telephonyManager.getLine1Number();
     if (phoneNum.startsWith("+82")) {
     phoneNum = phoneNum.replace("+82", "0");
     }
     displayUserPhoneNumber = PhoneNumberUtils.formatNumber(phoneNum);
     } catch (SecurityException e) {
     Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
     }
     */
}
