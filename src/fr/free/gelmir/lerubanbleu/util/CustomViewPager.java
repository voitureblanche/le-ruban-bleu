package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 21/05/13
 * Time: 00:10
 * To change this template use File | Settings | File Templates.
 */
public class CustomViewPager extends ViewPager {

    static final int ZOOM_LEVEL_0 = 0;
    static final int ZOOM_LEVEL_1 = 1;
    private int mZoomLevel = ZOOM_LEVEL_0;

    MyGestureDetectorListener mGestureDetectorListener;
    GestureDetector mGestureDetector;


    public CustomViewPager(Context context) {
        super(context);
        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mGestureDetectorListener = new MyGestureDetectorListener();
        mGestureDetector = new GestureDetector(getContext(), mGestureDetectorListener);
        mGestureDetector.setOnDoubleTapListener(mGestureDetectorListener);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.d("CustomViewPager", "onInterceptTouchEvent");
        //boolean result = mGestureDetector.onTouchEvent(ev);
        //return super.onInterceptTouchEvent(ev);

        // Do not intercept touch event, will propagate to the children, will be handled in the ViewPager last
        return false;

        // Intercept touch event, will not be propagated to the children, will be handled in the
        //return true;
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("CustomViewPager", "onTouchEvent");
        return super.onTouchEvent(event);
    }
    */

    // Gesture
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

                // TODO zoom fragments



            }
            else if (mZoomLevel == ZOOM_LEVEL_1) {
                Toast.makeText(getContext(), "Unzoom!", Toast.LENGTH_LONG).show();
                mZoomLevel = ZOOM_LEVEL_0;

                // TODO unzoom fragments
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
                Log.d("CustomViewPager", "onScroll " + Float.toString(v) + " " + Float.toString(v2));
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
