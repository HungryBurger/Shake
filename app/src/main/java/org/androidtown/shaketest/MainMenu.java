package org.androidtown.shaketest;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton settingButton;
    public static final int REQUEST_IMAGE_CROP = 2;
    // [END declare_auth]
    public GoogleSignInClient mGoogleSignInClient;
    private FragmentManager fragmentManager;
    int backbuttonChk = 0;
    SharedPrefManager mSharedPrefs;
    userData userdata;

    public static final int FROM_ALBUM = 0;
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        userdata = new userData(MainMenu.this);

        userdata.getUserInfo();
        initLayout();
        mSharedPrefs = SharedPrefManager.getInstance(this);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
        fragmentManager.beginTransaction().replace(R.id.frameLayout_card, CardFragment.newInstance(mSharedPrefs.getUserTemplateNo())).commit();
        backbuttonChk = 1;
        //초기 값 설정 카드 넘버 저장

    }

    public void galleryAddPic() {
        Log.d("tag", "galleryAddPic: ");
        Intent pickPic = new Intent(Intent.ACTION_PICK);
        pickPic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPic.setType("image/*");
        if (pickPic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPic, FROM_ALBUM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userdata.databaseReference != null) {
            userdata.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = (String) dataSnapshot.getValue();
                    if (value != null) {
                        userdata.mPicture1.setImageBitmap(userdata.stringToBitmap(value));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case FROM_ALBUM: {
                //앨범에서 가져오기
                if (data.getData() != null) {
                    galleryAddPic();
                    //이미지뷰에 이미지 셋팅
                    CropPicture(data.getData());
                    break;
                }
            }
            case REQUEST_IMAGE_CROP:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Log.d("ekit", "ekit");
                    mSharedPrefs.setUserImage((Bitmap) extras.get("data"));
                    mSharedPrefs.updateMyImgInfo();
                    //userdata.imageBitmap = (Bitmap) extras.get("data");
                    //userdata.uploadDB(userdata.imageBitmap);
                    break;
                }
        }
    }

    public void CropPicture(Uri uri) {
        Log.d("tag", "CropPicture: ");
        Intent cropPic = new Intent("com.android.camera.action.CROP");
        cropPic.setDataAndType(uri, "image/*");
        cropPic.putExtra("outputX", 200); // crop한 이미지의 x축 크기 (integer)
        cropPic.putExtra("outputY", 200); // crop한 이미지의 y축 크기 (integer)
        cropPic.putExtra("aspectX", 1); // crop 박스의 x축 비율 (integer)
        cropPic.putExtra("aspectY", 1); // crop 박스의 y축 비율 (integer)
        cropPic.putExtra("scale", true);
        cropPic.putExtra("return-data", true);
        if (cropPic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cropPic, REQUEST_IMAGE_CROP);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Contact List clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, ContactListMain_fragment.newInstance()).commit();
                backbuttonChk=1;
                break;
            case R.id.item2:
                Toast.makeText(this, "Editprofile clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, Editprofile.newInstance()).commit();
                backbuttonChk=1;
                break;
            case R.id.item3:
                Toast.makeText(this, "Main page clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
                fragmentManager.beginTransaction().replace(R.id.frameLayout_card, CardFragment.newInstance(mSharedPrefs.getUserTemplateNo())).commit();
                backbuttonChk=0;
                break;
            case R.id.log_out:
                click_log_out();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        settingButton = findViewById(R.id.setting_button);
        userdata.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(userdata.toolbar);

        /**
         * 환경설정 액티비티로 넘어가기
         */
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                userdata.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(MainMenu.this);
        userdata.nav_header_view = navigationView.getHeaderView(0);

        userdata.toolbar.setTitle("Shake");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void click_log_out() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
        builder.setMessage("로그아웃 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                }).setNegativeButton("취소", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (backbuttonChk == 1) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
            fragmentManager.beginTransaction().replace(R.id.frameLayout_card, CardFragment.newInstance(mSharedPrefs.getUserTemplateNo())).commit();
            backbuttonChk = 0;

        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                toast.cancel();
                super.onBackPressed();
            }
        }
    }

    public void showGuide() {
        toast = Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        userdata.mAuth.addAuthStateListener(userdata.mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (userdata.mListener != null) {
            userdata.mAuth.removeAuthStateListener(userdata.mListener);
        }
    }

    private void signOut() {
        // Firebase sign out
        userdata.mAuth.signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("AppLifeCycle", "MainMenu");
    }
}
