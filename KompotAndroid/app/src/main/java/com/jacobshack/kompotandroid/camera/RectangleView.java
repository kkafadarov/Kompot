package com.jacobshack.kompotandroid.camera;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jacobshack.kompotandroid.R;

import java.util.Vector;


/**
 * Created by kkafadarov on 10/5/14.
 *
 * I hereby proclaim this file to be voodoo magic.
 * It is not meant to be understood by mere mortals.
 * Praise gods of StackOverflow & GitHub.
 */
public class RectangleView extends View {

    private static Rect theRectangle;
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;

    private static final float LANDSCAPE_WIDTH_RATIO = 5f/9;
    private static final float LANDSCAPE_HEIGHT_RATIO = 1f/2;
    private static final int LANDSCAPE_MAX_FRAME_WIDTH = (int) (1920 * LANDSCAPE_WIDTH_RATIO); // = 5/8 * 1920
    private static final int LANDSCAPE_MAX_FRAME_HEIGHT = (int) (1080 * LANDSCAPE_HEIGHT_RATIO); // = 5/8 * 1080

    private static final float PORTRAIT_WIDTH_RATIO = 7f/8;
    private static final float PORTRAIT_HEIGHT_RATIO = 3f/8;
    private static final int PORTRAIT_MAX_FRAME_WIDTH = (int) (1080 * PORTRAIT_WIDTH_RATIO); // = 7/8 * 1080
    private static final int PORTRAIT_MAX_FRAME_HEIGHT = (int) (1920 * PORTRAIT_HEIGHT_RATIO); // = 3/8 * 1920

    public RectangleView(Context context) {
        super(context);
        updateFramingRect();
    }

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateFramingRect();
    }

    public void remakeRect() {
        invalidate();
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        if (viewResolution == null) {
            return;
        }

        int width = findDesiredDimensionInRange(LANDSCAPE_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, LANDSCAPE_MAX_FRAME_WIDTH);
        int height = findDesiredDimensionInRange(LANDSCAPE_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, LANDSCAPE_MAX_FRAME_HEIGHT);

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;

        theRectangle = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        Log.d("Kompot", "Rectangle mid is " + theRectangle.centerX() + "x" + theRectangle.centerY());
        Log.d("Kompot", "Rectangle height is " + theRectangle.height());
        Log.d("Kompot", "Rectangle width is " + theRectangle.width());
    }

    private static int findDesiredDimensionInRange(float ratio, int resolution, int hardMin, int hardMax) {
        int dim = (int) (ratio * resolution);
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (theRectangle == null) {
            Log.d("Kompot", "Unable to drow rectangle. (Rectangle is null)");
            return;
        }
        if (theRectangle.width() == MIN_FRAME_WIDTH
                && theRectangle.height() == MIN_FRAME_HEIGHT) {
            remakeRect();
        }
        drawMask(canvas);
        drawBorder(canvas);
    }

    public void drawMask(Canvas canvas) {
        Paint paint = new Paint();
        Resources resources = getResources();
        paint.setColor(resources.getColor(R.color.rectangle_mask));

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawRect(0, 0, width, theRectangle.top, paint);
        canvas.drawRect(0, theRectangle.top, theRectangle.left, theRectangle.bottom + 1, paint);
        canvas.drawRect(theRectangle.right + 1, theRectangle.top, width, theRectangle.bottom + 1, paint);
        canvas.drawRect(0, theRectangle.bottom + 1, width, height, paint);
    }

    public void drawBorder(Canvas canvas) {
        Paint paint = new Paint();
        Resources resources = getResources();
        paint.setColor(resources.getColor(R.color.viewfinder_border));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(resources.getInteger(R.integer.viewfinder_border_width));
        int lineLength = resources.getInteger(R.integer.viewfinder_border_length);

        canvas.drawLine(theRectangle.left - 1, theRectangle.top - 1, theRectangle.left - 1, theRectangle.top - 1 + lineLength, paint);
        canvas.drawLine(theRectangle.left - 1, theRectangle.top - 1, theRectangle.left - 1 + lineLength, theRectangle.top - 1, paint);

        canvas.drawLine(theRectangle.left - 1, theRectangle.bottom + 1, theRectangle.left - 1, theRectangle.bottom + 1 - lineLength, paint);
        canvas.drawLine(theRectangle.left - 1, theRectangle.bottom + 1, theRectangle.left - 1 + lineLength, theRectangle.bottom + 1, paint);

        canvas.drawLine(theRectangle.right + 1, theRectangle.top - 1, theRectangle.right + 1, theRectangle.top - 1 + lineLength, paint);
        canvas.drawLine(theRectangle.right + 1, theRectangle.top - 1, theRectangle.right + 1 - lineLength, theRectangle.top - 1, paint);

        canvas.drawLine(theRectangle.right + 1, theRectangle.bottom + 1, theRectangle.right + 1, theRectangle.bottom + 1 - lineLength, paint);
        canvas.drawLine(theRectangle.right + 1, theRectangle.bottom + 1, theRectangle.right + 1 - lineLength, theRectangle.bottom + 1, paint);
    }

    public synchronized Vector<Float> getScale() {
        Vector<Float> result = new Vector<Float>();
        result.add(LANDSCAPE_WIDTH_RATIO);
        result.add(LANDSCAPE_HEIGHT_RATIO);
        //result.add(new Float(theRectangle.left));
        //result.add(new Float(theRectangle.top));
        result.add(new Float(theRectangle.width()));
        result.add(new Float(theRectangle.height()));
        return result;
    }
}
