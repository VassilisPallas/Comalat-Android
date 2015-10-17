package org.sakaiproject.api.user.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.data.UserData;
import org.sakaiproject.api.user.data.UserProfileData;
import org.sakaiproject.api.user.data.UserSessionData;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 10/12/15.
 */
public class Login {

    private UserSessionData userSessionData;
    private UserData userData;
    private UserProfileData userProfileData;
    private JsonParser jsonParse;

    private Bitmap bitmap;
    private InputStream inputStream;
    private OutputStreamWriter wr;
    private String sessionId;
    private String loginJson;
    private String fullUserDataJson;
    private Context context;
    private URL url;
    private HttpURLConnection con;

    public Login(Context context) {
        this.context = context;
        userData = new UserData();
        userSessionData = new UserSessionData();
        userProfileData = new UserProfileData();
        jsonParse = new JsonParser();
    }

    public UserSessionData getUserSessionData() {
        return userSessionData;
    }

    public UserData getUserData() {
        return userData;
    }

    public UserProfileData getUserProfileData() {
        return userProfileData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Integer loginConnection(String... params) {
        try {
            url = new URL(params[0]);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            // set the loginConnection timeout to 5 seconds and the read timeout to 10 seconds
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();

            String data = "_username=" + URLEncoder.encode(params[1], "UTF-8");
            data += "&";
            data += "_password=" + URLEncoder.encode(params[2], "UTF-8");

            wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();

            Integer status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(con.getInputStream());
                sessionId = readStream(inputStream);
                inputStream.close();
                getLoginJson(context.getResources().getString(R.string.url) + "session/" + sessionId + ".json");
                getUserDataJson(context.getResources().getString(R.string.url) + "user/" + userSessionData.getUserEid() + ".json");
                getUserProfileDataJson(context.getResources().getString(R.string.url) + "profile/" + userSessionData.getUserEid() + ".json");
                bitmap = getUserImage(userProfileData.getImageUrl());
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return 0;
    }

    private void getLoginJson(String jsonUrl) {
        try {
            url = new URL(jsonUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();

            Integer status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(con.getInputStream());
                loginJson = readStream(inputStream);
                inputStream.close();
                userSessionData = jsonParse.parseLoginResult(loginJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserDataJson(String jsonUrl) {
        try {
            url = new URL(jsonUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();

            Integer status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(con.getInputStream());
                fullUserDataJson = readStream(inputStream);
                inputStream.close();
                userData = jsonParse.parseUserDataJson(fullUserDataJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getUserProfileDataJson(String jsonUrl) {
        try {
            url = new URL(jsonUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();

            Integer status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(con.getInputStream());
                fullUserDataJson = readStream(inputStream);
                inputStream.close();
                userProfileData = jsonParse.parseUserProfileDataJson(fullUserDataJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getUserImage(String url) {
        Bitmap bitmap = null;
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();

            Integer status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                InputStream inputStream = new BufferedInputStream(con.getInputStream());
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (inputStream != null)
            inputStream.close();

        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        con.disconnect();
    }

}
