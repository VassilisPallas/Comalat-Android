package org.sakaiproject.helpers.user_navigation_drawer_helpers;


import android.content.Context;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.sakai.R;

import java.lang.reflect.Field;

/**
 * Created by vasilis on 1/18/16.
 */
public class SitesNavigationDrawerHelper extends NavigationDrawerHelper {

    private NavigationView sitesNavigationView;
    private SearchView sitesSearchView;
    private Context context;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    /**
     * SitesNavigationDrawerHelper constructor
     *
     * @param context                          the context
     * @param mSwipeRefreshLayout              the swipeRefreshLayout object
     * @param onNavigationItemSelectedListener the listener for the selected item on the drawer
     */
    public SitesNavigationDrawerHelper(Context context, CustomSwipeRefreshLayout mSwipeRefreshLayout, Toolbar toolbar, NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        super(context, mSwipeRefreshLayout, toolbar);
        this.context = context;
        this.onNavigationItemSelectedListener = onNavigationItemSelectedListener;
        initialize();
        initializeSearch();
    }

    public void unCheckFirstItem() {
        if (sitesNavigationView.getMenu().getItem(0).getSubMenu().getItem(0).isChecked()) {
            sitesNavigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
        }
    }

    private void initialize() {

        sitesNavigationView = (NavigationView) ((AppCompatActivity) context).findViewById(R.id.site_nav_view);

        sitesNavigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        sitesSearchView = (SearchView) ((AppCompatActivity) context).findViewById(R.id.site_search_drawer);
    }

    private void initializeSearch() {


        final EditText e = (EditText) sitesSearchView.findViewById(sitesSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            e.setHintTextColor(context.getResources().getColor(R.color.hint, context.getTheme()));
        } else {
            e.setHintTextColor(context.getResources().getColor(R.color.hint));
        }


        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(e, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception ex) {
        }

        e.addTextChangedListener(new TextWatcher() {
            MenuItem sitesItem = sitesNavigationView.getMenu().findItem(R.id.sites);
            SubMenu sitesSubMenu = sitesItem.getSubMenu();


            MenuItem projectsItem = sitesNavigationView.getMenu().findItem(R.id.projects);
            SubMenu projectsSubMenu = projectsItem.getSubMenu();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int c) {

                if (e.getText().toString().length() == 0) {
                    for (int i = 0; i < sitesSubMenu.size(); i++) {
                        sitesSubMenu.getItem(i).setVisible(true);
                    }

                    for (int i = 0; i < projectsSubMenu.size(); i++) {
                        projectsSubMenu.getItem(i).setVisible(true);

                    }
                } else {
                    for (int i = 0; i < sitesSubMenu.size(); i++) {
                        if (!sitesSubMenu.getItem(i).getTitle().toString().toLowerCase().contains(s.toString().toLowerCase())) {
                            sitesSubMenu.getItem(i).setVisible(false);
                        } else {
                            sitesSubMenu.getItem(i).setVisible(true);
                        }
                    }

                    for (int i = 0; i < projectsSubMenu.size(); i++) {
                        if (!projectsSubMenu.getItem(i).getTitle().toString().toLowerCase().contains(s.toString().toLowerCase())) {
                            projectsSubMenu.getItem(i).setVisible(false);
                        } else {
                            projectsSubMenu.getItem(i).setVisible(true);
                        }
                    }
                }

                for (int i = 0, count = sitesNavigationView.getChildCount(); i < count; i++) {
                    final View child = sitesNavigationView.getChildAt(i);
                    if (child != null && child instanceof ListView) {
                        final ListView menuView = (ListView) child;
                        final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                        final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                        wrapped.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void fillSitesDrawer(boolean login) {

        sitesIds.clear();

        MenuItem sitesItem = sitesNavigationView.getMenu().findItem(R.id.sites);
        SubMenu sitesSubMenu = sitesItem.getSubMenu();

        MenuItem projectsItem = sitesNavigationView.getMenu().findItem(R.id.projects);
        SubMenu projectsSubMenu = projectsItem.getSubMenu();


        sitesSubMenu.clear();
        projectsSubMenu.clear();

        for (int i = 0; i < SiteData.getSites().size(); i++) {
            sitesSubMenu.add(R.id.sites, ++id, Menu.NONE, SiteData.getSites().get(i).getTitle()).setCheckable(true);
            sitesIds.put(id, SiteData.getSites().get(i));

            if (i == 0)
                if (login) {
                    sitesSubMenu.performIdentifierAction(id, 0);
                    sitesSubMenu.getItem(0).setChecked(true);
                    NavigationDrawerHelper.doAction(SiteData.getSites().get(i).getPages().get(0).getTitle());
                }
        }

        for (int i = 0; i < SiteData.getProjects().size(); i++) {
            projectsSubMenu.add(R.id.projects, ++id, Menu.NONE, SiteData.getProjects().get(i).getTitle()).setCheckable(true);
            sitesIds.put(id, SiteData.getProjects().get(i));
        }
    }

}