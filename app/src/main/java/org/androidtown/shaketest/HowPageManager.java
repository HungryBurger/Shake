package org.androidtown.shaketest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class HowPageManager extends Fragment {
    private static final String BUNDLE_LAYOUT = "bundle_layout";
    private int layout;
    private ViewGroup view;
    public static HowPageManager newInstance (int template) {
        HowPageManager fragment = new HowPageManager();
        Bundle args = new Bundle();

        args.putInt(
                BUNDLE_LAYOUT,
                template
        );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle args = getArguments();
        layout = args.getInt(BUNDLE_LAYOUT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup)inflater.inflate(R.layout.activity_how_page1, container, false);

        switch (layout) {
            case 2: {
                view = (ViewGroup)inflater.inflate(R.layout.activity_how_page2, container, false);
                break;
            }
            case 3: {
                view = (ViewGroup)inflater.inflate(R.layout.activity_how_page3, container, false);
                break;
            }
            case 4: {
                view = (ViewGroup)inflater.inflate(R.layout.activity_how_page4, container, false);
                break;
            }
            default:
                break;
        }
        return view;
    }
}
