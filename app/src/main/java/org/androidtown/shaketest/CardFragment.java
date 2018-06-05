package org.androidtown.shaketest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardFragment extends Fragment {
    private static final String BUNDLE_KEY_TEMPLATE = "bundle_key_template";
    private int mTemplate;
    private ViewGroup view;
    private String userName, userPhoneNum, userEmail;
    CircleImageView mPicture,convertQRButton;
    MainMenu activity;
    userData userdata;

    public static CardFragment newInstance (int template) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();

        args.putInt(
                BUNDLE_KEY_TEMPLATE,
                template
        );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle args = getArguments();
        mTemplate = args.getInt(BUNDLE_KEY_TEMPLATE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainMenu) getActivity();
        userdata = new userData(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup)inflater.inflate(R.layout.card1, container, false);

        switch (mTemplate) {
            case 2: {
                view = (ViewGroup)inflater.inflate(R.layout.card2, container, false);
                break;
            }
            case 3: {
                view = (ViewGroup)inflater.inflate(R.layout.card3, container, false);
                break;
            }
            case 4: {
                view = (ViewGroup)inflater.inflate(R.layout.card4, container, false);
                break;
            }
            case 5:{
                view = (ViewGroup)inflater.inflate(R.layout.card5, container, false);
                break;
            }
            case 6:{
                view = (ViewGroup)inflater.inflate(R.layout.card6, container, false);
                break;
            }
            default:
                break;
        } setCardContent();
        return view;
    }

    private void setCardContent () {
        userdata.getPhonenum();
        TextView name = view.findViewById(R.id.card_name);
        TextView phone = view.findViewById(R.id.card_phoneNumber);
        TextView email = view.findViewById(R.id.card_email);
        mPicture =  view.findViewById(R.id.user_picture1);

        convertQRButton = view.findViewById(R.id.convertQR);
        convertQRButton.setVisibility(View.INVISIBLE);
        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdata.imageDialog();
            }
        });
        getinfo();
        changeImage();
        name.setText(userName);
        phone.setText(userPhoneNum);
        email.setText(userEmail);
        email.setSelected(true);
        mPicture.setImageBitmap(userdata.imageBitmap);
    }
    private void changeImage(){
        if(userdata.databaseReference != null) {
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
    private void getinfo() {
        userdata.mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(userdata.mUser != null) {
            userName = userdata.mUser.getDisplayName();
            userEmail = userdata.mUser.getEmail();
            userdata.databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userdata.mUser.getUid()).child("userImg");
            userPhoneNum = userdata.displayUserPhoneNumber;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeImage();
    }
}
