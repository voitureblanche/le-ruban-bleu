package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Toast;


// cf. http://stackoverflow.com/questions/7418955/how-to-animate-zoom-out-with-imageview-that-uses-matrix-scaling
public class CustomImageView extends ImageView {

    // Gesture
    private MyGestureDetectorListener mGestureDetectorListener;
    private GestureDetector mGestureDetector;

    private int mZoomLevel;
    static final int ZOOM_LEVEL_0 = 0;
    static final int ZOOM_LEVEL_1 = 1;

    // Zoom management
    Matrix mMatrix;
    Matrix mInverseMatrix;
    float[] mDoubleTapImagePoint = new float[2];
    float mScaleMin = 0;
    float mScaleMax = 0;


    public CustomImageView(Context context) {
        super(context);
        init(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // Constructor
    private void init(Context context)
    {
        super.setClickable(true);

        // Zoom management
        mMatrix = new Matrix();
        mInverseMatrix = new Matrix();

        // Gesture management
        mGestureDetectorListener = new MyGestureDetectorListener();
        mGestureDetector = new GestureDetector(getContext(), mGestureDetectorListener);
        mGestureDetector.setOnDoubleTapListener(mGestureDetectorListener);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                boolean result;
                result = mGestureDetector.onTouchEvent(event);
                return result;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get view dimension
        int viewHeight = getHeight();
        int viewWidth = getWidth();

        // Get bitmap dimension
        Drawable drawing = getDrawable();
        if (drawing == null) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();

        // Compute scales
        mScaleMax = (float) viewHeight / bitmapHeight;
        mScaleMin = (float) viewWidth / bitmapWidth;

    }

    // Gesture listener
    private class MyGestureDetectorListener implements GestureDetector.OnGestureListener , GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (mZoomLevel == ZOOM_LEVEL_0) {
                Toast.makeText(getContext(), "Zoom!", Toast.LENGTH_LONG).show();
                mZoomLevel = ZOOM_LEVEL_1;
                zoom(motionEvent);
            }
            else if (mZoomLevel == ZOOM_LEVEL_1) {
                Toast.makeText(getContext(), "Unzoom!", Toast.LENGTH_LONG).show();
                mZoomLevel = ZOOM_LEVEL_0;
                setScaleType(ScaleType.FIT_CENTER);
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
            if (mZoomLevel == ZOOM_LEVEL_1) {
                // Toast.makeText(getContext(), "Scrolling !?", Toast.LENGTH_LONG).show();
                Log.d("MyGestureDetectorListener", "onScroll " + Float.toString(v) + " " + Float.toString(v2));
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
            return false;
        }
    }


    private void zoom(MotionEvent event)
    {
        // Tapped point coordinates
        float x = event.getX();
        float y = event.getY();

        // Matrix
        float[] matrixValues = new float[9];
        //setImageMatrix(matrix);
        //setScaleType(ScaleType.MATRIX);

        mMatrix.set(getImageMatrix());
        mMatrix.getValues(matrixValues);
        mMatrix.invert(mInverseMatrix);
        mDoubleTapImagePoint[0] = x;
        mDoubleTapImagePoint[1] = y;
        mInverseMatrix.mapPoints(mDoubleTapImagePoint);
        //final float scale = matrixValues[Matrix.MSCALE_X];
        //final float targetScale = scale < 1.0f ? 1.0f : calculateFitToScreenScale();
        final float targetScale = 0;
        final float finalX;
        final float finalY;

        // Zoom out
        if (targetScale == mScaleMin) {
            // scaling the image to fit the screen, we want the resulting image to be centered. We need to take
            // into account the shift that is applied to zoom on the tapped point, easiest way is to reuse
            // the transformation matrix.
            RectF imageBounds = new RectF(getDrawable().getBounds());
            // set up matrix for target
            mMatrix.reset();
            mMatrix.postTranslate(-mDoubleTapImagePoint[0], -mDoubleTapImagePoint[1]);
            mMatrix.postScale(targetScale, targetScale);
            mMatrix.mapRect(imageBounds);

            finalX = ((getWidth() - imageBounds.width()) / 2.0f) - imageBounds.left;
            finalY = ((getHeight() - imageBounds.height()) / 2.0f) - imageBounds.top;
        }
        // else zoom around the double-tap point
        else {
            finalX = x;
            finalY = y;
        }

        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final long startTime = System.currentTimeMillis();
        final long duration = 800;
        post(new Runnable() {
            @Override
            public void run() {
                float t = (float) (System.currentTimeMillis() - startTime) / duration;
                t = t > 1.0f ? 1.0f : t;
                float interpolatedRatio = interpolator.getInterpolation(t);
                float tempScale = scale + interpolatedRatio * (targetScale - scale);
                float tempX = x + interpolatedRatio * (finalX - x);
                float tempY = y + interpolatedRatio * (finalY - y);
                mMatrix.reset();
                // translate initialPoint to 0,0 before applying zoom
                mMatrix.postTranslate(-mDoubleTapImagePoint[0], -mDoubleTapImagePoint[1]);
                // zoom
                mMatrix.postScale(tempScale, tempScale);
                // translate back to equivalent point
                mMatrix.postTranslate(tempX, tempY);
                setImageMatrix(mMatrix);
                if (t < 1f) {
                    post(this);
                }
            }
        });
    }

}
