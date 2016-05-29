package org.sakaiproject.api.memberships.pages.syllabus;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasilis on 1/28/16.
 */
public class SyllabusService {

    private Context context;
    private String siteId;
    private final String syllabus_tag;
    Callback callback;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    /**
     * OnlineSyllabus constructor
     *
     * @param context the context
     */
    public SyllabusService(Context context, String siteId, Callback callback) {
        this.context = context;
        this.siteId = siteId;
        this.callback = callback;
        syllabus_tag = User.getUserEid() + " " + siteId + " syllabus";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getSyllabus(String url) throws IOException {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest syllabusRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_syllabus", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                callback.onSuccess(null);

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               callback.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(syllabusRequest, syllabus_tag);
    }
}
