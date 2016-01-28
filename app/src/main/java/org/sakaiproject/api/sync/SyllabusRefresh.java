package org.sakaiproject.api.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.sakaiproject.api.pages.syllabus.OfflineSyllabus;
import org.sakaiproject.api.pages.syllabus.OnlineSyllabus;
import org.sakaiproject.api.pages.syllabus.Syllabus;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.customviews.adapters.SyllabusAdapter;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UpdateSyllabus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 1/28/16.
 */
public class SyllabusRefresh extends AsyncTask<Void, Void, Void> {

    private Context context;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private String siteId;
    private RecyclerView mRecyclerView;
    private SyllabusAdapter mAdapter;
    private UpdateSyllabus callback;

    public SyllabusRefresh(Context context) {
        this.context = context;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public void setmAdapter(SyllabusAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setCallback(UpdateSyllabus callback) {
        this.callback = callback;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        OnlineSyllabus onlineSyllabus = new OnlineSyllabus(context, siteId);
        try {
            onlineSyllabus.getSyllabus(context.getString(R.string.url) + "syllabus/site/" + siteId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        OfflineSyllabus offlineSyllabus = new OfflineSyllabus(context, siteId);
        Syllabus syllabus = null;
        try {
            syllabus = offlineSyllabus.getSyllabus();
            if (syllabus.getItems().size() > 0) {
                callback.update(mRecyclerView, mAdapter, syllabus, siteId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setRefreshing(false);
    }
}
