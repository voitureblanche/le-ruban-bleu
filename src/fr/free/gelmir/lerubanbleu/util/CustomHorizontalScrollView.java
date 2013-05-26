package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 26/05/13
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Constructor
    private void construct(Context context)
    {
        super.setClickable(true);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);
        return false;
    }

}
