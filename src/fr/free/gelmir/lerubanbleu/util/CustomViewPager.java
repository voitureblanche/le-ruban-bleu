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


public class CustomViewPager extends ViewPager {

    // Viewpager states
    private final int FULL_PAGING = 0;
    private final int PARTIAL_PAGING = 1;
    private final int IMAGE_DRAGGING = 2;
    private int mViewPagerState = FULL_PAGING;

    // Image drag
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
            mViewPagerState = IMAGE_DRAGGING;
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
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("CustomViewPager", "onTouchEvent " + Integer.toString(event.getAction()));

        switch (mViewPagerState) {

            // Full paging
            case FULL_PAGING:
                return super.onTouchEvent(event);

            // Image scrolling
            case IMAGE_DRAGGING:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.i("CustomViewPager", "onTouchEvent ACTION_DOWN");
                        mDragStartX = event.getRawX();
                        getCurrentItem();
                        return super.onTouchEvent(event);

                    case MotionEvent.ACTION_MOVE:
                        //Log.i("CustomViewPager", "onTouchEvent ACTION_MOVE");

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

                        // Boundary has been reached:
                        // - forward motion event to the superclass
                        // - change Viewpager state
                        if (boundary) {
                            Log.d("CustomViewPager", "onTouchEvent boundary reached!");
                            mViewPagerState = PARTIAL_PAGING;
                            return super.onTouchEvent(event);
                        }
                        return true;


                    case MotionEvent.ACTION_UP:
                        mDragStartX = 0;
                        return super.onTouchEvent(event);

                    default:
                        return true;
                }

            case PARTIAL_PAGING:
                return super.onTouchEvent(event);

            default:
                return super.onTouchEvent(event);
        }
    }


    // Called by the activity
    public void onPageScrolled(int i, float v, int i2) {
        if (mViewPagerState == PARTIAL_PAGING) {

        }
    }

    // Called by the activity
    public void onPageScrollStateChanged(int i) {
        if (mViewPagerState == PARTIAL_PAGING) {
            if (i == SCROLL_STATE_SETTLING) {
                // Revert to previous state
                mViewPagerState = IMAGE_DRAGGING;
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
                    if (fragment != null) {
                        View view = fragment.getView();
                        if (view != null) {
                            CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                            imageView.zoom(CustomImageView.ZOOM_LEVEL_1, CustomImageView.ALIGN_RIGHT, true);
                        }
                    }
                }

                // Zoom current and next pages
                i = currentItem;
                j = (currentItem + pageLimit + 1) > adapter.getCount() ? (adapter.getCount() - 1) : (currentItem + pageLimit + 1);
                for (; i < j; i++) {
                    EpisodeFragment fragment = adapter.getItem(i);
                    if (fragment != null) {
                        View view = fragment.getView();
                        if (view != null) {
                            CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                            imageView.zoom(CustomImageView.ZOOM_LEVEL_1, CustomImageView.ALIGN_LEFT, true);
                        }
                    }
                }

                // Change state
                mViewPagerState = IMAGE_DRAGGING;

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
                    if (fragment != null) {
                        View view = fragment.getView();
                        if (view != null) {
                            CustomImageView imageView = (CustomImageView) view.findViewById(R.id.episodeCustomImageView);
                            imageView.zoom(CustomImageView.ZOOM_LEVEL_0, 0, true);
                        }
                    }
                }

                // Change state
                mViewPagerState = FULL_PAGING;

                break;
        }
    }

}
