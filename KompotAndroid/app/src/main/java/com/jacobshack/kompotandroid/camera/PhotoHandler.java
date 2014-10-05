package com.jacobshack.kompotandroid.camera;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;
    private Vector<Float> rectData;
    private String lastFilePath;
    public PhotoHandler(Context context, Vector<Float> rectData) {
        this.context = context;
        this.rectData = rectData;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bmpData = BitmapFactory.decodeByteArray(data, 0, data.length);

        float scaleWidth = rectData.elementAt(0);
        float scaleHeight = rectData.elementAt(1);
        int imageWidth = bmpData.getWidth();
        int imageHeight = bmpData.getHeight();

        int rectWidth = (int) rectData.elementAt(2).floatValue();
        int rectHeight = (int) rectData.elementAt(3).floatValue();
        int verticalOffset = (int) ((float) imageWidth * (scaleWidth / 2));
        int firstPixelX = imageWidth / 2 - verticalOffset;
        int horizontalOffset = (int) ((float) imageHeight * (scaleHeight / 2));
        int firstPixelY = imageHeight / 2 - horizontalOffset;
        Bitmap croppedImage = bmpData.createBitmap(bmpData, firstPixelX, firstPixelY,
                (int) ((float)imageWidth * (scaleWidth)), (int) ((float)imageHeight * (scaleHeight)));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        data = stream.toByteArray();
        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            lastFilePath = filename;
            Toast.makeText(context, "New Image saved:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
            lastFilePath = null;
        }
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "KompotStorage");
    }

    public String getLastFilePath() {
        return lastFilePath;
    }
} 