package org.androidtown.shaketest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.ArrayList;

public class Editprofile extends Fragment {
    private FragmentManager fragmentManager;
    public static Editprofile newInstance() {
        Bundle args = new Bundle();
        Editprofile fragment = new Editprofile();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_editprofile, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        Button a = (Button)mView.findViewById(R.id.button1);
        Button b = (Button)mView.findViewById(R.id.button2);

        a.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),CustomScannerActivity.class);
                Toast.makeText(getActivity(), "QR코드가 눌렸습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentManager.beginTransaction().replace(R.id.frameLayout, Customized_main.newInstance()).commit();
                Toast.makeText(getActivity(), "커스터마이징 수정하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }
}
