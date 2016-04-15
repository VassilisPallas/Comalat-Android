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
    private int tabsCount;
    private int pos = 0;

    public DashboardTabAdapter(FragmentManager fm, int tabsCount) {
        super(fm);
        this.tabsCount = tabsCount;
    }

    @Override
    public Fragment getItem(int position) {
        pos = position;
        switch (position) {
            case 0:
                return new AssignmentTabFragment();
            case 1:
                return new EventTabFragment();
            default:
                return null;
        }
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
