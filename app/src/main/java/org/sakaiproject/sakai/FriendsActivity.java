package org.sakaiproject.sakai;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.sakaiproject.helpers.ActionsHelper;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Drawable person = ActionsHelper.setCustomDrawableColor(this, R.mipmap.ic_group, Color.WHITE);
        Drawable settings = ActionsHelper.setCustomDrawableColor(this, R.mipmap.ic_settings, Color.WHITE);

        tabLayout.addTab(tabLayout.newTab().setIcon(person));

        tabLayout.addTab(tabLayout.newTab().setIcon(settings));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
        } else {
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        //viewPager.setAdapter(adapter);
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

    }

}
