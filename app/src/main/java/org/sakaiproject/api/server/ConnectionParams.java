package org.sakaiproject.api.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vasilis on 10/17/15.
 */
public class ConnectionParams {
    private URL url;
    private HttpURLConnection con;
    private OutputStreamWriter wr;

    /**
     * open the connection to the server
     *
     * @param url    the url for the connection
     * @param method the method (POST, GET, PUT, DELETE)
     * @param accept true if accepts response (json), false if doesn't
     * @param data   the data for the output stream, null if it doesn't send request
     */
    public void openConnection(String url, String method, boolean accept, String data) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();

            if (method.equalsIgnoreCase("GET") && accept) {
                con.setRequestProperty("Accept", "application/json");
            }

            if (method.equalsIgnoreCase("POST")) {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
            } else if (method.equalsIgnoreCase("GET")) {
                con.setRequestMethod("GET");
            }

            // set the loginConnection timeout to 5 seconds and the read timeout to 10 seconds
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);


            if (method.equalsIgnoreCase("POST") && (data != null || !data.isEmpty())) {
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
