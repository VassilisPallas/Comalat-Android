package org.sakaiproject.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by vasilis on 12/5/15.
 * Custom ScrollView for checking if the ScrollView is on the top or on the bottom
 */
public class ProfileScrollView extends ScrollView {

    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public ProfileScrollView(Context context) {
        super(context);
    }

    public ProfileScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d <= 0) {
            // bottom
        } else if (getScrollY() <= 0) {
            // top
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

}