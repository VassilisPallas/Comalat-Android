package org.sakaiproject.general;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by vasilis on 10/17/15.
 * Singleton class for Connection
 * also stores ome useful session values
 */
public class Connection {
    private static Connection instance = null;
    private URL url;
    private HttpURLConnection con;
    private OutputStreamWriter wr;

    private static String sessionId = null;
    private static Long creationTime;
    private static Long lastAccessedTime;
    // max seconds the session can be idle before automatic invalidation
    private static Integer maxInactiveInterval;
    private Context context;

    private boolean socketException = false;

    private Connection() {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    public static synchronized Connection getInstance() {
        if (instance == null)
            instance = new Connection();
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static void setSessionId(String id) {
        if (sessionId == null)
            sessionId = id;
    }

    public static void nullSessionId() {
        if (sessionId != null)
            sessionId = null;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static Long getCreationTime() {
        return creationTime;
    }

    public static Long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public static Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public static void setCreationTime(Long creationTime) {
        Connection.creationTime = creationTime;
    }

    public static void setLastAccessedTime(Long lastAccessedTime) {
        Connection.lastAccessedTime = lastAccessedTime;
    }

    public static void setMaxInactiveInterval(Integer maxInactiveInterval) {
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
            Integer status = 0;
            // avoid any server's problem
            // 500 - INTERNAL SERVER ERROR (general server failure, probably a failure in the provider)
            do {
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

                // set connection timeout and read timeout to 10 seconds
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);


                if ((method == ConnectionType.POST || method == ConnectionType.PUT) && (data != null && !data.equals(""))) {
                    con.setDoOutput(true);
                    con.setChunkedStreamingMode(0);

                    wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    wr.close();
                }

                con.connect();
                status = getResponseCode();
            } while (status == 500);
        } catch (SocketTimeoutException e) {
            socketException = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketException = false;
    }

    public void closeConnection() {
        con.disconnect();
    }

    public Integer getResponseCode() throws IOException {
        return con.getResponseCode();
    }

    public boolean isSocketException() {
        return socketException;
    }

    public InputStream getInputStream() throws IOException {
        return con.getInputStream();
    }

    public InputStream getErrorStream() {
        return con.getErrorStream();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        instance = getInstance();
    }
}
