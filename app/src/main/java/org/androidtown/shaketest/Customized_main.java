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
    ArrayList<CustomizedAdapter.Item> list;
    String displayUserPhoneNumber;
    private SharedPrefManager mSharedPrefs;
    int itemPosition;

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
        list = new ArrayList<CustomizedAdapter.Item>();
        setInitialData();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.list);
        mSharedPrefs = SharedPrefManager.getInstance(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CustomizedAdapter(list, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return mView;
    }

    private void setInitialData() {
        list.add(new CustomizedAdapter.Item("Style1", "", R.drawable.card1));
        list.add(new CustomizedAdapter.Item("Style2", "", R.drawable.card2));
        list.add(new CustomizedAdapter.Item("Style3", "", R.drawable.card3));
        list.add(new CustomizedAdapter.Item("Style4", "", R.drawable.card4));
        list.add(new CustomizedAdapter.Item("Style5", "", R.drawable.card5));
        list.add(new CustomizedAdapter.Item("Style6", "", R.drawable.card6));
    }
    public void onClick(final View view) {
        itemPosition = mRecyclerView.getChildPosition(view);
        Integer item = list.size();
        Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
    }
}
