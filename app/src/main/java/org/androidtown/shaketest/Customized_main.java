package org.androidtown.shaketest;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Customized_main extends Fragment {
    public static Customized_main newInstance() {
        Bundle args = new Bundle();

        Customized_main fragment = new Customized_main();
        fragment.setArguments(args);

        return fragment;
    }

    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_customized_main, container, false);

        return mView;
    }
}