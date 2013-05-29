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


public class CustomImageView extends ImageView {

    // Gesture
    GestureDetector mGestureDetector;

    private int mZoomLevel;
    static final int ZOOM_LEVEL_0 = 0;
    static final int ZOOM_LEVEL_1 = 1;

    // Zoom
    private float mScaleMin = 0;
    private float mScaleMax = 0;
    private float mInitPointX = 0;
    private float mInitPointY = 0;
    private boolean mFirstScale = true;

    // Drag
    private boolean mDrag = false;
    private float mDragStartX;

    // Rectangles
    RectF mBitmapRect;
    RectF mViewRect;

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

        // Gesture management
        MyGestureDetectorListener gestureDetectorListener = new MyGestureDetectorListener();
        mGestureDetector = new GestureDetector(getContext(), gestureDetectorListener);
        mGestureDetector.setOnDoubleTapListener(gestureDetectorListener);
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

        if (mFirstScale) {
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

            // TODO consider zoom status

            // First scaling
            Matrix matrix = new Matrix();
            float[] matrixValues = new float[9];
            mBitmapRect = new RectF(0, 0, bitmapWidth, bitmapHeight);
            mViewRect = new RectF(0, 0, viewWidth, viewHeight);
            matrix.setRectToRect(mBitmapRect, mViewRect, Matrix.ScaleToFit.CENTER);
            matrix.getValues(matrixValues);
            setImageMatrix(matrix);
            setScaleType(ScaleType.MATRIX);

            // Init point
            mInitPointX = matrixValues[Matrix.MTRANS_X];
            mInitPointY = matrixValues[Matrix.MTRANS_Y];

            // First scale done
            mFirstScale = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("CustomImageView", "onTouchEvent");
        //return super.onTouchEvent(event);

        if (mDrag)
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("CustomImageView", "onTouchEvent ACTION_DOWN");
                    mDragStartX = event.getRawX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.i("CustomImageView", "onTouchEvent ACTION_MOVE");
                    float endX = event.getRawX();
                    float distanceX = endX - mDragStartX;
                    mDragStartX = endX;
                    horizontalScroll(distanceX);
                    break;
            }
            return true;
        }

        return super.onTouchEvent(event);
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
                //Toast.makeText(getContext(), "Zoom!", Toast.LENGTH_LONG).show();
                mZoomLevel = ZOOM_LEVEL_1;
                mDrag = true;
                zoom(motionEvent, mScaleMax);

                // TODO save zoom status
            }
            else if (mZoomLevel == ZOOM_LEVEL_1) {
                //Toast.makeText(getContext(), "Unzoom!", Toast.LENGTH_LONG).show();
                mZoomLevel = ZOOM_LEVEL_0;
                mDrag = false;
                zoom(motionEvent, mScaleMin);

                // TODO save zoom status
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
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float distanceX, float distanceY) {
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
        final Matrix matrix = new Matrix();
        float[] matrixValues = new float[9];
        matrix.set(getImageMatrix());
        matrix.getValues(matrixValues);

        // Scaling variables
        final float startScale = matrixValues[Matrix.MSCALE_X];
        final float endScale = scale;
        final float startX;
        final float startY;
        final float endX;
        final float endY;

        // Zoom
        if (startScale < endScale) {
            startX = matrixValues[Matrix.MTRANS_X];
            startY = matrixValues[Matrix.MTRANS_Y];
            // Always zoom to the origin
            endX = 0;
            endY = 0;
        }
        // Unzoom
        else {
            startX = matrixValues[Matrix.MTRANS_X];
            startY = matrixValues[Matrix.MTRANS_Y];
            endX = mInitPointX;
            endY = mInitPointY;
        }
        // Log.i("zoom", "translate from (" + Float.toString(startX) + "," + Float.toString(startY) + ") to (" + Float.toString(endX) + "," + Float.toString(endY) + ")" );

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
                //matrix.postTranslate(0, 0); // apply zoom on (0, 0)
                matrix.postScale(tempScale, tempScale);
                matrix.postTranslate(tempX, tempY);
                setImageMatrix(matrix);

                if (t < 1f) {
                    post(this);
                }
            }
        });
    }

    //
    private void horizontalScroll(float distanceX)
    {
        // Matrix
        Matrix matrix = new Matrix();
        float[] matrixValues = new float[9];
        matrix.set(getImageMatrix());
        matrix.getValues(matrixValues);

        // Get current scaling variables
        float startX = matrixValues[Matrix.MTRANS_X];
        float scale = matrixValues[Matrix.MSCALE_X];

        // Compute boundaries
        float minX = - ((mBitmapRect.right * scale) - mViewRect.right);
        float maxX = 0;

        // Compute translation
        float translateX;
        float endX = startX + distanceX;
        if (endX < minX) {
            translateX = minX - startX;
        }
        else if (endX > 0) {
            translateX = -startX;
        }
        else {
            translateX = distanceX;
        }

        // TODO detect boundaries touch

        // Apply matrix
        matrix.postTranslate(translateX, 0);
        setImageMatrix(matrix);
    }

}
