package org.sakaiproject.api.site.actions;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sakaiproject.api.sync.MembershipRefresh;
import org.sakaiproject.customviews.adapters.MembershipAdapter;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/24/16.
 */
public class UnJoinAsync extends AsyncTask<Void, Void, Boolean> {

    private String siteId;
    private Context context;
    private ProgressBar refreshProgressBar;
    private MembershipAdapter mAdapter;
    private int position;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout customSwipeRefreshLayout;

    public UnJoinAsync(String siteId, Context context, ProgressBar progressBar, MembershipAdapter mAdapter, int position, org.sakaiproject.customviews.CustomSwipeRefreshLayout customSwipeRefreshLayout) {
        this.refreshProgressBar = progressBar;
        this.mAdapter = mAdapter;
        this.context = context;
        this.siteId = siteId;
        this.position = position;
        this.customSwipeRefreshLayout = customSwipeRefreshLayout;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        refreshProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return SiteUnJoin.unJoin(context.getString(R.string.url) + "/membership/unjoin/site/" + siteId);
    }

    @Override
    protected void onPostExecute(Boolean unjoin) {
        super.onPostExecute(unjoin);
        MembershipRefresh membershipRefresh = new MembershipRefresh(context);
        membershipRefresh.setSwipeRefreshLayout(customSwipeRefreshLayout);
        membershipRefresh.execute();

        mAdapter.notifyItemRemoved(position);
        if (unjoin) {
            Toast.makeText(context, context.getResources().getString(R.string.successful_unjoined), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
        refreshProgressBar.setVisibility(View.GONE);
    }
}