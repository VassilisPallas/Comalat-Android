package org.sakaiproject.api.login;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by vasilis on 10/18/15.
 * The methods for the login connection with or without internet connection
 */
public interface ILogin {

    /**
     * @param params for login with internet connection params[0] = url, params[1] = user_id, params[2] = password,
     *               for login without internet connection params[0] = user_id, params[1] = password
     * @throws IOException
     */
    public void login(String... params);

    /**
     * get the json file from the login
     *
     * @param params for login with internet connection params[0] = jsonUrl
     *               for login without internet connection nothing
     * @throws IOException
     */
    void getLoginJson(String... params) throws IOException;

    /**
     * get the json with the user's data
     *
     * @param params for login with internet connection params[0] = jsonUrl
     *               for login without internet connection nothing
     * @throws IOException
     */
    void getUserDataJson(String... params) throws IOException;

    /**
     * get the json file with user's profile data
     *
     * @param params for login with internet connection params[0] = jsonUrl
     *               for login without internet connection nothing
     * @throws IOException
     */
    void getUserProfileDataJson(String... params) throws IOException;

    /**
     * get user's profile image
     *
     * @param params for login with internet connection params[0] = imageUrl
     *               for login without internet connection nothing
     */
    void getUserImage(String... params) throws FileNotFoundException;

    /**
     * get user's profile thumbnail image
     *
     * @param params for login with internet connection params[0] = imageUrl
     *               for login without internet connection nothing
     */
    void getUserThumbnailImage(String... params) throws FileNotFoundException;

    /**
     * @return the login type
     */
    LoginType getLoginType();
}
