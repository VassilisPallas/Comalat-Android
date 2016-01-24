package org.sakaiproject.helpers.user_navigation_drawer_helpers;


import android.content.Context;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import org.sakaiproject.api.site.SitePage;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.sakai.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 1/18/16.
 */
public class UserMainNavigationDrawerHelper extends NavigationDrawerHelper implements Serializable {

    private NavigationView navigationView;
    private Context context;
    private static List<String> myWorkSpaceItems = new ArrayList<>();

    /**
     * UserMainNavigationDrawerHelper constructor
     *
     * @param context                          the context
     * @param mSwipeRefreshLayout              the swipeRefreshLayout object
     * @param onNavigationItemSelectedListener the listener for the selected item on the drawer
     */
    public UserMainNavigationDrawerHelper(Context context, CustomSwipeRefreshLayout mSwipeRefreshLayout, Toolbar toolbar, NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        super(context, mSwipeRefreshLayout, toolbar);
        this.context = context;

        myWorkSpaceItems.add(context.getResources().getString(R.string.dashboard));
        myWorkSpaceItems.add(context.getResources().getString(R.string.home));
        myWorkSpaceItems.add(context.getResources().getString(R.string.profile));
        myWorkSpaceItems.add(context.getResources().getString(R.string.membership));
        myWorkSpaceItems.add(context.getResources().getString(R.string.calendar));
        myWorkSpaceItems.add(context.getResources().getString(R.string.resources));
        myWorkSpaceItems.add(context.getResources().getString(R.string.announcements));
        myWorkSpaceItems.add(context.getResources().getString(R.string.preferences));
        myWorkSpaceItems.add(context.getResources().getString(R.string.account));

        navigationView = (NavigationView) ((AppCompatActivity) context).findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    public void createDrawer(String title, List<SitePage> list) {

        MenuItem mainItem = navigationView.getMenu().findItem(R.id.main_item);
        SubMenu subMenu = mainItem.getSubMenu();

        mainItem.setTitle(title);

        subMenu.clear();

        pagesIds.clear();
        myWorkspaceIds.clear();

        if (list == null) {
            // make visible the items which are on the My Workspace
            for (String page : myWorkSpaceItems) {
                int iconId = findIcon(page);
                // add new item to menu
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    subMenu.add(R.id.main_group, ++id, Menu.NONE, page).setIcon(context.getResources().getDrawable(iconId, context.getTheme())).setCheckable(true);
                } else {
                    subMenu.add(R.id.main_group, ++id, Menu.NONE, page).setIcon(context.getResources().getDrawable(iconId)).setCheckable(true);
                }
                myWorkspaceIds.put(id, page);
            }
        } else {
            for (SitePage page : list) {
                if (page.getTitle().equals(context.getResources().getString(R.string.comalat_guide))) {
                    subMenu.add(R.id.main_group, ++id, page.getPosition(), page.getTitle()).setCheckable(true);
                } else {
                    int iconId = findIcon(page.getTitle());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        subMenu.add(R.id.main_group, ++id, page.getPosition(), page.getTitle()).setIcon(context.getResources().getDrawable(iconId, context.getTheme())).setCheckable(true);
                    } else {
                        subMenu.add(R.id.main_group, ++id, page.getPosition(), page.getTitle()).setIcon(context.getResources().getDrawable(iconId)).setCheckable(true);
                    }
                }
                pagesIds.put(id, page);
            }
        }

        // add new item to menu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subMenu.add(R.id.main_group, R.id.help, subMenu.size(), context.getResources().getString(R.string.help)).setIcon(context.getResources().getDrawable(R.mipmap.ic_help, context.getTheme())).setCheckable(true);
        } else {
            subMenu.add(R.id.main_group, R.id.help, subMenu.size(), context.getResources().getString(R.string.help)).setIcon(context.getResources().getDrawable(R.mipmap.ic_help)).setCheckable(true);
        }


//        for (Integer ii : myWorkspaceIds.keySet()) {
//            Log.i("myWorkspace", myWorkspaceIds.get(ii) + " has " + String.valueOf(ii));
//        }
//
//        for (Integer ii : pagesIds.keySet()) {
//            Log.i("pages", pagesIds.get(ii).getTitle() + " has " + String.valueOf(ii));
//        }
//
//        for (Integer ii : sitesIds.keySet()) {
//            Log.i("sites and projects", sitesIds.get(ii).getTitle() + " has " + String.valueOf(ii));
//        }
    }

}