package org.sakaiproject.api.memberships.actions;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.adapters.MembershipAdapter;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.workspace.WorkspaceService;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.general.Connection;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasilis on 1/22/16.
 */
public class SiteUnJoin {

    private String siteId;
    private Context context;
    private MembershipAdapter mAdapter;
    private int position;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout customSwipeRefreshLayout;
    private Callback callback;
    private final String unjoin_tag;

    public SiteUnJoin(String siteId, Context context, MembershipAdapter mAdapter, int position, org.sakaiproject.customviews.CustomSwipeRefreshLayout customSwipeRefreshLayout, Callback callback) {
        this.mAdapter = mAdapter;
        this.context = context;
        this.siteId = siteId;
        this.position = position;
        this.customSwipeRefreshLayout = customSwipeRefreshLayout;
        this.callback = callback;
        unjoin_tag = User.getUserEid() + " " + siteId + " unjoin";
    }

    public void unJoin() {
        EmptyRequest membershipUnJoinRequest = new EmptyRequest(Request.Method.POST, context.getString(R.string.url) + "/membership/unjoin/site/" + siteId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                WorkspaceService workspaceService = new WorkspaceService(context, User.getUserId());
                workspaceService.setDelegate(callback);
                workspaceService.getWorkspace();

                mAdapter.notifyItemRemoved(position);

                Toast.makeText(context, context.getResources().getString(R.string.successful_unjoined), Toast.LENGTH_SHORT).show();
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

        AppController.getInstance().addToRequestQueue(membershipUnJoinRequest, unjoin_tag);
    }
}
