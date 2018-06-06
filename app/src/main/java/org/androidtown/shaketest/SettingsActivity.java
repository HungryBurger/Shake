package org.androidtown.shaketest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,
                        new SettingsFragment()).commit();

    }

    public static class SettingsFragment extends PreferenceFragment {
        private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
        private Context context;
        private SharedPrefManager mSharedPrefManager;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);

            mSharedPrefManager = SharedPrefManager.getInstance(getActivity());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            context = getActivity();

            prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Intent intent = new Intent(context, ShakeService.class);
                    SharedPreferences setRefer = PreferenceManager
                            .getDefaultSharedPreferences(context);
                    if (setRefer.getBoolean("shake_service_on", false)) {
                        Log.d("MyReceiver", "서비스 체크 참 ");
                        mSharedPrefManager.setServiceCheck(true);
                        context.startService(intent);
                        BroadCastManager.getInstance(getContext());
                    } else {
                        Log.d("MyReceiver", "서비스 체크 거짓");
                        mSharedPrefManager.setServiceCheck(false);
                        context.stopService(intent);
                        BroadCastManager.getInstance(context).unregisterMyReceiver();
                    }
                }
            }; prefs.registerOnSharedPreferenceChangeListener(prefListener);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
