package org.androidtown.shaketest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    private PreferenceScreen screen;
    private SwitchPreference service_check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,
                        new SettingsFragment()).commit();

    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preference);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Intent intent = new Intent(getApplicationContext(), ShakeService.class);
        SharedPreferences setRefer = PreferenceManager
                .getDefaultSharedPreferences(this);

        if (setRefer.getBoolean("shake_service_on", false)) {
            Toast.makeText(getApplicationContext(), "서비스 실행", Toast.LENGTH_SHORT).show();
            startService(intent);
        } else {
            Toast.makeText(getApplicationContext(), "서비스 취소", Toast.LENGTH_SHORT).show();
            stopService(intent);
        }
    }
}
