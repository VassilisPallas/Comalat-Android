package org.sakaiproject.api.memberships.pages.wiki;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.wiki.Wiki;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 01/03/16.
 */
public class MembershipOfflineWiki {
    private Callback callback;
    private String siteId;
    Context context;
    Gson gson = new Gson();
    private Wiki wiki;

    public MembershipOfflineWiki(Callback callback, Context context, String siteId) {
        this.callback = callback;
        this.context = context;
        this.siteId = siteId;
    }

    public void getWiki() {
        if (siteId != null) {
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki")) {
                try {
                    String w = ActionsHelper.readJsonFile(context, siteId + "_wiki", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki");
                    wiki = gson.fromJson(w, Wiki.class);
                    getPageData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "wiki")) {
                try {
                    String w = ActionsHelper.readJsonFile(context, "wiki", User.getUserEid() + File.separator + "wiki");
                    wiki = gson.fromJson(w, Wiki.class);
                    getPageData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getPageData() {
        if (siteId != null) {
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki")) {
                try {
                    String w = ActionsHelper.readJsonFile(context, siteId + "_wiki_data", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki");
                    Wiki temp = gson.fromJson(w, Wiki.class);
                    wiki.setComments(temp.getComments());
                    wiki.setHtml(temp.getHtml());

                    callback.onSuccess(wiki);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "wiki")) {
                try {
                    String w = ActionsHelper.readJsonFile(context, "wiki_data", User.getUserEid() + File.separator + "wiki");
                    Wiki temp = gson.fromJson(w, Wiki.class);
                    wiki.setComments(temp.getComments());
                    wiki.setHtml(temp.getHtml());

                    callback.onSuccess(wiki);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
