package org.sakaiproject.customviews.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by vasilis on 12/13/15.
 */
public class AboutScrollView extends ScrollView {
    private boolean datePickerIsVisible = false, scrollable;

    public AboutScrollView(Context context) {
        super(context);
    }

    public AboutScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AboutScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDatePickerIsVisible(boolean datePickerIsVisible) {
        this.datePickerIsVisible = datePickerIsVisible;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (isScrollable() && datePickerIsVisible) {
                    return super.onTouchEvent(motionEvent);
                }
                // only continue to handle the touch event if scrolling enabled
                return scrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(motionEvent);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!isScrollable())
            return false;
        else
            return super.onInterceptTouchEvent(ev);
    }
}
