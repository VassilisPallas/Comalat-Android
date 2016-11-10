package org.sakaiproject.api.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.login.Login;
import org.sakaiproject.api.pojos.login.Profile;
import org.sakaiproject.api.pojos.login.UserData;
import org.sakaiproject.api.user.workspace.WorkspaceService;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.Connection;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 09/02/16.
 */
public class LoginService implements ILogin {

    private final String tag_session_id = "sessionId";
    private final String tag_login_json = "login json";
    private final String tag_user_data_json = "user data json";
    private final String tag_user_profile_data_json = "user profile data json";
    private final String tag_user_image = "user image";
    private final String tag_user_image_thumb = "user image thumb";

    private StringRequest sessionRequest;
    private ImageRequest userImage, userImagethumb;

    private ProgressBar progressBar;
    private TextView loginTextView;
    private Context context;

    private final Gson gson = new Gson();

    public LoginService(ProgressBar progressBar, TextView loginTextView, Context context) {
        this.progressBar = progressBar;
        this.loginTextView = loginTextView;
        this.context = context;
        progressBar.setVisibility(View.VISIBLE);
        loginTextView.setVisibility(View.GONE);
    }

    @Override
    public void login(String... params) {
        getSessionId(params[0], params[1], params[2]);
    }

    private void getSessionId(final String url, final String name, final String pass) {

        sessionRequest = new StringRequest(Request.Method.POST, url + "session", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Connection.setSessionId(response);
                PasswordEncryption passwordEncryption = new PasswordEncryption();

                SharedPreferences.Editor editor = context.getSharedPreferences(name + "_user_data", context.MODE_PRIVATE).edit();
                editor.putString("user_id", name);
                editor.putString("password", passwordEncryption.encrypt(pass));
                editor.apply();

                getLoginJson(url, response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("_username", name);
                params.put("_password", pass);

                return params;
            }

        };

        sessionRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(sessionRequest, tag_session_id);
    }

    @Override
    public void getLoginJson(final String... params) {
        Log.i("param", params[1]);
        JsonObjectRequest loginJson = new JsonObjectRequest(Request.Method.GET, params[0] + "session/" + params[1] + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Login login = gson.fromJson(response.toString(), Login.class);

                JsonParser.parseLoginResult(login);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "loginJson", User.getUserEid() + File.separator + "user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                WorkspaceService workspaceService = new WorkspaceService(context, User.getUserId());
                workspaceService.setProgressBar(progressBar);
                workspaceService.setLoginTextView(loginTextView);
                workspaceService.setDelegate(new Callback() {
                    @Override
                    public void onSuccess(Object obj) {

                    }

                    @Override
                    public void onError(VolleyError error) {
                        if (error instanceof ServerError)
                            Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
                workspaceService.setLogin(true);
                workspaceService.getWorkspace();

                getUserDataJson(params[0], User.getUserEid());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginJson.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(loginJson, tag_login_json);
    }

    @Override
    public void getUserDataJson(final String... params) {

        JsonObjectRequest userDataJson = new JsonObjectRequest(Request.Method.GET, params[0] + "user/" + params[1] + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserData userData = gson.fromJson(response.toString(), UserData.class);

                JsonParser.parseUserDataJson(userData);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "fullUserDataJson", User.getUserEid() + File.separator + "user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                getUserProfileDataJson(params[0], params[1]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        userDataJson.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(userDataJson, tag_user_data_json);

    }

    @Override
    public void getUserProfileDataJson(String... params) {

        JsonObjectRequest userProfileDataJson = new JsonObjectRequest(Request.Method.GET, params[0] + "profile/" + params[1] + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Profile profile = gson.fromJson(response.toString(), Profile.class);

                JsonParser.parseUserProfileDataJson(profile);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "userProfileDataJson", User.getUserEid() + File.separator + "user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                getUserImage(org.sakaiproject.api.user.profile.Profile.getImageUrl());
                getUserThumbnailImage(org.sakaiproject.api.user.profile.Profile.getImageThumbUrl());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userProfileDataJson.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(userProfileDataJson, tag_user_profile_data_json);

    }

    @Override
    public void getUserImage(String... params) {
        userImage = new ImageRequest(params[0], new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    try {
                        ActionsHelper.saveImage(context, response, "user_image.jpg", User.getUserEid() + File.separator + "user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userImage.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(userImage, tag_user_image);
    }

    @Override
    public void getUserThumbnailImage(String... params) {
        userImagethumb = new ImageRequest(params[0], new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    try {
                        ActionsHelper.saveImage(context, response, "user_thumbnail_image.jpg", User.getUserEid() + File.separator + "user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);


            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                loginTextView.setVisibility(View.VISIBLE);
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userImagethumb.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(userImagethumb, tag_user_image_thumb);
    }

    @Override
    public LoginType getLoginType() {
        throw new UnsupportedOperationException();
    }

}
