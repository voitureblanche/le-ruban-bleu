package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;


public class CustomImageView extends ImageView {

    // Zoom levels
    public static int ZOOM_LEVEL_0 = 0;
    public static int ZOOM_LEVEL_1 = 1;

    // Alignments
    public static int ALIGN_LEFT = 0;
    public static int ALIGN_RIGHT = 1;

    // Dimensions
    private int mViewWidth;
    private int mViewHeight;
    private int mBitmapWidth;
    private int mBitmapHeight;

    // Zoom
    private float mScaleMin = 0;
    private float mScaleMax = 0;
    private float mScaleToFitPointX = 0;
    private float mScaleToFitPointY = 0;
    private boolean mFirstDraw = true;


    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageURI(Uri uri) {
        // Get bitmap dimension
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();

        super.setImageURI(uri);
    }

    // cf. http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Get view dimension
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Compute scales
        mScaleMin = (float) mViewWidth / mBitmapWidth;
        mScaleMax = (float) mViewHeight / mBitmapHeight;

        // Compute default matrix that fits the image in the view
        Matrix matrix = new Matrix();
        float[] matrixValues = new float[9];
        RectF bitmapRect = new RectF(0, 0, mBitmapWidth, mBitmapHeight);
        RectF viewRect = new RectF(0, 0, mViewWidth, mViewHeight);
        matrix.setRectToRect(bitmapRect, viewRect, Matrix.ScaleToFit.CENTER);
        matrix.getValues(matrixValues);

        // Scale to fit point
        mScaleToFitPointX = matrixValues[Matrix.MTRANS_X];
        mScaleToFitPointY = matrixValues[Matrix.MTRANS_Y];

        // Apply zoom level
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        int zoomLevel = application.getZoomLevel();
        switch (zoomLevel)
        {
            case 0:
                setScaleType(ScaleType.MATRIX);
                setImageMatrix(matrix);
                break;

            case 1:
                setScaleType(ScaleType.MATRIX);
                matrix.reset();
                matrix.postScale(mScaleMax, mScaleMax);
                matrix.postTranslate(0, 0);
                setImageMatrix(matrix);
                break;
        }

        // Mandatory call
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    // cf. http://stackoverflow.com/questions/7418955/how-to-animate-zoom-out-with-imageview-that-uses-matrix-scaling
    public void zoom(int zoomLevel, int alignment)
    {
        // Matrix
        final Matrix matrix = new Matrix();
        float[] matrixValues = new float[9];
        matrix.set(getImageMatrix());
        matrix.getValues(matrixValues);

        // Scaling variables
        final float startScale = matrixValues[Matrix.MSCALE_X];
        final float endScale;
        final float startX;
        final float startY;
        final float endX;
        final float endY;

        // Zoom
        if (zoomLevel == ZOOM_LEVEL_1) {
            endScale = mScaleMax;
            startX = matrixValues[Matrix.MTRANS_X];
            startY = matrixValues[Matrix.MTRANS_Y];

            if (alignment == ALIGN_LEFT) {
                // Always zoom to the origin
                endX = 0;
                endY = 0;
            }
            else {
                endX = - ((mBitmapWidth * mScaleMax) - mViewWidth);;
                endY = 0;
            }
        }

        // Unzoom
        else {
            endScale = mScaleMin;
            startX = matrixValues[Matrix.MTRANS_X];
            startY = matrixValues[Matrix.MTRANS_Y];
            endX = mScaleToFitPointX;
            endY = mScaleToFitPointY;
        }

        // Interpolation
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final long startTime = System.currentTimeMillis();
        final long duration = 300;

        // Thread
        post(new Runnable() {
            @Override
            public void run() {

                // Increment time
                float t = (float) (System.currentTimeMillis() - startTime) / duration;
                t = t > 1.0f ? 1.0f : t;

                // Compute scale variables
                float interpolatedRatio = interpolator.getInterpolation(t);
                float tempScale = startScale + interpolatedRatio * (endScale - startScale);
                float tempX = startX + interpolatedRatio * (endX - startX);
                float tempY = startY + interpolatedRatio * (endY - startY);

                // Apply matrix
                matrix.reset();
                matrix.postScale(tempScale, tempScale);
                matrix.postTranslate(tempX, tempY);
                setImageMatrix(matrix);

                if (t < 1f) {
                    post(this);
                }
            }
        });
    }

    // Horizontal scroll, only available in ZOOM_LEVEL_1
    public boolean horizontalScroll(float distanceX)
    {
        boolean boundary = false;

        // Matrix
        Matrix matrix = new Matrix();
        float[] matrixValues = new float[9];
        matrix.set(getImageMatrix());
        matrix.getValues(matrixValues);

        // Get current scaling variables
        float startX = matrixValues[Matrix.MTRANS_X];

        // Compute boundaries
        float minX = - ((mBitmapWidth * mScaleMax) - mViewWidth);
        float maxX = 0;

        // Compute translation
        float translateX;
        float endX = startX + distanceX;

        // Detect boundaries
        // Left boundary has been reached
        if (endX < minX) {
            translateX = minX - startX;
            boundary = true;
        }
        // Right boundary has been reached
        else if (endX > 0) {
            translateX = -startX;
            boundary = true;
        }
        else {
            translateX = distanceX;
        }

        // Apply matrix
        matrix.postTranslate(translateX, 0);
        setImageMatrix(matrix);

        return boundary;
    }

}
