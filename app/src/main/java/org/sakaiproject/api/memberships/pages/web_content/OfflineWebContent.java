package org.sakaiproject.api.memberships.pages.web_content;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.pojos.web_content.WebContent;
import org.sakaiproject.api.pojos.wiki.Wiki;
import org.sakaiproject.api.sync.WebContentRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 01/03/16.
 */
public class OfflineWebContent {
    Context context;
    Gson gson = new Gson();
    private String siteId;
    private WebContent webContent;
    private WebContentRefreshUI callback;

    public OfflineWebContent(Context context, String siteId, WebContentRefreshUI callback) {
        this.context = context;
        this.siteId = siteId;
        this.callback = callback;
    }

    public void getWebContent() {
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "web_content")) {
            try {
                String w = ActionsHelper.readJsonFile(context, siteId + "_web_content", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "web_content");
                webContent = gson.fromJson(w, WebContent.class);
                callback.updateUI(webContent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
