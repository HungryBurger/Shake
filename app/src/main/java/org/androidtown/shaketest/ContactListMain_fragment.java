package org.androidtown.shaketest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.androidtown.shaketest.ServiceApplication.myContactList;
import static org.androidtown.shaketest.ServiceApplication.person;

public class ContactListMain_fragment extends Fragment {
    private MyAdapter myAdapter;
    RecyclerView recyclerView;
    static TextView name, pnum, email;
    List<MyAdapter.ContactInformation> productList = new ArrayList<>();

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

        name = mView.findViewById(R.id.user_name);
        pnum = mView.findViewById(R.id.user_phone);
        email = mView.findViewById(R.id.user_email);
        recyclerView = (RecyclerView) mView.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        setInitialData();
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
        recyclerView.setAdapter(myAdapter);
        return mView;
    }

    private void setInitialData() {
        HashMap<String, ContactData> list = ServiceApplication.person;
        if (list == null) return;

        Iterator<String> iterator = list.keySet().iterator();
        while (iterator.hasNext()) {
            String cur = iterator.next();
            productList.add(
                    new MyAdapter.ContactInformation(
                            cur,
                            list.get(cur)
                    )
            );
        }
    }
}