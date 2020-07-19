package com.fightalarm.fightalarm.providers;

/**
 * Created by Presly on 20/09/18.
 */


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver {

    private Context context;

    public ConnectivityReceiver(Context context) {
        this.context = context;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager manager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            for (NetworkInfo state : info) {
                if (state.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
