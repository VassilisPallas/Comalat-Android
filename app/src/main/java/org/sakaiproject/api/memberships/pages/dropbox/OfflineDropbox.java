package org.sakaiproject.api.memberships.pages.dropbox;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.dropbox.Dropbox;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 10/03/16.
 */
public class OfflineDropbox {
    private Context context;
    private SiteData siteData;
    private Gson gson = new Gson();
    private Map<String, Integer> dropboxList = new HashMap<>();
    private Callback callback;
    private Roster roster;

    public OfflineDropbox(Context context, SiteData siteData, Callback callback, Roster roster) {
        this.context = context;
        this.siteData = siteData;
        this.callback = callback;
        this.roster = roster;
    }

    public void getDropboxItems() {
        for (int i = 0; i < roster.getMembersTotal(); i++) {
            Member member = roster.getMembers().get(i);

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox")) {
                try {
                    String json = ActionsHelper.readJsonFile(context, member.getUserId() + "_dropbox", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox");
                    Dropbox dropbox = gson.fromJson(json, Dropbox.class);

                    for (int ii = 0; ii < dropbox.getCollection().size(); ii++) {
                        String url = dropbox.getCollection().get(ii).getUrl();
                        File externalStoragePath = context.getFilesDir();
                        String tempUrl = (context.getResources().getString(R.string.url).replaceFirst("direct", "access")) + "content/group-user/" + siteData.getId();
                        url = url.replaceFirst(tempUrl, "");
                        url = url.replaceAll(member.getUserId(), member.getEid());
                        url = User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox" + File.separator + "files" + url;
                        File f = new File(externalStoragePath, url);
                        if (!f.exists()) {
                            try {
                                f.getParentFile().mkdirs();
                                f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        dropboxList.put(f.getAbsolutePath(), dropbox.getCollection().get(ii).getSize());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (i == roster.getMembersTotal() - 1) {
                callback.onSuccess(dropboxList);
            }
        }
    }

}
