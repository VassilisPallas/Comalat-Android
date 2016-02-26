package org.sakaiproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.sakaiproject.sakai.AssignmentTabFragment;
import org.sakaiproject.sakai.EventTabFragment;

/**
 * Created by vspallas on 23/02/16.
 */
public class DashboardTabAdapter extends FragmentStatePagerAdapter {
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private int tabsCount;

    public DashboardTabAdapter(FragmentManager fm, int tabsCount, org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        super(fm);
        this.tabsCount = tabsCount;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AssignmentTabFragment().getSwipeRefreshLayout(swipeRefreshLayout);
            case 1:
                return new EventTabFragment().getSwipeRefreshLayout(swipeRefreshLayout);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
