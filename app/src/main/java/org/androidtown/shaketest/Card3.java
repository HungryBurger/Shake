package org.androidtown.shaketest;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Card3 extends Fragment {
    FragmentManager fragmentManager;
    public static Card3 newInstance() {
        Bundle args = new Bundle();
        Card3 fragment = new Card3();
        fragment.setArguments(args);
        return fragment;
    }
    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.card2, container, false);
        return mView;
    }
}