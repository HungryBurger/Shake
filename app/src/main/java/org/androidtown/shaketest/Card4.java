package org.androidtown.shaketest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Card4 extends Fragment {
    ArrayList<CustomizedAdapter.Item> list;

    public static Card4 newInstance() {
        Bundle args = new Bundle();
        Card4 fragment = new Card4();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.card4, container, false);
        return mView;
    }
}
