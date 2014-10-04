package com.jacobshack.kompotandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setFormats(Arrays.asList(BarcodeFormat.QR_CODE));

        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("type", "QRScan");
        if (rawResult.getBarcodeFormat() != BarcodeFormat.QR_CODE) {
            setResult(RESULT_CANCELED, returnIntent);
        } else {
            returnIntent.putExtra(Constants.QR_CODE, rawResult.getText());
            setResult(0, returnIntent);
        }
        finish();
    }
}
