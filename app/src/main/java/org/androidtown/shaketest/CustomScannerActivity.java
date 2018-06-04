package org.androidtown.shaketest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;

public class CustomScannerActivity extends Activity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private final String  HEADER = "shake#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory());

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(CustomScannerActivity.this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1;
        getWindow().setAttributes(params);

        Log.d("QRCODE", makeContents());
        /* Create QR code and Connect it with ImageView */
        generateQRCode(makeContents());
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    /**
     * Make Content to be used for QR
     * by Our Format
     * @return
     */
    private String makeContents () {
        return (HEADER + FirebaseAuth.getInstance().getUid());
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
