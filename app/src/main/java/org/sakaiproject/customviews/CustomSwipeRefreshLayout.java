package org.sakaiproject.customviews;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import java.io.Serializable;

/**
 * Created by vasilis on 11/11/15.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout implements Serializable {
    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
