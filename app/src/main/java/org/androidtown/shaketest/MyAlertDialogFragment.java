package org.androidtown.shaketest;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MyAlertDialogFragment extends DialogFragment {
    View mView;
    TextView nameTv;
    TextView emailTv;
    TextView numTv;

    public static MyAlertDialogFragment newInstance(String name, String pNum, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("pNum", pNum);
        bundle.putString("eMail", email);

        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        frag.setArguments(bundle);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.fragmentdialog, null);

        nameTv = (TextView) mView.findViewById(R.id.card_name);
        emailTv = (TextView) mView.findViewById(R.id.card_email);
        numTv = (TextView) mView.findViewById(R.id.card_pNum);

        Bundle data = getArguments();

        String displayName = data.getString("name");
        String phoneNum = data.getString("pNum");
        String eMail = data.getString("eMail");
        nameTv.setText(displayName);
        numTv.setText(phoneNum);
        emailTv.setText(eMail);
        builder.setView(mView).setNegativeButton("닫기",null);

        return builder.create();
    }
}