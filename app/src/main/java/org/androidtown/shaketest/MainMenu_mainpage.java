package org.androidtown.shaketest;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenu_mainpage extends Fragment {
    private int chk;
    FragmentManager fragmentManager;
    MainMenu activity;
    public static MainMenu_mainpage newInstance() {
        Bundle args = new Bundle();
        MainMenu_mainpage fragment = new MainMenu_mainpage();

        fragment.setArguments(args);
        return fragment;
    }
    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_main_menu_mainpage, container, false);

        return mView;
    }
}