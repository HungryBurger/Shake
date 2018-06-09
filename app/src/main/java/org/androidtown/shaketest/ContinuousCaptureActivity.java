package org.androidtown.shaketest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private final String HEADER = "shake#";
    private SharedPrefManager mSharedPrefManager;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(HEADER + lastText)) {
                // Prevent duplicate scans
                return;
            }

            if (result.getText().startsWith("shake")) {
                String[] arr = result.getText().split("#");
                lastText = arr[1];
                barcodeView.setStatusText(arr[1]);
                Toast.makeText(getApplicationContext(), "스캔 완료!", Toast.LENGTH_SHORT).show();

                final String uid = arr[1];

                DatabaseReference contactListRef = FirebaseDatabase.getInstance().getReference().child("contact_list").child(mSharedPrefManager.getUserUid());
                if (((ServiceApplication) getApplication()).myContactList == null) {
                    ((ServiceApplication) getApplication()).myContactList = new ArrayList<>();
                    ((ServiceApplication) getApplication()).person = new HashMap<>();
                }
                if (((ServiceApplication) getApplication()).myContactList.contains(uid)) return;

                ((ServiceApplication) getApplication()).myContactList.add(uid);
                contactListRef.setValue(((ServiceApplication) getApplication()).myContactList);

                DatabaseReference infoRef = FirebaseDatabase.getInstance().getReference().child("myInfo").child(uid);
                infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ContactData data = dataSnapshot.getValue(ContactData.class);
                            if (data != null) {
                                saveContacts(
                                        data.getName(),
                                        data.getPhoneNum(),
                                        data.getEmail()
                                );
                                ((ServiceApplication) getApplication()).person.put(uid, data);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "잘 못된 형식의 데이터 입니다.", Toast.LENGTH_SHORT).show();
            }

            //Added preview of scanned barcode
            ImageView imageView = findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(generateQRCode(makeContents(lastText)));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
        setContentView(R.layout.continuous_scan);
        ((ImageView) findViewById(R.id.qrView)).setImageBitmap(generateQRCode(makeContents()));

        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
    }

    /**
     * Make Content to be used for QR
     * by Our Format
     *
     * @return
     */
    private String makeContents() {
        return (HEADER + mSharedPrefManager.getUserUid());
    }

    //Overloading
    private String makeContents(String content) {
        return (HEADER + content);
    }

    /**
     * Create QR code and Connect it with ImageView
     *
     * @param content
     */
    public Bitmap generateQRCode(String content) {
        Bitmap bitmap = null;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            // 한글 지원
            String data = new String(content.getBytes("UTF-8"), "ISO-8859-1");
            bitmap = toBitmap(
                    qrCodeWriter.encode(
                            data,
                            BarcodeFormat.QR_CODE,
                            400, 400)
            );
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Convert Content to Bitmap
     * then, Return it
     *
     * @param matrix
     * @return
     */
    public Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    public void saveContacts(String name, String phonNum, String email) {
        ArrayList<ContentProviderOperation> operationLists;

        operationLists = new ArrayList<>();
        operationLists.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // 상대방 이름 저장
        if (name != null) {
            operationLists.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name
                    ).build());
        }
        // 휴대폰 번호 저장
        if (phonNum != null) {
            operationLists.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phonNum)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        // 이메일 저장
        if (email != null) {
            operationLists.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationLists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
