package org.sakaiproject.api.friends;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.friends.Friends;
import org.sakaiproject.api.pojos.login.Profile;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 07/04/16.
 */
public class FriendsService {
    private final String tag = User.getUserEid() + " friends";
    private Callback callback;
    private Gson gson = new Gson();
    private Context context;

    public FriendsService(Callback callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    public void getFriends(final String url) {
        JsonArrayRequest friendsRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type collectionType = new TypeToken<List<Friends>>() {
                }.getType();
                List<Friends> friends = gson.fromJson(response.toString(), collectionType);

                Log.i("json", response.toString());

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "friends"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "friends", User.getUserEid() + File.separator + "friends");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                getFriendData(friends);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    getFriends(url);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(friendsRequest, tag);
    }

    private void getFriendData(final List<Friends> friends) {

        for (int i = 0; i < friends.size(); i++) {
            final Friends f = friends.get(i);
            String tag = f.getDisplayName() + " data";
            String url = context.getString(R.string.url) + "profile/" + f.getFriendId() + ".json";
            final int index = i;
            JsonObjectRequest friendDataRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Profile profile = gson.fromJson(response.toString(), Profile.class);

                    Log.i("profile", response.toString());

                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "friends" + File.separator + f.getFriendId()))
                        try {
                            ActionsHelper.writeJsonFile(context, response.toString(), f.getFriendId() + "_data", User.getUserEid() + File.separator + "friends" + File.separator + f.getFriendId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    getFriendImage(friends, index, profile.getImageThumbUrl());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getFriendData(friends);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("_validateSession", Connection.getSessionId());
                    return headers;
                }
            };
            AppController.getInstance().addToRequestQueue(friendDataRequest, tag);
        }
    }

    private void getFriendImage(final List<Friends> friends, final int index, String url) {
        String tag = friends.get(index).getFriendId() + " image";
        ImageRequest friendImageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                try {
                    ActionsHelper.saveImage(context, response, friends.get(index).getFriendId() + "_image.jpg", User.getUserEid() + File.separator + "friends" + File.separator + friends.get(index).getFriendId());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (index == friends.size() - 1)
                    callback.onSuccess(friends);
            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
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
        AppController.getInstance().addToRequestQueue(friendImageRequest, tag);
    }
}
