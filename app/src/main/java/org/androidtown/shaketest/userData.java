package org.androidtown.shaketest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class userData {
    Toolbar toolbar;
    View nav_header_view;
    TextView mName, mPhoneNum, mEmail;
    CircleImageView mPicture1;
    public Bitmap imageBitmap;
    private static final int FROM_ALBUM = 1;
    public  FirebaseAuth mAuth;
    public  FirebaseUser mUser;
    public FirebaseAuth.AuthStateListener mListener;
    public String displayUserName, displayUserEmail, displayUserPhoneNumber;
    // [END declare_auth]
    public DatabaseReference databaseReference;
    String TAG = "tag";

    private Activity activity;
    public userData(){

    }
    public userData(ShakeActivity shakeActivity){
        this.activity = shakeActivity;
    }
    public userData(MainMenu mainMenu){
        this.activity = mainMenu;
    }

    public void getUserInfo(){
        mAuth = FirebaseAuth.getInstance();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();

                if (mUser != null) {
                    displayUserName = mUser.getDisplayName();
                    displayUserEmail = mUser.getEmail();
                    getPhonenum();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("userImg");
                    setProfile();
                } else {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }
            }
        };
    }
    private void setProfile() {
        mEmail =  nav_header_view.findViewById(R.id.profile_E_mail);
        mEmail.setSelected(true);
        mName = nav_header_view.findViewById(R.id.profile_name);
        mPhoneNum =  nav_header_view.findViewById(R.id.profile_phone_number);
        mPicture1 =  nav_header_view.findViewById(R.id.profile_picture);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String)dataSnapshot.getValue();
                if(value != null) {
                    mPicture1.setImageBitmap(stringToBitmap(value));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mPicture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog();
            }
        });
        mEmail.setText(displayUserEmail);
        mName.setText(displayUserName);
        mPhoneNum.setText(displayUserPhoneNumber);
    }
    public void getPhonenum() {
        TelephonyManager telephonyManager = (TelephonyManager)activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String phoneNum = telephonyManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            }
            displayUserPhoneNumber = PhoneNumberUtils.formatNumber(phoneNum);
        } catch (SecurityException e) {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public void imageDialog() {
        Log.d("tag", "imageDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("사진 선택");
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("앨범 찾기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                galleryAddPic();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void galleryAddPic() {
        Log.d("tag", "galleryAddPic: ");
        Intent pickPic = new Intent(Intent.ACTION_PICK);
        pickPic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPic.setType("image/*");
        if (pickPic.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(pickPic, FROM_ALBUM);
        }
    }

    public void uploadDB(Bitmap bitmap){
        Log.d("tag", "uploadDB: ");
        //mSharedPrefs.setImage(bitmap);
        databaseReference.setValue(bitmapToString(activity, bitmap));
    }
    public  Bitmap stringToBitmap(String bitmapString){
        Log.d("tag", "stringToBitmap: ");
        byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
    public  String bitmapToString(Context context, Bitmap bitmap){
        Log.d("tag", "bitmapToString: ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
    }
}
