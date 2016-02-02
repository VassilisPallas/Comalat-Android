package org.sakaiproject.api.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.sakaiproject.api.site.OnlineSite;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.customviews.adapters.MembershipAdapter;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 1/25/16.
 */
public class MembershipRefresh extends AsyncTask<Void, Void, Void> {

    private Context context;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MembershipAdapter mAdapter;

    public MembershipRefresh(Context context) {
        this.context = context;
    }

    public void setSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public void setmAdapter(MembershipAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }


    @Override
    protected Void doInBackground(Void... params) {
        SiteData.getSites().clear();
        SiteData.getProjects().clear();
        OnlineSite onlineSite = new OnlineSite(context);
        try {
            onlineSite.getSites(context.getString(R.string.url) + "membership.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        UserActivity.getSitesNavigationDrawer().fillSitesDrawer();
        if (mAdapter != null && mRecyclerView != null) {
            List<SiteData> temp = new ArrayList<>(SiteData.getSites());
            temp.addAll(SiteData.getProjects());
            mAdapter.setMemberships(temp);
            mRecyclerView.setAdapter(mAdapter);
        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
