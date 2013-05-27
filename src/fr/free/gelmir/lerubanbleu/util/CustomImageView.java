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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Toast;


public class CustomImageView extends ImageView {

    // Gesture
    private MyGestureDetectorListener mGestureDetectorListener;
    private GestureDetector mGestureDetector;

    private int mZoomLevel;
    static final int ZOOM_LEVEL_0 = 0;
    static final int ZOOM_LEVEL_1 = 1;

    // Zoom
    Matrix mMatrix;
    Matrix mInverseMatrix;
    float mScaleMin = 0;
    float mScaleMax = 0;

    private boolean mFirstDraw = true;



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

        if (mFirstDraw) {
            // Get view dimension
            int viewWidth = getWidth();
            int viewHeight = getHeight();

            // Get bitmap dimension
            Drawable drawing = getDrawable();
            if (drawing == null) {
                return;
            }
            Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            // Compute scales
            mScaleMax = (float) viewHeight / bitmapHeight;
            mScaleMin = (float) viewWidth / bitmapWidth;

            // First scaling
            Matrix matrix = new Matrix();
            RectF bitmapRect = new RectF(0, 0, bitmapWidth, bitmapHeight);
            RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
            matrix.setRectToRect(bitmapRect, viewRect, Matrix.ScaleToFit.CENTER);
            setImageMatrix(matrix);
            setScaleType(ScaleType.MATRIX);

            mFirstDraw = false;
        }
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
                zoom(motionEvent, mScaleMax);
            }
            else if (mZoomLevel == ZOOM_LEVEL_1) {
                Toast.makeText(getContext(), "Unzoom!", Toast.LENGTH_LONG).show();
                mZoomLevel = ZOOM_LEVEL_0;
                zoom(motionEvent, mScaleMin);
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


    // cf. http://stackoverflow.com/questions/7418955/how-to-animate-zoom-out-with-imageview-that-uses-matrix-scaling
    private void zoom(MotionEvent event, float scale)
    {
        // Matrix
        float[] matrixValues = new float[9];
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);

        // Get current matrix
        mMatrix.set(getImageMatrix());
        mMatrix.getValues(matrixValues);

        // Final variables
        final float startScale = matrixValues[Matrix.MSCALE_X];
        final float targetScale = scale;
        final float finalX;
        final float finalY;

        // Interpolator
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();

        // Time
        final long startTime = System.currentTimeMillis();
        final long duration = 300;

        // Thread
        post(new Runnable() {
            @Override
            public void run() {

                // Increment time
                float t = (float) (System.currentTimeMillis() - startTime) / duration;
                t = t > 1.0f ? 1.0f : t;

                // Compute scale
                float interpolatedRatio = interpolator.getInterpolation(t);
                float tempScale = startScale + interpolatedRatio * (targetScale - startScale);
                mMatrix.reset();

                // translate initialPoint to 0,0 before applying zoom
                //mMatrix.postTranslate(-mDoubleTapImagePoint[0], -mDoubleTapImagePoint[1]);

                // zoom
                mMatrix.postScale(tempScale, tempScale);

                // translate back to equivalent point
                //mMatrix.postTranslate(tempX, tempY);
                setImageMatrix(mMatrix);

                if (t < 1f) {
                    post(this);
                }
            }
        });
    }

}
