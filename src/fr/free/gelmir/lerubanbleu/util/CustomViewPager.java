package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 21/05/13
 * Time: 00:10
 * To change this template use File | Settings | File Templates.
 */
public class CustomViewPager extends ViewPager {

    // Paging
    private boolean mPagingEnabled = true;

    // Gesture
    GestureDetector mGestureDetector;

    public CustomViewPager(Context context) {
        super(context);
        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // Constructor
    private void init(Context context) {

        // Disable paging when image is zoomed
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        if (application.getZoomLevel() == 1) {
            mPagingEnabled = false;
        }

        // Gesture
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d("CustomViewPager", "onInterceptTouchEvent " + Integer.toString(event.getAction()));
        return super.onInterceptTouchEvent(event);

        /*
        if (mPagingEnabled) {
            return super.onInterceptTouchEvent(ev);
        }

        // Do not intercept touch event, will propagate to the children, will be handled in the ViewPager last
        return false;

        // Intercept touch return true, will not propagate to the children, will be handled in onTouchedEvent()
        // return true;
        */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("CustomViewPager", "onTouchEvent " + Integer.toString(event.getAction()));
        return super.onTouchEvent(event);
        /*
        if (mPagingEnabled) {
            return super.onTouchEvent(event);
        }
        return false;
        */
    }


    public void setPaging(boolean b) {
        mPagingEnabled = b;
    }


    // Gesture
    private class MyGestureDetectorListener implements GestureDetector.OnGestureListener , GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Toast.makeText(getContext(), "Intercepted double-tap!?", Toast.LENGTH_LONG).show();
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
