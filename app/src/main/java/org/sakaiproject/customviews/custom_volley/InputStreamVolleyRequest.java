package org.sakaiproject.customviews.custom_volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by vspallas on 03/03/16.
 */
public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> listener;
    private Map<String, String> params;

    //static map for directly accessing headers
    public Map<String, String> responseHeaders;

    public InputStreamVolleyRequest(int method, String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener, Map<String, String> params) {
        super(method, url, errorListener);
        setShouldCache(false);
        this.listener = listener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        //return super.getParams();
        return params;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        listener.onResponse(response);
    }
}
