package org.androidtown.shaketest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mEditor;

    private static final String UI_ITEM_NO = "UI_Item_No";
    private static final String SERVICE_CHECK = "ServiceCheck";
    private static final String CHECK_FIRST = "isFirst";
    private static final String MY_INFO = "MyInfo";

    private SharedPrefManager (Context context) {
        try {
            mSharedPrefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            mEditor = mSharedPrefs.edit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Test",e.getMessage());
        }
    }

    public static SharedPrefManager getInstance (Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        } return mInstance;
    }

    public void setUI_ItemNo (int no) {
        mEditor.putInt(UI_ITEM_NO, no);
        mEditor.commit();
    }

    public int getUI_ItemNo () {
        return mSharedPrefs.getInt(UI_ITEM_NO, 1);
    }

    public void setServiceCheck (boolean isChecked) {
        mEditor.putBoolean(SERVICE_CHECK, isChecked);
        mEditor.commit();
    }

    public boolean getServiceCheck () {
        return mSharedPrefs.getBoolean(SERVICE_CHECK, false);
    }

    public void setCheckFirst (boolean isFirst) {
        mEditor.putBoolean(CHECK_FIRST, isFirst);
        mEditor.commit();
    }

    public boolean getCheckFirst () {
        return mSharedPrefs.getBoolean(CHECK_FIRST, false);
    }


}
