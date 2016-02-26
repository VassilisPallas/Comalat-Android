package org.sakaiproject.api.session;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.custom_volley.BooleanRequest;
import org.sakaiproject.sakai.AppController;

/**
 * Created by vspallas on 26/02/16.
 */
@Deprecated
public class IdleModeCheckService extends IntentService {

    private String idle_tag = User.getUserEid() + " idle";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public IdleModeCheckService() {
        super("IdleModeCheckService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getDataString();
        Log.i("url", url);
        while (NetWork.getConnectionEstablished()) {

            BooleanRequest idle = new BooleanRequest(Request.Method.GET, url, null, new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    Log.i("idle", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            AppController.getInstance().addToRequestQueue(idle, idle_tag);
        }
    }
}
