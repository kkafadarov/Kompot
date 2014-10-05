package com.jacobshack.kompotandroid;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jacobshack.kompotandroid.camera.PhotoHandler;
import com.jacobshack.kompotandroid.camera.Preview;
import com.jacobshack.kompotandroid.camera.RectangleView;

import java.util.Vector;

public class CardPictureActivity extends Activity {

    private Camera theCamera;
    private Preview thePreview;
    private RectangleView theRectangleView;
    private PhotoHandler photoHandler;

    @Override
    public void onResume() {
        super.onResume();
        theCamera.startPreview();          // Start camera on resume
    }



    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        theCamera.stopPreview();
    }

    private Handler autoFocusHandler = new Handler();

    // Mimic continuous auto-focusing
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if(theCamera != null) {
                theCamera.autoFocus(autoFocusCB);
            }
        }
    };
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_card_picture);

        theCamera = KompotUtil.getCamera();
        theCamera.autoFocus(autoFocusCB);
        thePreview = new Preview(this, theCamera);
        theRectangleView = new RectangleView(this);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

        preview.addView(thePreview);
        preview.addView(theRectangleView);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vector<Float> rectData = theRectangleView.getScale();
                if (photoHandler == null) {
                    photoHandler = new PhotoHandler(getApplicationContext(), rectData);
                }
                theCamera.takePicture(null, null, photoHandler);
                String filePath = photoHandler.getLastFilePath();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("type", "CardImage");
                if (filePath == null) {
                    setResult(RESULT_CANCELED, returnIntent);
                } else {
                    returnIntent.putExtra(Constants.CARD_IMAGE, filePath);
                    setResult(0, returnIntent);
                }
                Log.d("KompotTest", "Finish from cardpicture");
                //theCamera.stopPreview();
                //theCamera.release();
                //theCamera = null;
                finish();
            }
        });
    }

}
