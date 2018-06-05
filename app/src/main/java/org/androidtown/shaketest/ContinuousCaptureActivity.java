package org.androidtown.shaketest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.List;

public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    /* Instance Variable for SaveContact method */
    private ArrayList<ContentProviderOperation> operationLists;
    private String DisplayName;
    private String MobileNumber;
    private String emailAddress;

    private final String  HEADER = "shake#";

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            if (result.getText().startsWith("shake")) {
                String[] arr = result.getText().split("#");
                lastText = arr[1];
                barcodeView.setStatusText(arr[1]);
                Toast.makeText(getApplicationContext(), "스캔 완료!", Toast.LENGTH_SHORT).show();

                final String uid = arr[1];
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference contactListRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("contact_list");

                contactListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> contactList = null;
                        if (dataSnapshot.exists()) {
                            contactList = (ArrayList<String>) dataSnapshot.getValue();
                        } else {
                            contactList = new ArrayList<>();
                        }
                        if (!contactList.contains(uid)) {
                            contactList.add(uid);
                            contactListRef.setValue(contactList);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } beepManager.playBeepSoundAndVibrate();

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

        setContentView(R.layout.continuous_scan);
        ((ImageView)findViewById(R.id.qrView)).setImageBitmap(generateQRCode(makeContents()));

        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
    }

    /**
     * Make Content to be used for QR
     * by Our Format
     * @return
     */
    private String makeContents () {
        return (HEADER + FirebaseAuth.getInstance().getUid());
    }
    //Overloading
    private String makeContents (String content) {
        return (HEADER + content);
    }

    /**
     * Create QR code and Connect it with ImageView
     * @param content
     */
    public Bitmap generateQRCode (String content) {
        Bitmap bitmap = null;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            // 한글 지원
            String data = new String (content.getBytes("UTF-8"), "ISO-8859-1");
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
        } return bitmap;
    }

    /**
     * Convert Content to Bitmap
     * then, Return it
     * @param matrix
     * @return
     */
    public Bitmap toBitmap (BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; ++ x) {
            for (int y = 0; y < height; ++ y) {
                bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        } return bitmap;
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
