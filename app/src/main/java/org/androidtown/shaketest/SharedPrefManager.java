package org.androidtown.shaketest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class SharedPrefManager extends AppCompatActivity{

    private static SharedPrefManager mInstance;
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mEditor;
    private static final String UI_ITEM_NO = "UI_Item_No";
    private static final String IMAGEKEY = "imagekey";

    private SharedPrefManager (Context context) {
        mSharedPrefs = context.getSharedPreferences(
                "pref", Context.MODE_PRIVATE
        );
        mEditor =mSharedPrefs.edit();
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
}
