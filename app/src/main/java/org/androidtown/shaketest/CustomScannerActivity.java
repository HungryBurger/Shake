package org.androidtown.shaketest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomScannerActivity extends Activity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private String userName, userPhoneNum, userEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SharedPrefManager mSharedPrefs;
    private final String  HEADER = "shake#";
    private String lastText;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                return;
            }
            lastText = result.getText();
            barcodeScannerView.setStatusText(result.getText());

            ImageView imageView = findViewById(R.id.qrView);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.BLACK));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory());
        barcodeScannerView.decodeContinuous(callback);

//        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
//        barcodeScannerView.setTorchListener(CustomScannerActivity.this);
//
//        init();
//
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.screenBrightness = 1;
//        getWindow().setAttributes(params);
//
//        Log.d("QRCODE", makeContents());
//        /* Create QR code and Connect it with ImageView */
//        generateQRCode(makeContents());
//        capture = new CaptureManager(this, barcodeScannerView);
//        capture.initializeFromIntent(getIntent(), savedInstanceState);
//        capture.decode();
    }

    private void init () {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        userName = mUser.getDisplayName();
        userEmail = mUser.getEmail();
        userPhoneNum = getPhoneNum();

        mSharedPrefs = SharedPrefManager.getInstance(this);
    }

    /**
     * Make Content to be used for QR
     * by Our Format
     * @return
     */
    private String makeContents () {
        return (HEADER + mUser.getUid());
        //return (HEADER + userName + "#" + userPhoneNum + "#" +userEmail + "#" + mSharedPrefs.getUI_ItemNo() + "#" + mUser.getUid());
    }

    /**
     * Create QR code and Connect it with ImageView
     * @param content
     */
    public void generateQRCode (String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            // 한글 지원
            String data = new String (content.getBytes("UTF-8"), "ISO-8859-1");
            Bitmap bitmap = toBitmap(
                    qrCodeWriter.encode(
                            data,
                            BarcodeFormat.QR_CODE,
                            400, 400)
            );
            ((ImageView)findViewById(R.id.qrView)).setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String getPhoneNum() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String phoneNum = telephonyManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            } return PhoneNumberUtils.formatNumber(phoneNum);

        } catch (SecurityException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }


    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }
}
