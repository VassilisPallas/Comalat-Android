package org.sakaiproject.api.general;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vasilis on 10/17/15.
 * Singleton class for Connection
 * also stores the session id
 */
public class Connection {
    private static Connection instance = null;
    private URL url;
    private HttpURLConnection con;
    private OutputStreamWriter wr;

    private static String sessionId = null;
    private static Integer creationTime;
    private static Integer lastAccessedTime;
    private static Integer maxInactiveInterval;


    private Connection() {
    }

    public static synchronized Connection getInstance() {
        if (instance == null)
            instance = new Connection();
        return instance;
    }

    public static synchronized void setSessionId(String id) {
        if (sessionId == null)
            sessionId = id;
    }

    public static synchronized void nullSessionId() {
        if (sessionId != null)
            sessionId = null;
    }

    public static synchronized String getSessionId() {
        return sessionId;
    }

    public static synchronized Integer getCreationTime() {
        return creationTime;
    }

    public static synchronized Integer getLastAccessedTime() {
        return lastAccessedTime;
    }

    public static synchronized Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public static synchronized void setCreationTime(Integer creationTime) {
        Connection.creationTime = creationTime;
    }

    public static synchronized void setLastAccessedTime(Integer lastAccessedTime) {
        Connection.lastAccessedTime = lastAccessedTime;
    }

    public static synchronized void setMaxInactiveInterval(Integer maxInactiveInterval) {
        Connection.maxInactiveInterval = maxInactiveInterval;
    }

    /**
     * open the connection to the server
     *
     * @param urlCon      the url for the connection
     * @param method      the type (POST, GET, PUT, DELETE)
     * @param accept      true if send request (json), false if doesn't
     * @param contentType true for content type (x-www-form-urlencoded)
     * @param data        the data for the output stream, null if it doesn't send request
     */
    public void openConnection(String urlCon, ConnectionType method, boolean accept, boolean contentType, String data) {
        try {
            url = new URL(urlCon);
            con = (HttpURLConnection) url.openConnection();

            if (accept) {
                con.setRequestProperty("Accept", "application/json");
            }

            if (contentType) {
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }

            switch (method) {
                case POST:
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    break;
                case GET:
                    con.setRequestMethod("GET");
                    break;
                case PUT:
                    con.setRequestMethod("PUT");
                    break;
                case DELETE:
                    con.setRequestMethod("DELETE");
                    break;
                default:
                    throw new IllegalArgumentException("Not valid type");
            }

            // set loginConnection timeout and read timeout to 10 seconds
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);


            if ((method == ConnectionType.POST || method == ConnectionType.PUT) && (data != null || !data.isEmpty())) {
                wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(data);
                wr.flush();
                wr.close();
            }

            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        con.disconnect();
    }

    public Integer getResponseCode() throws IOException {
        return con.getResponseCode();
    }

    public InputStream getInputStream() throws IOException {
        return con.getInputStream();
    }

}
