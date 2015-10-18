package org.sakaiproject.api.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by vasilis on 10/18/15.
 * get if the device is connected to the internet, and what type of connection is
 */
public class NetWork {

    private static boolean connectionEstablished = false;

    private static NetWorkTypes getNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info == null || !info.isConnected())
            return NetWorkTypes.NO_CONNECTION;

        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return NetWorkTypes.WIFI;

        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: // all above for 2G

                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP: // all above for 3G

                case TelephonyManager.NETWORK_TYPE_LTE:   // 4G
                    return NetWorkTypes.MOBILE_NETWORK;
                default:
                    return NetWorkTypes.UNKNOWN;
            }
        }
        return NetWorkTypes.UNKNOWN;
    }

    public static boolean isConnected(Context context) {
        NetWorkTypes netWorkType;
        netWorkType = getNetwork(context);
        if (netWorkType == NetWorkTypes.WIFI || netWorkType == NetWorkTypes.MOBILE_NETWORK) {
            connectionEstablished = true;
        } else {
            connectionEstablished = false;
        }
        return connectionEstablished;
    }

    public static boolean getConnectionEstablished() {
        return connectionEstablished;
    }
}
