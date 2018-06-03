package org.androidtown.shaketest;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ContactListMain_fragment extends Fragment {

    private MyAdapter myAdapter;
    RecyclerView recyclerView;
    static TextView name, pnum, email;
    List<MyAdapter.ContactInformation> productList = new ArrayList<>();
    ArrayList<ContactData> contactDataList;

    public static ContactListMain_fragment newInstance() {
        Bundle args = new Bundle();

        ContactListMain_fragment fragment = new ContactListMain_fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_contact_list_main_fragment, container, false);
        setInitialData();
        name = mView.findViewById(R.id.user_name);
        pnum = mView.findViewById(R.id.user_phone_num);
        email = mView.findViewById(R.id.user_email);

        recyclerView = (RecyclerView) mView.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        myAdapter = new MyAdapter(getActivity(), productList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);

                MyAdapter.ContactInformation information = productList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                builder.setView(layoutInflater.inflate(R.layout.fragment_dialog_receiver, null));

                builder.create().show();
            }
        });
//        myAdapter = new MyAdapter(getActivity(), productList, new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(getActivity(), "롱클릭", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        recyclerView.setAdapter(myAdapter);

        return mView;
    }

    private void setInitialData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference contactListRef = FirebaseDatabase.getInstance().getReference().child("users");
        contactDataList = new ArrayList<>();

        contactListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArrayList<String> myList = ((ServiceApplication)getActivity().getApplication()).myContactList;
                Iterator<String> iter = myList.iterator();

                Log.d("CONTACT_LIST_MAIN", dataSnapshot.getKey());
                Log.d("CONTACT_LIST_MAIN", myList.size() + " 개수");

                while (iter.hasNext()) {
                    String cur = iter.next();
                    if (cur.equals(dataSnapshot.getKey())) {
                        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(cur).child("myInfo");

                        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ContactData contactData = dataSnapshot.getValue(ContactData.class);

                                contactDataList.add(contactData);
                                productList.add(
                                        new MyAdapter.ContactInformation (
                                            contactData.getName(),contactData.getPhoneNum(), contactData.getEmail(), R.mipmap.ic_launcher
                                        )
                                );
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
//
//                contactData = dataSnapshot.getValue(ContactData.class);
//                productList.add(new MyAdapter.ContactInformation(contactData.getName(),contactData.getPhoneNum(),R.mipmap.ic_launcher));
//                myAdapter.notifyDataSetChanged();
//                Log.d("FireDB", "Value " + contactData.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}