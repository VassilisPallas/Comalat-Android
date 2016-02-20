package org.sakaiproject.api.memberships.pages.syllabus;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.sakaiproject.api.sync.SyllabusRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Actions;
import org.sakaiproject.sakai.AppController;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 1/28/16.
 */
public class SyllabusService {

    private Context context;
    private String siteId;
    private final String syllabus_tag;
    SyllabusRefreshUI delegate;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    /**
     * OnlineSyllabus constructor
     *
     * @param context the context
     */
    public SyllabusService(Context context, String siteId, SyllabusRefreshUI delegate) {
        this.context = context;
        this.siteId = siteId;
        this.delegate = delegate;
        syllabus_tag = User.getUserEid() + " " + siteId + " syllabus";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getSyllabus(String url) throws IOException {
        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest syllabusRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus"))
                    try {
                        Actions.writeJsonFile(context, response.toString(), siteId + "_syllabus", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                delegate.updateUI();

                if(swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(syllabus_tag, error.getMessage());
                if(swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });

        AppController.getInstance().addToRequestQueue(syllabusRequest, syllabus_tag);

//        connection.openConnection(url, ConnectionType.GET, true, false, null);
//        Integer status = connection.getResponseCode();
//        if (status >= 200 && status < 300) {
//            inputStream = new BufferedInputStream(connection.getInputStream());
//            json = Actions.readJsonStream(inputStream);
//            inputStream.close();
//            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus"))
//                Actions.writeJsonFile(context, json, siteId + "_syllabus", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus");
//        }
    }

}