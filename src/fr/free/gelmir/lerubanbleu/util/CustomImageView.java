package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 18/05/13
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
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

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }
    */



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
                //fitToHeight();
                zoomIn();
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


    private void zoomIn()
    {
        // Get the view height
        int viewHeightTemp = this.getHeight();

        // Get the bitmap
        Drawable drawing = getDrawable();
        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int height = bitmap.getHeight();

        AnimationSet zoomAnimation = new AnimationSet(true);
        /*
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, -imageViewXCoord/(mScreenWidth/mImageViewWidth),
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, -imageViewYCoord/(mScreenWidth/mImageViewWidth)
        );
        translateAnimation.setDuration(200);
        */

        //float scale = (float) viewHeightTemp / height;
        //ScaleAnimation scaleAnimation = new ScaleAnimation(1, scale, 1, scale);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 2, 1, 2);
        scaleAnimation.setDuration(200);

        //zoomAnimation.addAnimation(translateAnimation);
        zoomAnimation.addAnimation(scaleAnimation);
        zoomAnimation.setFillAfter(true);
        zoomAnimation.setFillEnabled(true);

        startAnimation(zoomAnimation);

    }

}
