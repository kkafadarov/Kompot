package com.jacobshack.kompotandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;

public class PairStudent extends ActionBarActivity {

    private static String uniqueIdentifier;
    private static String imageFilePath;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pair_student);

        getQRCode();
    }

    public static void resetData() {
        uniqueIdentifier = null;
    }

    private void getQRCode() {
        Intent qrIntent = new Intent(this, QRScannerActivity.class);
        startActivityForResult(qrIntent, 0);
    }

    private void getCardPicture() {
        Intent pictureIntent = new Intent(this, CardPictureActivity.class);
        startActivityForResult(pictureIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String requestType = data.getStringExtra("type");

        if (requestType == null) {
            Log.d("Kompot", "Request type is null");
            return;
        }
        Log.w("KompotTest", requestType);
        if (requestType.equals("QRScan")) {
            uniqueIdentifier = data.getStringExtra(Constants.QR_CODE);
            getCardPicture();
        } else if (requestType.equals("CardImage")) {
            Log.d("KompotTest", uniqueIdentifier);
            // Process the picture
            // Send a request to the server
            // Respond if success or not
            imageFilePath = data.getStringExtra(Constants.CARD_IMAGE);
            int success = KompotRequest.uploadFile(imageFilePath, uniqueIdentifier);

            if (success == 0) {
                finish();
            } else {
                KompotUtil.makeWarning(this, "Unsuccessful Student/Exam pairing.");
                //KompotUtil.makeNotification(this, "Failiure", "Unsuccessful Student/Exam pairing");
                // TODO: Elaborate on error messages.
                // TODO: Add manual input fallback for student name.
                finish();
            }
        }
    }
}
