package com.azlan.freedom.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.azlan.freedom.R;
import com.azlan.freedom.Services.AuthenticationCheckService;
import com.azlan.freedom.databinding.ActivityHomeBinding;
import com.azlan.freedom.ui.fragments.AddDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;
    private BroadcastReceiver internetConnectionReceiver;
    private BroadcastReceiver authConnectionReceiver;
    AppBarConfiguration appBarConfiguration;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
         navController  = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);

         appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                 R.id.navigation_chat,
                 R.id.navigation_notifications,
                 R.id.navigation_profile)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.navigation_add){
                    //
//                    Toast.makeText(HomeActivity.this, "add clicked", Toast.LENGTH_SHORT).show();
                    showCustomDialogAboveNavigationView(navController);
                }


                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });
        // Register the broadcast receiver to listen for internet connection lost
        internetConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Display a Snackbar when the internet connection is lost
                if (intent.getAction() != null) {
                    if (intent.getAction().equals("INTERNET_CONNECTION_LOST")) {
                        // Display a Snackbar when the internet connection is lost
                        View rootView = findViewById(android.R.id.content);
                        Snackbar.make(rootView, "Internet connection lost", Snackbar.LENGTH_LONG).show();
                    } else if (intent.getAction().equals("INTERNET_CONNECTION_RESTORED")) {
                        // Display a Snackbar when the internet connection is restored
                        View rootView = findViewById(android.R.id.content);
                        Snackbar.make(rootView, "Internet connection restored", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("INTERNET_CONNECTION_LOST");
        intentFilter.addAction("INTERNET_CONNECTION_RESTORED");
        registerReceiver(internetConnectionReceiver, intentFilter);


        // Register the broadcast receiver to listen for Authentification Log Out
       authConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //
                if (intent.getAction().equals("AUTHENTICATION_LOGOUT")) {
                    // Display a Snackbar when FirbaseUSer has Log Out
                    if (intent.getBooleanExtra("isAuthenticated", true)) {
                        Log.d("TAG", "onReceive: "+"Authentication still");
                    } else {
                        View rootView = findViewById(android.R.id.content);
                        Snackbar.make(rootView, "Authentication Lost", Snackbar.LENGTH_LONG).show();
                        // Call the stopService method to stop the Service
                        stopService(new Intent(HomeActivity.this, AuthenticationCheckService.class));
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                }
            }
        };
        IntentFilter authintentFilter = new IntentFilter();
        intentFilter.addAction("AUTHENTICATION_LOGOUT");
        registerReceiver(authConnectionReceiver, intentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the broadcast receiver
        unregisterReceiver(internetConnectionReceiver);
        unregisterReceiver(authConnectionReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
         navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showCustomDialogAboveNavigationView(NavController navigationView) {
        // Create a new PopupWindow
        PopupWindow popupWindow = new PopupWindow(this);

        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.add_dialog, null);
        popupWindow.setContentView(dialogView);
        CardView cardV_photo=dialogView.findViewById(R.id.addphoto_cardview);
        CardView cardV_status=dialogView.findViewById(R.id.addstatus_cardview);
        CardView cardV_video=dialogView.findViewById(R.id.addvideo_cardview);
        // Create the dimensions of the PopupWindow (adjust as needed)
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        cardV_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(HomeActivity.this, AddPhotoActivity.class));
            }
        });
        cardV_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
              //  startActivity(new Intent(HomeActivity.this, AddStatusActivity.class));
            }
        });
        cardV_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
             //   startActivity(new Intent(HomeActivity.this, AddVideoActivity.class));
            }
        });
        // Calculate the position to show the PopupWindow above the NavigationView
        int[] location = new int[2];
        findViewById(R.id.nav_host_fragment_activity_home).getLocationOnScreen(location);
        int x =location[0];
        int y = (int) (location[1]+location[1]*2.5f);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setAnimationStyle(R.anim.anim_popup);
        
        // Show the PopupWindow above the NavigationView
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, y);


    }


}