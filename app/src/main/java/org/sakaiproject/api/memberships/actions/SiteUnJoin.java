package org.sakaiproject.api.memberships.actions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONObject;
import org.sakaiproject.api.memberships.MembershipService;
import org.sakaiproject.api.sync.MembershipRefreshUI;
import org.sakaiproject.adapters.MembershipAdapter;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.IOException;

/**
 * Created by vasilis on 1/22/16.
 */
public class SiteUnJoin {

    private String siteId;
    private Context context;
    private MembershipAdapter mAdapter;
    private int position;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout customSwipeRefreshLayout;
    private MembershipRefreshUI delegate;


    public SiteUnJoin(String siteId, Context context, MembershipAdapter mAdapter, int position, org.sakaiproject.customviews.CustomSwipeRefreshLayout customSwipeRefreshLayout, MembershipRefreshUI delegate) {
        this.mAdapter = mAdapter;
        this.context = context;
        this.siteId = siteId;
        this.position = position;
        this.customSwipeRefreshLayout = customSwipeRefreshLayout;
        this.delegate = delegate;
    }

    public void unJoin() {
        EmptyRequest membershipUnJoinRequest = new EmptyRequest(Request.Method.POST, context.getString(R.string.url) + "/membership/unjoin/site/" + siteId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                MembershipService membershipService = new MembershipService(context, delegate);
                membershipService.setSwipeRefreshLayout(customSwipeRefreshLayout);
                try {
                    membershipService.getSites(context.getString(R.string.url) + "membership.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyItemRemoved(position);

                Toast.makeText(context, context.getResources().getString(R.string.successful_unjoined), Toast.LENGTH_SHORT).show();

                Log.i("unjoin", "true");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("unjoin", error.getMessage());
                Log.i("unjoin", "false");
                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(membershipUnJoinRequest, "join");
    }
}
