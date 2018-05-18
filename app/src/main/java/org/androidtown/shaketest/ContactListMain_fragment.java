package org.androidtown.shaketest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactListMain_fragment extends Fragment {
    List<ContactInformation> productList = new ArrayList<>();
    public static ContactListMain_fragment newInstance() {
        Bundle args = new Bundle();

        ContactListMain_fragment fragment = new ContactListMain_fragment();
        fragment.setArguments(args);

        return fragment;
    }

    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_contact_list_main_fragment, container, false);
        setInitialData();
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        MyAdapter myAdapter = new MyAdapter(getActivity(),productList);
        recyclerView.setAdapter(myAdapter);
        return mView;
    }
    private void setInitialData(){
        productList.add(new ContactInformation("text1","text1",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text2","text2",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
        productList.add(new ContactInformation("text3","text3",R.mipmap.ic_launcher));
    }
}