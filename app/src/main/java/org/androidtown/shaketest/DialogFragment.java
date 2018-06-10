package org.androidtown.shaketest;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

public class DialogFragment extends BlurDialogFragment {

    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";
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
    private static final String BUNDLE_KEY_IS_OPPONENT = "bundle_key_opponent";
    private static final String BUNDLE_KEY_ACTIVIY = "bundle_key_activity";

    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private int mTemplate;
    private ContactData mOpponent;
    private int activityNo;
    private Activity activity;
    private Intent shakeIntent;
    private View view;
    CircleImageView mPicture, convertQRButton, read;

    private SharedPrefManager mSharedPrefManager;
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
                                             int template,
                                             ContactData opponent,
                                             int activity_no) {
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
        args.putSerializable(
                BUNDLE_KEY_IS_OPPONENT,
                opponent
        );
        args.putInt(
                BUNDLE_KEY_ACTIVIY,
                activity_no
        );

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        shakeIntent = new Intent(activity, ShakeActivity.class);

        Bundle args = getArguments();
        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
        mTemplate = args.getInt(BUNDLE_KEY_TEMPLATE);
        mOpponent = (ContactData) args.getSerializable(BUNDLE_KEY_IS_OPPONENT);
        activityNo = args.getInt(BUNDLE_KEY_ACTIVIY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefManager = SharedPrefManager.getInstance(getContext());
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
        if (mOpponent == null)
            setCardContent();
        else
            setCardContent(mOpponent);

        builder.setView(view);
        return builder.create();
    }

    private void setCardContent() {
        this.mPicture = view.findViewById(R.id.user_picture1);
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

        name.setText(mSharedPrefManager.getUserName());
        phone.setText(mSharedPrefManager.getUserPhonenum());
        email.setText(mSharedPrefManager.getUserEmail());
        email.setSelected(true);

        if (mSharedPrefManager.getUserImage() != null)
            this.mPicture.setImageBitmap(mSharedPrefManager.getUserImage());
        else
            this.mPicture.setImageResource(R.drawable.user_profile);
    }

    private void setCardContent(ContactData oppo) {
        mPicture = view.findViewById(R.id.user_picture1);
        TextView name = view.findViewById(R.id.card_name);
        final TextView phone = view.findViewById(R.id.card_phoneNumber);
        final TextView email = view.findViewById(R.id.card_email);

        convertQRButton = view.findViewById(R.id.convertQR);
        convertQRButton.setVisibility(View.INVISIBLE);

        name.setText(oppo.getName());
        phone.setText(oppo.getPhoneNum());
        email.setText(oppo.getEmail());
        email.setSelected(true);

        if (oppo.getImage() != null)
            this.mPicture.setImageBitmap(stringToBitmap(oppo.getImage()));
        else
            mPicture.setImageResource(R.drawable.user_profile);
        //TODO 전화걸기 및 이메일 전송 기능추가
//        phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(
//                        Intent.ACTION_CALL,
//                        Uri.parse("tel:" + phone.getText())
//                ));
//            }
//        });
//
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(
//                        Intent.ACTION_SENDTO,
//                        Uri.parse("mailto:" + email.getText())
//                ));
//            }
//        });
    }

    private void startScanning() {
        Intent intent = new Intent(getContext(), ContinuousCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public Bitmap stringToBitmap(String bitmapString) {
        if (bitmapString == null) return null;

        Log.d("tag", "stringToBitmap: ");
        byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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

        switch (activityNo) {
            case 1: {
                getActivity().finish();
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                //TODO 전송화면으로 전환
                //transferDialog();
                getActivity().finish();
                break;
            }
            default: {
                Log.d("OnDismiss", "Invalid Value");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void transferDialog () {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("전송 모드로 전환하시겠습니까");
        builder.setPositiveButton("전환", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().startActivity(new Intent(getActivity(), ShakeActivity.class));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                dialog.cancel();
            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}