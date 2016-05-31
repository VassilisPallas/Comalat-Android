package org.sakaiproject.api.memberships.pages.roster;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.customviews.custom_volley.InputStreamVolleyRequest;
import org.sakaiproject.general.notifications.DownloadNotification;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vspallas on 03/03/16.
 */
public class DownloadExcelService {
    private Context context;
    private SiteData siteData;
    private InputStreamVolleyRequest downloadRequest;
    private DownloadNotification downloadNotification;

    public DownloadExcelService(Context context, SiteData siteData) {
        this.context = context;
        this.siteData = siteData;
        downloadNotification = new DownloadNotification(context);
    }

    public void download(String url) {
        downloadNotification.showNotification();
        String download_tag = siteData.getId() + " roster excel download";
        downloadRequest = new InputStreamVolleyRequest(Request.Method.GET, url, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                saveFile(response, downloadRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
                downloadNotification.hideNotification();
            }
        }, null);

        AppController.getInstance().addToRequestQueue(downloadRequest, download_tag);
    }

    private void saveFile(byte[] response, InputStreamVolleyRequest downloadRequest) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "Comalat" + File.separator + siteData.getTitle();
        File f = ActionsHelper.saveFile(context, response, path, downloadRequest);
        downloadNotification.setPath(f);
        downloadNotification.hideNotification();
    }

}
