package com.azlan.freedom.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class InternetCheckService extends Service {

    private BroadcastReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        registerConnectivityReceiver();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerConnectivityReceiver() {
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isInternetConnected(context)) {
                    // Internet is connected
                    // Broadcast an intent to the current activity to show a message
                    Intent broadcastIntent = new Intent("INTERNET_CONNECTION_RESTORED");
                    sendBroadcast(broadcastIntent);
                } else {
                    // Internet is disconnected
                    // Broadcast an intent to the current activity to show a message
                    Intent broadcastIntent = new Intent("INTERNET_CONNECTION_LOST");
                    broadcastIntent.putExtra("isBack",false);
                    sendBroadcast(broadcastIntent);
                }
            }
        };

        // Register the receiver for network connectivity changes
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    private boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
    }
}
