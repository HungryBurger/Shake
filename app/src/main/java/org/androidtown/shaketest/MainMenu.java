package org.androidtown.shaketest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.File;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ToggleButton servcie_check;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    View nav_header_view;
    TextView mName, mPhoneNum, mEmail;
    CircleImageView mPicture;
    ImageButton settingButton;
    CircleImageView bPicture;
    NfcAdapter nfcAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mListener;
    private String displayUserName, displayUserEmail, displayUserPhoneNumber;
    // [END declare_auth]
    private GoogleSignInClient mGoogleSignInClient;
    private FragmentManager fragmentManager;

    private Uri photoURI;
    private String mCurrentPhotoPath;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private static final int REQUEST_IMAGE_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initLayout();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
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
        findViewById(R.id.popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog();
            }
        });
        onNFC();
        findViewById(R.id.nfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainMenu.this, NFCActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });

        servcie_check = findViewById(R.id.service_check);
        servcie_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShakeService.class);
                intent.putExtra("userName", displayUserName);
                intent.putExtra("userEmail", displayUserEmail);
                intent.putExtra("userPhoneNum", displayUserPhoneNumber);
                if (servcie_check.isChecked()) {
                    startService(intent);
                } else {
                    stopService(intent);
                }
            }
        });
    }

    private void initLayout() {
        settingButton = findViewById(R.id.setting_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
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
        nav_header_view = navigationView.getHeaderView(0);
        toolbar.setTitle("Shake");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

            case FROM_CAMERA: {
                //촬영
                try {
                    Log.v("알림", "FROM_CAMERA 처리");
                    galleryAddPic();
                    CropPicture(photoURI);
                    //이미지뷰에 이미지셋팅
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case REQUEST_IMAGE_CROP:
                Bundle extras = data.getExtras();

                // String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/shake/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Log.d("ekit", "ekit");
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mPicture.setImageBitmap(imageBitmap);
                    bPicture.setImageBitmap(imageBitmap);
                    break;
                }
                File f = new File(photoURI.getPath());
                if (f.exists()) f.delete();
        }
    }

    private void setProfile() {
        mEmail = (TextView) nav_header_view.findViewById(R.id.profile_E_mail);
        mEmail.setSelected(true);
        mName = (TextView) nav_header_view.findViewById(R.id.profile_name);
        mPhoneNum = (TextView) nav_header_view.findViewById(R.id.profile_phone_number);
        mPicture = (CircleImageView) nav_header_view.findViewById(R.id.profile_picture);
        bPicture = (CircleImageView) findViewById(R.id.profile_image);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Contact List clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, ContactListMain_fragment.newInstance()).commit();
                break;
            case R.id.item2:
                Toast.makeText(this, "Customized clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, Customized_main.newInstance()).commit();
                break;
            case R.id.item3:
                Toast.makeText(this, "Main page clicked..", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.frameLayout, MainMenu_mainpage.newInstance()).commit();
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

    public void callDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MyAlertDialogFragment newDialogFragment = MyAlertDialogFragment.newInstance(displayUserName, displayUserPhoneNumber, displayUserEmail);
        newDialogFragment.show(fm, "dialog");
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
        //this.grantUriPermission("com.android.camera",photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
}