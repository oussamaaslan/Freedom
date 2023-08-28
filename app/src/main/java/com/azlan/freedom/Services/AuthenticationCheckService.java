package com.azlan.freedom.Services;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.ViewModelProvider;

import com.azlan.freedom.tools.Utility;
import com.azlan.freedom.viewmodels.AuthViewModel;

import java.util.concurrent.atomic.AtomicReference;

public class AuthenticationCheckService extends LifecycleService {

    private Handler handler;
    private AuthViewModel authViewModel;
    private Runnable checkAuthenticationRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        //initialation of AuthViewModel
        authViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AuthViewModel.class);
        // Initialize the handler and the runnable
        handler = new Handler();
        checkAuthenticationRunnable = new MyRunnable(this,authViewModel,handler);  }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the authentication check runnable when the service is started
        super.onStartCommand(intent, flags, startId);
        handler.post(checkAuthenticationRunnable);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Remove the callback when the service is destroyed to prevent memory leaks
        handler.removeCallbacks(checkAuthenticationRunnable);
    }

    public class MyRunnable implements Runnable {
        private LifecycleOwner lifecycleOwner;
        private AuthViewModel authViewModel;
        Handler handler;

        public MyRunnable(LifecycleOwner lifecycleOwner,AuthViewModel authViewModel,Handler handler) {
            this.lifecycleOwner = lifecycleOwner;
            this.authViewModel=authViewModel;
            this.handler=handler;
        }

        @Override
        public void run() {
//            AtomicReference<String> token=null;
//            authViewModel.getFirbaseToken();
//            authViewModel.firebaseTokenLiveData.observe(lifecycleOwner,s -> {
//                token.set(s);
//            });
            // Use the lifecycleOwner reference to observe LiveData or perform any other lifecycle-aware operations
            // For example, observing LiveData:
            if(Utility.tokenCheck(getApplicationContext())){


            authViewModel.getFirbaseToken();
            authViewModel.firebaseTokenLiveData.observe(lifecycleOwner, s -> {

                        if(s==null){
                            Utility.tokenLogout(getApplicationContext());
                            Intent broadcastIntent = new Intent("AUTHENTICATION_LOGOUT");
                            broadcastIntent.putExtra("isAuthenticated",false);
                            sendBroadcast(broadcastIntent);
                        }
                        else {
                            Utility.loginRefresh(getApplicationContext(),s);



                        }
                    });
            }           handler.postDelayed(this, 1000); // 60000 milliseconds = 1 minute
            // Do other background tasks here...
        }
    }
}


