package com.cnbcyln.app.akordefterim.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetInfo {
    private ConnectivityManager connManager = null;
    private WifiManager wifiManager = null;
    private WifiInfo wifiInfo = null;

    public NetInfo(Activity activity) {
        connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        wifiInfo = wifiManager.getConnectionInfo();
    }

    public int getCurrentNetworkType() {
        if (null == connManager)
            return 0;
        NetworkInfo netinfo = connManager.getActiveNetworkInfo();
        return netinfo.getType();
    }

    @SuppressLint("DefaultLocale")
    public String getWifiIpAddress() {
        if (null == wifiManager || null == wifiInfo)
            return "";

        int ipAddress = wifiInfo.getIpAddress();
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }

    @SuppressLint("HardwareIds")
    public String getWiFiMACAddress() {
        if (null == wifiManager || null == wifiInfo)
            return "";
        return wifiInfo.getMacAddress();
    }

    public String getWiFiSSID() {
        if (null == wifiManager || null == wifiInfo)
            return "";

        return wifiInfo.getSSID();
    }

    public String getIPAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> enumnet = NetworkInterface.getNetworkInterfaces();
            NetworkInterface netinterface = null;

            while (enumnet.hasMoreElements()) {
                netinterface = enumnet.nextElement();

                for (Enumeration<InetAddress> enumip = netinterface.getInetAddresses();
                     enumip.hasMoreElements(); ) {
                    InetAddress inetAddress = enumip.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipaddress = inetAddress.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ipaddress;
    }
}
