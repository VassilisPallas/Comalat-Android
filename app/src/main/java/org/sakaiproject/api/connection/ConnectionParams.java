package org.sakaiproject.api.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vasilis on 10/17/15.
 */
public class ConnectionParams {
    private URL url;
    private HttpURLConnection con;

    public void openConnection(String url, String method, String accept) {
        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            // set the loginConnection timeout to 5 seconds and the read timeout to 10 seconds
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readStream(InputStream inputStream) throws IOException {
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
