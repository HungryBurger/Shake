package org.androidtown.shaketest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardFragment extends Fragment {
    private static final String BUNDLE_KEY_TEMPLATE = "bundle_key_template";
    private int mTemplate;
    private ViewGroup view;
    private SharedPrefManager mSharedPrefManager;
    CircleImageView mPicture,convertQRButton;


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

        mSharedPrefManager = SharedPrefManager.getInstance(getContext());
        Log.d("TAG", "onCreate: card");
        //activity = (MainMenu) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup)inflater.inflate(R.layout.card1, container, false);
        Log.d("TAG", "onCreateView: card");
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

        TextView name = view.findViewById(R.id.card_name);
        TextView phone = view.findViewById(R.id.card_phoneNumber);
        TextView email = view.findViewById(R.id.card_email);
        mPicture =  view.findViewById(R.id.user_picture1);
        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu)getActivity()).editDialog();
            }
        });

        convertQRButton = view.findViewById(R.id.convertQR);
        convertQRButton.setVisibility(View.INVISIBLE);

//        changeImage();
        name.setText(mSharedPrefManager.getUserName());
        phone.setText(mSharedPrefManager.getUserPhonenum());
        email.setText(mSharedPrefManager.getUserEmail());
        email.setSelected(true);
        if (mSharedPrefManager.getUserImage() != null)
            mPicture.setImageBitmap(mSharedPrefManager.getUserImage());
        else
            mPicture.setImageResource(R.drawable.user_profile);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: card");

        if (mSharedPrefManager.getUserImage() != null)
            mPicture.setImageBitmap(mSharedPrefManager.getUserImage());
        else
            mPicture.setImageResource(R.drawable.user_profile);
    }
}
