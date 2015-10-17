package org.sakaiproject.api.motd;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.sakaiproject.api.json.JsonParser;

/**
 * Created by vasilis on 10/16/15.
 */
public class MessageOfTheDay {

    private URL url;
    private HttpURLConnection con;
    private InputStream inputStream;
    private JsonParser jsonParse;

    public MessageOfTheDay() {
        jsonParse = new JsonParser();
    }

    private List<String> message;
    private List<String> siteUrl;
    private MessageOfTheDay messageOfTheDay = this;

    public List<String> getMessage() {
        return message;
    }

    public List<String> getSiteUrl() {
        return siteUrl;
    }

    public MessageOfTheDay getMessageOfTheDayObj() {
        return messageOfTheDay;
    }

    public void setSiteUrl(List<String> siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public void getMessageOfTheDay(String url) {
        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(10000);

            con.connect();

            Integer status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(con.getInputStream());
                String mothJson = readStream(inputStream);
                inputStream.close();
                messageOfTheDay = jsonParse.parseMotdJson(mothJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
