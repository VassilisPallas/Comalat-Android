package org.sakaiproject.sakai;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import org.sakaiproject.adapters.FriendsTabAdapter;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;

public class ChatFriendsActivity extends AppCompatActivity implements ISwipeRefresh {

    private FriendsTabAdapter adapter;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friends);

        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.blue, R.color.orange);

        mSwipeRefreshLayout.setRefreshing(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Drawable person = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            person = getResources().getDrawable(R.mipmap.ic_group_white, getTheme());
        } else {
            person = getResources().getDrawable(R.mipmap.ic_group_white);
        }


        Drawable settings = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            settings = getResources().getDrawable(R.mipmap.ic_settings_white, getTheme());
        } else {
            settings = getResources().getDrawable(R.mipmap.ic_settings_white);
        }

        tabLayout.addTab(tabLayout.newTab().setIcon(person));

        tabLayout.addTab(tabLayout.newTab().setIcon(settings));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
        } else {
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new FriendsTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), mSwipeRefreshLayout);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSwipeRefreshLayout.setEnabled(false);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void Callback(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }
}
