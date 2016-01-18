package org.sakaiproject.helpers.user_navigation_drawer_helpers;


import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/18/16.
 */
public class SitesNavigationDrawerHelper extends NavigationDrawerHelper {

    private NavigationView sitesNavigationView;
    private SearchView sitesSearchView;
    private Context context;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    public SitesNavigationDrawerHelper(Context context, CustomSwipeRefreshLayout mSwipeRefreshLayout, NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        super(context, mSwipeRefreshLayout);
        this.context = context;
        this.onNavigationItemSelectedListener = onNavigationItemSelectedListener;
        initialize();
        initializeSearch();
    }


    private void initialize() {

        sitesNavigationView = (NavigationView) ((AppCompatActivity) context).findViewById(R.id.site_nav_view);

        sitesNavigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        sitesSearchView = (SearchView) ((AppCompatActivity) context).findViewById(R.id.site_search_drawer);
    }

    private void initializeSearch() {


        final EditText e = (EditText) sitesSearchView.findViewById(sitesSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        e.setHintTextColor(Color.parseColor("#808080"));

        ((AppCompatActivity) context).findViewById(R.id.sites_drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open sites drawer
                drawer.openDrawer(GravityCompat.END);
            }
        });

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

    public void fillSitesDrawer() {

        sitesIds.clear();

        MenuItem sitesItem = sitesNavigationView.getMenu().findItem(R.id.sites);
        SubMenu sitesSubMenu = sitesItem.getSubMenu();

        MenuItem projectsItem = sitesNavigationView.getMenu().findItem(R.id.projects);
        SubMenu projectsSubMenu = projectsItem.getSubMenu();


        sitesSubMenu.clear();
        projectsSubMenu.clear();

        sitesSubMenu.add(R.id.sites, R.id.my_workspace, Menu.NONE, "My Workspace").setCheckable(true).setChecked(true);

        for (int i = 0; i < SiteData.getSites().size(); i++) {
            sitesSubMenu.add(R.id.sites, ++id, Menu.NONE, SiteData.getSites().get(i).getTitle()).setCheckable(true);
            sitesIds.put(id, SiteData.getSites().get(i));
        }

        for (int i = 0; i < SiteData.getProjects().size(); i++) {
            projectsSubMenu.add(R.id.projects, ++id, Menu.NONE, SiteData.getProjects().get(i).getTitle()).setCheckable(true);
            sitesIds.put(id, SiteData.getProjects().get(i));
        }

    }

}