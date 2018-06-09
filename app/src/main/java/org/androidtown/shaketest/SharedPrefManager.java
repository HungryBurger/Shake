package org.androidtown.shaketest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mEditor;

    private static final String USER_UID = "UserUid";
    private static final String USER_NAME = "UserName";
    private static final String USER_PHONENUM = "UserPhonNum";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_TEMPLATE_NO = "UserTemplateNo";
    private static final String USER_IMAGE = "UserImage";
    private static final String SERVICE_CHECK = "ServiceCheck";
    private static final String CHECK_FIRST = "isFirst";

    private DatabaseReference mReference;

    private SharedPrefManager(Context context) {
        try {
            mSharedPrefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            mEditor = mSharedPrefs.edit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Test", e.getMessage());
        }
    }

    public static SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void setUserData(String uid, ContactData userData) {
        setUserUid(uid);
        setUserName(userData.getName());
        setUserPhonenum(userData.getPhoneNum());
        setUserEmail(userData.getEmail());
        setUserTemplateNo(userData.getTemplate_no());
        setUserImage(stringToBitmap(userData.getImage()));
    }

    public ContactData getUserData() {
        ContactData userData = new ContactData(
                getUserName(),
                getUserPhonenum(),
                getUserEmail(),
                getUserTemplateNo(),
                bitmapToString(getUserImage())
        );
        return userData;
    }

    public void setUserUid(String uid) {
        mEditor.putString(USER_UID, uid);
        mEditor.commit();
    }

    public String getUserUid() {
        return mSharedPrefs.getString(USER_UID, null);
    }

    public void setUserName(String name) {
        mEditor.putString(USER_NAME, name);
        mEditor.commit();
    }

    public String getUserName() {
        return mSharedPrefs.getString(USER_NAME, null);
    }

    public void setUserPhonenum(String phoneNum) {
        mEditor.putString(USER_PHONENUM, phoneNum);
        mEditor.commit();
    }

    public String getUserPhonenum() {
        return mSharedPrefs.getString(USER_PHONENUM, null);
    }

    public void setUserEmail(String email) {
        mEditor.putString(USER_EMAIL, email);
        mEditor.commit();
    }

    public String getUserEmail() {
        return mSharedPrefs.getString(USER_EMAIL, null);
    }

    public void setUserTemplateNo(int no) {
        mEditor.putInt(USER_TEMPLATE_NO, no);
        mEditor.commit();
    }

    public int getUserTemplateNo() {
        return mSharedPrefs.getInt(USER_TEMPLATE_NO, 1);
    }

    public void setUserImage(Bitmap userImage) {
        mEditor.putString(USER_IMAGE, bitmapToString(userImage));
        mEditor.commit();
        updateMyInfo();
    }

    public Bitmap getUserImage() {
        String userImg = mSharedPrefs.getString(USER_IMAGE, null);
        return stringToBitmap(userImg);
    }

    public void setServiceCheck(boolean isChecked) {
        mEditor.putBoolean(SERVICE_CHECK, isChecked);
        mEditor.commit();
    }

    public boolean getServiceCheck() {
        return mSharedPrefs.getBoolean(SERVICE_CHECK, false);
    }

    public void setCheckFirst(boolean isFirst) {
        mEditor.putBoolean(CHECK_FIRST, isFirst);
        mEditor.commit();
    }

    public boolean getCheckFirst() {
        return mSharedPrefs.getBoolean(CHECK_FIRST, false);
    }

    public Bitmap stringToBitmap(String bitmapString) {
        if (bitmapString == null) return null;

        Log.d("tag", "stringToBitmap: ");
        byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) return null;

        Log.d("tag", "bitmapToString: ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public void updateMyInfo() {
        mReference = FirebaseDatabase.getInstance().getReference().child("myInfo").child(getUserUid());
        mReference.setValue(getUserData());
    }
}
