package org.androidtown.shaketest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CardFragment extends Fragment {
    private static final String BUNDLE_KEY_TEMPLATE = "bundle_key_template";
    private int mTemplate;
    private ViewGroup view;
    private FirebaseUser mUser;
    private String displayUserPhoneNumber;
    private String userName, userPhoneNum, userEmail;

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
            }
            case 4: {
                view = (ViewGroup)inflater.inflate(R.layout.card4, container, false);
            }
            case 5:{
                view = (ViewGroup)inflater.inflate(R.layout.card5, container, false);
            }
            case 6:{
                view = (ViewGroup)inflater.inflate(R.layout.card6, container, false);
            }

            default:
                break;
        } setCardContent();
        return view;
    }

    private void setCardContent () {

        getPhonenum();
        getinfo();

        TextView name = view.findViewById(R.id.card_name);
        TextView phone = view.findViewById(R.id.card_phoneNumber);
        TextView email = view.findViewById(R.id.card_email);

        name.setText(userName);
        phone.setText(userPhoneNum);
        email.setText(userEmail);
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
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null) {
            userName = mUser.getDisplayName();
            userEmail = mUser.getEmail();
        }
    }
}
