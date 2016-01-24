package org.sakaiproject.api.sync;

import android.content.Context;
import android.os.AsyncTask;

import org.sakaiproject.api.site.OnlineSite;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

import java.io.IOException;

/**
 * Created by vasilis on 1/24/16.
 */
public class MembershipRefresh extends AsyncTask<Void, Void, Void> {

    private Context context;

    public MembershipRefresh(Context context) {
        this.context = context;
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
    }
}
