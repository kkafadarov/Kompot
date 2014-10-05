package com.jacobshack.kompotandroid.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * TODO: document your custom view class.
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera theCamera;
    private SurfaceHolder theHolder;

    public Preview(Context context, Camera camera) {
        super(context);

        theCamera = camera;
       //theCamera.autoFocus(autoFocusCB);
        theHolder = getHolder();
        theHolder.addCallback(this);
        theHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Camera.Parameters params = theCamera.getParameters();
            params.setFocusMode("continuous-picture");
            theCamera.setParameters(params);

            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Kompot", "Error setting camera preview");
        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // Ignore
    }
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // Ignore
    }

}
