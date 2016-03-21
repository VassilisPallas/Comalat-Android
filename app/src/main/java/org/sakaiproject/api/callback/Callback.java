package org.sakaiproject.api.callback;

import com.android.volley.VolleyError;

/**
 * Created by vspallas on 21/03/16.
 */
public interface Callback {
    void onSuccess(Object obj);
    void onError(VolleyError error);
}
