package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;


// cf. http://stackoverflow.com/questions/7418955/how-to-animate-zoom-out-with-imageview-that-uses-matrix-scaling
public class CustomImageView extends ImageView {

    private MyGestureDetectorListener mGestureDetectorListener;
    private GestureDetector mGestureDetector;

    private int mZoomLevel;
    static final int ZOOM_LEVEL_0 = 0;
    static final int ZOOM_LEVEL_1 = 1;


    public CustomImageView(Context context) {
        super(context);
        construct(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct(context);
    }

    // Constructor
    private void construct(Context context)
    {
        super.setClickable(true);

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

    // cf. http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // Get display size
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int desiredWidth = display.getWidth();  // deprecated
        int desiredHeight = display.getHeight();  // deprecated

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            // Be whatever you want
            width = desiredWidth;
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            // Be whatever you want
            height = desiredHeight;
        }

        // MUST CALL THIS
        setMeasuredDimension(width, height);
    }
    */


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
                setScaleType(ScaleType.CENTER);
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
}
