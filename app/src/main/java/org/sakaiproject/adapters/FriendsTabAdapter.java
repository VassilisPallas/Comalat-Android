package org.sakaiproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.sakaiproject.sakai.ChatFriendsFragment;
import org.sakaiproject.sakai.ChatSettingsFragment;
import org.sakaiproject.sakai.EventTabFragment;

/**
 * Created by vspallas on 07/04/16.
 */
public class FriendsTabAdapter extends FragmentStatePagerAdapter {
    private int tabsCount;
    private int pos = 0;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout mSwipeRefreshLayout;

    public FriendsTabAdapter(FragmentManager fm, int tabsCount, org.sakaiproject.customviews.CustomSwipeRefreshLayout mSwipeRefreshLayout) {
        super(fm);
        this.tabsCount = tabsCount;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    @Override
    public Fragment getItem(int position) {
        pos = position;

        Log.i("pos", String.valueOf(position));
        switch (position) {
            case 0:
                return new ChatFriendsFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            case 1:
                return new ChatSettingsFragment();
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
