package org.sakaiproject.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.sakaiproject.sakai.HomeAnnouncementsTabFragment;
import org.sakaiproject.sakai.HomeEventsTabFragment;
import org.sakaiproject.sakai.HomeInformationTabFragment;
import org.sakaiproject.sakai.HomeMessagesTabFragment;
import org.sakaiproject.sakai.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vassilis on 4/13/16.
 */
public class HomeTabAdapter extends FragmentStatePagerAdapter {
    private int tabsCount;
    private int pos = 0;
    private final String information, events, announcements, messages;
    private Map<Integer, String> positions;

    public HomeTabAdapter(FragmentManager fm, int tabsCount, Context context, Map<Integer, String> positions) {
        super(fm);
        this.tabsCount = tabsCount;
        information = context.getString(R.string.information);
        events = context.getString(R.string.events);
        announcements = context.getString(R.string.announcements);
        messages = context.getString(R.string.messages);
        this.positions = positions;
    }

    @Override
    public Fragment getItem(int position) {
        pos = position;

        return getFragment(positions.get(position));
    }

    private Fragment getFragment(String title) {

        if (title.equals(information)) {
            return new HomeInformationTabFragment();
        } else if (title.equals(events)) {
            return new HomeEventsTabFragment();
        } else if (title.equals(announcements)) {
            return new HomeAnnouncementsTabFragment();
        } else if (title.equals(messages)) {
            return new HomeMessagesTabFragment();
        }
        return null;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
