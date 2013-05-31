package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import fr.free.gelmir.lerubanbleu.LeRubanBleuApplication;
import fr.free.gelmir.lerubanbleu.R;
import fr.free.gelmir.lerubanbleu.fragment.EpisodeFragment;
import fr.free.gelmir.lerubanbleu.fragment.EpisodeFragmentPagerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 21/05/13
 * Time: 00:10
 * To change this template use File | Settings | File Templates.
 */
public class CustomViewPager extends ViewPager {

    // Swipe, drag management
    private boolean mSwipeEnabled = true;
    private float mDragStartX;

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
            mSwipeEnabled = false;
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
        if (mSwipeEnabled) {
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

        // Swipe
        if (mSwipeEnabled) {
            return super.onTouchEvent(event);
        }

        // Drag
        else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("CustomViewPager", "onTouchEvent ACTION_DOWN");
                    mDragStartX = event.getRawX();
                    return super.onTouchEvent(event);

                case MotionEvent.ACTION_MOVE:
                    Log.i("CustomViewPager", "onTouchEvent ACTION_MOVE");

                    // Get view
                    EpisodeFragmentPagerAdapter adapter = (EpisodeFragmentPagerAdapter) getAdapter();
                    EpisodeFragment fragment = adapter.getItem(getCurrentItem());
                    View view = fragment.getView();
                    CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);

                    // Scroll
                    float endX = event.getRawX();
                    float distanceX = endX - mDragStartX;
                    mDragStartX = endX;
                    boolean boundary = imageView.horizontalScroll(distanceX);

                    // Boundary has been reached: re-enable page swiping
                    if (boundary) {
                        return super.onTouchEvent(event);
                    }
                    return true;

                default:
                    return super.onTouchEvent(event);
            }
        }
    }

    // Gesture
    private class MyGestureDetectorListener implements GestureDetector.OnGestureListener , GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            //Log.d("CustomViewPager", "MyGestureDetectorListener onDoubleTap");
            LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
            int zoomLevel = application.getZoomLevel();
            int pageLimit;
            int currentItem;
            EpisodeFragmentPagerAdapter adapter;
            switch (zoomLevel) {
                case 0:
                    // Save new zoom level
                    application.setZoomLevel(1);

                    // Get viewpager properties
                    pageLimit = getOffscreenPageLimit();
                    currentItem = getCurrentItem();
                    adapter = (EpisodeFragmentPagerAdapter) getAdapter();

                    // Zoom previous pages
                    for (int i=currentItem-pageLimit; i<currentItem; i++) {
                        EpisodeFragment fragment = adapter.getItem(i);
                        View view = fragment.getView();
                        CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                        imageView.zoom(CustomImageView.ZOOM_LEVEL_1);
                    }

                    // Zoom current and next pages
                    for (int i=currentItem; i<currentItem+pageLimit+1; i++) {
                        EpisodeFragment fragment = adapter.getItem(i);
                        View view = fragment.getView();
                        CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                        imageView.zoom(CustomImageView.ZOOM_LEVEL_1);
                    }

                    // Disable swipe
                    mSwipeEnabled = false;

                    break;

                case 1:
                    // Save new zoom level
                    application.setZoomLevel(0);

                    // Get viewpager properties
                    pageLimit = getOffscreenPageLimit();
                    currentItem = getCurrentItem();
                    adapter = (EpisodeFragmentPagerAdapter) getAdapter();

                    // Unzoom all pages
                    for (int i=currentItem-pageLimit; i<currentItem+pageLimit+1; i++) {
                        EpisodeFragment fragment = adapter.getItem(i);
                        View view = fragment.getView();
                        CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                        imageView.zoom(CustomImageView.ZOOM_LEVEL_0);
                    }

                    // Enable swipe
                    mSwipeEnabled = true;

                    break;
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
