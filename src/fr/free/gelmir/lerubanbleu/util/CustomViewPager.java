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

        // Disable swipe when image is zoomed
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

        // Page change listener
        //setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void setCurrentItem(int item) {
        Log.d("CustomViewPager", "setCurrentItem");
        super.setCurrentItem(item);

        // Apply zoom level
        //setZoomLevel();
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

                    // Boundary has been reached: forward motion event to the superclass
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

            // Set zoom level
            setZoomLevel();
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


    private void setZoomLevel()
    {
        // Get properties
        LeRubanBleuApplication application = LeRubanBleuApplication.getInstance();
        int zoomLevel = application.getZoomLevel();
        int pageLimit;
        int currentItem;
        EpisodeFragmentPagerAdapter adapter;
        int i, j;

        switch (zoomLevel) {
            case 0:
                // Save new zoom level
                application.setZoomLevel(1);

                // Get viewpager properties
                pageLimit = getOffscreenPageLimit();
                currentItem = getCurrentItem();
                adapter = (EpisodeFragmentPagerAdapter) getAdapter();

                // Zoom previous pages
                i = (currentItem - pageLimit) < 0 ? 0 : (currentItem - pageLimit);
                for (; i<currentItem; i++) {
                    EpisodeFragment fragment = adapter.getItem(i);
                    View view = fragment.getView();
                    CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                    imageView.zoom(CustomImageView.ZOOM_LEVEL_1, CustomImageView.ALIGN_RIGHT);
                }

                // Zoom current and next pages
                i = currentItem;
                j = (currentItem + pageLimit + 1) > adapter.getCount() ? (adapter.getCount() - 1) : (currentItem + pageLimit + 1);
                for (; i < j; i++) {
                    EpisodeFragment fragment = adapter.getItem(i);
                    View view = fragment.getView();
                    CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                    imageView.zoom(CustomImageView.ZOOM_LEVEL_1, CustomImageView.ALIGN_LEFT);
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
                i = (currentItem - pageLimit) < 0 ? 0 : (currentItem - pageLimit);
                j = (currentItem + pageLimit + 1) > adapter.getCount() ? (adapter.getCount() - 1) : (currentItem + pageLimit + 1);
                for (; i < j; i++) {
                    EpisodeFragment fragment = adapter.getItem(i);
                    View view = fragment.getView();
                    CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                    imageView.zoom(CustomImageView.ZOOM_LEVEL_0, 0);
                }

                // Enable swipe
                mSwipeEnabled = true;

                break;
        }



    }

    private class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            // Align other pages
            Log.d("CustomViewPager", "MyOnPageChangeListener onPageSelected " + Integer.toString(i));
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

}
