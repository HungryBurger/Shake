package org.androidtown.shaketest;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    View nav_header_view;
    TextView mName, mPhoneNum, mEmail;
    CircleImageView mPicture;
    ImageButton settingButton;
    Button read,write;
    private static final int FROM_ALBUM = 1;
    private static final int REQUEST_IMAGE_CROP = 2;

    private int chklist=1;
    Uri photoURI;
    NfcAdapter nfcAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mListener;
    public String displayUserName, displayUserEmail, displayUserPhoneNumber;
    // [END declare_auth]
    private GoogleSignInClient mGoogleSignInClient;
    private FragmentManager fragmentManager;
    SharedPrefManager mSharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        read = findViewById(R.id.read);
        write = findViewById(R.id.write);
        /**
         * 자동 권한 요청하기
         */

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(android.Manifest.permission.WRITE_CONTACTS,
                        android.Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.NFC,
                        Manifest.permission.BIND_NFC_SERVICE
                )
                .check();

        initLayout();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
        fragmentManager.beginTransaction().replace(R.id.frameLayout_card, Card1.newInstance()).commit();
        //초기 값 설정 카드 넘버 저장
        mSharedPrefs = SharedPrefManager.getInstance(this);
        Log.d("SharedPref", String.valueOf(mSharedPrefs.getUI_ItemNo()));
        mAuth = FirebaseAuth.getInstance();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();

                if (mUser != null) {
                    displayUserName = mUser.getDisplayName();
                    displayUserEmail = mUser.getEmail();
                    getPhonenum();
                    //callDialog();
                    setProfile();
                } else {
                    startActivity(new Intent(MainMenu.this, MainActivity.class));
                    finish();
                }
            }
        };
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ReadNFC.class);
                startActivity(intent);
            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, WriteNFC.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("name", mName.getText().toString());
                myBundle.putString("phoneNum", mPhoneNum.getText().toString());
                myBundle.putString("E-mail", mEmail.getText().toString());
                intent.putExtras(myBundle);

                startActivity(intent);
            }
        });
    }




    private void onNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(MainMenu.this);

        if (nfcAdapter.isEnabled()) {
        } else {
            AlertDialog.Builder alertBox = new AlertDialog.Builder(MainMenu.this);
            alertBox.setTitle("NFC Connection ERROR....");
            alertBox.setMessage("NFC를 켜주세요.");
            alertBox.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                        startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                    else
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            });
            alertBox.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertBox.show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(getApplicationContext(), ShakeService.class);
        SharedPreferences setRefer = PreferenceManager
                .getDefaultSharedPreferences(this);

        if (setRefer.getBoolean("shake_service_on", false)) {
            startService(intent);
        } else {
            stopService(intent);
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

                // String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/shake/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Log.d("ekit", "ekit");
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mPicture.setImageBitmap(imageBitmap);
                    break;
                }
        }
    }

    private void setProfile() {
        mEmail = (TextView) nav_header_view.findViewById(R.id.profile_E_mail);
        mEmail.setSelected(true);
        mName = (TextView) nav_header_view.findViewById(R.id.profile_name);
        mPhoneNum = (TextView) nav_header_view.findViewById(R.id.profile_phone_number);
        mPicture = (CircleImageView) nav_header_view.findViewById(R.id.profile_picture);


        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog();
            }
        });
        mEmail.setText(displayUserEmail);
        mName.setText(displayUserName);
        mPhoneNum.setText(displayUserPhoneNumber);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Contact List clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, ContactListMain_fragment.newInstance()).commit();

                read.setVisibility(View.GONE);
                write.setVisibility(View.GONE);
                break;
            case R.id.item2:
                Toast.makeText(this, "Editprofile clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, Editprofile.newInstance()).commit();
                read.setVisibility(View.GONE);
                write.setVisibility(View.GONE);
                break;
            case R.id.item3:
                Toast.makeText(this, "Main page clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
                String temp1 = "Card"+mSharedPrefs.getUI_ItemNo()+".newInstance()";
                if(mSharedPrefs.getUI_ItemNo()==1)
                    fragmentManager.beginTransaction().replace(R.id.frameLayout_card,Card1.newInstance()).commit();
                else if(mSharedPrefs.getUI_ItemNo()==2)
                    fragmentManager.beginTransaction().replace(R.id.frameLayout_card,Card2.newInstance()).commit();
                else if(mSharedPrefs.getUI_ItemNo()==3)
                    fragmentManager.beginTransaction().replace(R.id.frameLayout_card,Card3.newInstance()).commit();
                else if(mSharedPrefs.getUI_ItemNo()==4)
                    fragmentManager.beginTransaction().replace(R.id.frameLayout_card,Card4.newInstance()).commit();
                read.setVisibility(View.GONE);
                write.setVisibility(View.GONE);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        ){
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
        nav_header_view = navigationView.getHeaderView(0);
        toolbar.setTitle("Shake");
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
        else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mListener != null) {
            mAuth.removeAuthStateListener(mListener);
        }
    }

    private void getPhonenum() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String phoneNum = telephonyManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            }
            displayUserPhoneNumber = PhoneNumberUtils.formatNumber(phoneNum);
        } catch (SecurityException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
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
    public void imageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
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
        Intent pickPic = new Intent(Intent.ACTION_PICK);
        pickPic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPic.setType("image/*");
        if (pickPic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPic, FROM_ALBUM);
        }
    }
    public void CropPicture(Uri uri) {
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
    protected void onDestroy() {
        super.onDestroy();

        Log.d("AppLifeCycle", "MainMenu");
    }
}