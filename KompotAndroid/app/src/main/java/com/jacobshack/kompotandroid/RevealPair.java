package com.jacobshack.kompotandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


public class RevealPair extends ActionBarActivity {

    private static String uniqueIdentifier;
    private static String exam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reveal_pair);

        Intent intent = getIntent();
        exam = intent.getStringExtra("Exam");

        Intent qrIntent = new Intent(this, QRScannerActivity.class);
        startActivityForResult(qrIntent, 0);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String requestType = data.getStringExtra("type");

        if (requestType == null) {
            Log.d("Kompot", "Request type is null");
            return;
        }

        uniqueIdentifier = data.getStringExtra(Constants.QR_CODE);
        String user = KompotRequest.revealPair(exam, uniqueIdentifier);

        TextView textView = (TextView) findViewById(R.id.field1);
        textView.setText(user);

    }


}
