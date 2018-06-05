package org.androidtown.shaketest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

public class DialogFragment extends BlurDialogFragment {

    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";
    public DatabaseReference databaseReference;
    /**
     * Bundle key used to start the blur dialog with a given blur radius (int).
     */
    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";

    /**
     * Bundle key used to start the blur dialog with a given dimming effect policy.
     */
    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";

    /**
     * Bundle key used to start the blur dialog with a given debug policy.
     */
    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";
    private static final String BUNDLE_KEY_TEMPLATE = "bundle_key_template";
    userData userdata;
    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private int mTemplate;
    private View view;
    private String userName, userPhoneNum, userEmail;
    ShakeActivity shakeActivity;
    CircleImageView mPicture, convertQRButton;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param radius          blur radius.
     * @param downScaleFactor down scale factor.
     * @param dimming         dimming effect.
     * @param debug           debug policy.
     * @return well instantiated fragment.
     */
    public static DialogFragment newInstance(int radius,
                                             float downScaleFactor,
                                             boolean dimming,
                                             boolean debug,
                                             int template) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putInt(
                BUNDLE_KEY_BLUR_RADIUS,
                radius
        );
        args.putFloat(
                BUNDLE_KEY_DOWN_SCALE_FACTOR,
                downScaleFactor
        );
        args.putBoolean(
                BUNDLE_KEY_DIMMING,
                dimming
        );
        args.putBoolean(
                BUNDLE_KEY_DEBUG,
                debug
        );
        args.putInt(
                BUNDLE_KEY_TEMPLATE,
                template
        );

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
        mTemplate = args.getInt(BUNDLE_KEY_TEMPLATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shakeActivity = (ShakeActivity) getActivity();
        userdata = new userData(shakeActivity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.card1, null);
        switch (mTemplate) {

            case 2:
                view = getActivity().getLayoutInflater().inflate(R.layout.card2, null);
                break;
            case 3:
                view = getActivity().getLayoutInflater().inflate(R.layout.card3, null);
                break;
            case 4:
                view = getActivity().getLayoutInflater().inflate(R.layout.card4, null);
                break;
            case 5:
                view = getActivity().getLayoutInflater().inflate(R.layout.card5, null);
                break;
            case 6:
                view = getActivity().getLayoutInflater().inflate(R.layout.card6, null);
                break;
            default:
                break;
        }
        setCardContent();

        builder.setView(view);
        return builder.create();
    }

    private void setCardContent() {
        userdata.getPhonenum();
        mPicture = view.findViewById(R.id.user_picture1);
        TextView name = view.findViewById(R.id.card_name);
        TextView phone = view.findViewById(R.id.card_phoneNumber);
        TextView email = view.findViewById(R.id.card_email);

        convertQRButton = view.findViewById(R.id.convertQR);
        convertQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
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

    private void startScanning() {
        new IntentIntegrator(getActivity()).
                setBeepEnabled(false).
                setOrientationLocked(false).
                setCaptureActivity(CustomScannerActivity.class).
                initiateScan();
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

    @Override
    protected boolean isDebugEnable() {
        return mDebug;
    }

    @Override
    protected boolean isDimmingEnable() {
        return mDimming;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return mDownScaleFactor;
    }

    @Override
    protected int getBlurRadius() {
        return mRadius;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().finish();
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