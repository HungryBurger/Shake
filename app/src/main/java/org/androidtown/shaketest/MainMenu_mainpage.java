package org.androidtown.shaketest;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class MainMenu_mainpage extends Fragment {

    private FragmentManager fragmentManager;

    public static MainMenu_mainpage newInstance() {
        Bundle args = new Bundle();
        MainMenu_mainpage fragment = new MainMenu_mainpage();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int subActionButtonSize = 140;

        ImageView icon = new ImageView(getActivity());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.plus));
        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new Builder(getActivity())
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);

        ImageView settingIcon = new ImageView(getActivity());
        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.settings));
        SubActionButton settingBtn = itemBuilder.setContentView(settingIcon)
                                            .setLayoutParams(params)
                                            .build();

        ImageView shakeIcon = new ImageView(getActivity());
        shakeIcon.setImageDrawable(getResources().getDrawable(R.drawable.smartphone));
        SubActionButton shakeBtn = itemBuilder.setContentView(shakeIcon)
                                            .setLayoutParams(params)
                                            .build();

//        ImageView listIcon = new ImageView(getActivity());
//        listIcon.setImageDrawable(getResources().getDrawable(R.drawable.contactlist));
//        SubActionButton listBtn = itemBuilder.setContentView(listIcon)
//                                            .setLayoutParams(params)
//                                            .build();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        shakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShakeActivity.class));
            }
        });

//        listBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.frameLayout, ContactListMain_fragment.newInstance()).commit();
//            }
//        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                                            .addSubActionView(settingBtn)
                                            .addSubActionView(shakeBtn)
                                            .attachTo(actionButton)
                                            .build();
    }

    //This section shows the fragment.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.activity_main_menu_mainpage, container, false);
        return mView;
    }
}