package org.sakaiproject.api.signup;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.sakaiproject.sakai.AppController;
import org.sakaiproject.api.login.LoginService;
import org.sakaiproject.sakai.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasilis on 10/17/15.
 * Create new user to sakai
 */
public class SignupService {

    String userId;
    private final String tag_sign_up = "sign up";
    private final String tag_eid_exists = "userEid exists";
    private boolean exists = true;

    private Context context;

    private ProgressBar signupProgressBar;
    private TextView signupTextView;
    private TextInputLayout idInputLayout;

    public SignupService(Context context, ProgressBar signupProgressBar, TextView signupTextView, TextInputLayout idInputLayout) {
        this.context = context;
        this.signupProgressBar = signupProgressBar;
        this.signupTextView = signupTextView;
        this.idInputLayout = idInputLayout;
    }

    /**
     * the REST call (POST) for the signup
     *
     * @param url       the url
     * @param eid       the username
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param password  the password
     */
    public void signUp(final String url, final String eid, final String firstName, final String lastName, final String email, final String password) {

        StringRequest signupRequest = new StringRequest(Request.Method.POST, url + "user", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userId = response;
                new LoginService(signupProgressBar, signupTextView, context).login(url, eid, password);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                signupProgressBar.setVisibility(View.GONE);
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("eid", eid);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("password", password);
                params.put("type", "registered");
                return params;
            }
        };

        signupRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(signupRequest, tag_sign_up);
    }

    /**
     * check if the username already exists
     *
     * @param url
     */
    public void eidExists(String url, final EidExistence existence) {

        StringRequest existsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                exists = false;

                idInputLayout.setError(context.getResources().getString(R.string.eid_exists));

                existence.signUpButton(exists);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                exists = true;
                idInputLayout.setErrorEnabled(false);

                existence.signUpButton(exists);
            }
        });


        existsRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(existsRequest, tag_eid_exists);
    }
}
