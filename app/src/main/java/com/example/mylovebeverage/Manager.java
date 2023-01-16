package com.example.mylovebeverage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.databinding.ActivityManagerBinding;
import com.google.android.material.navigation.NavigationView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Manager extends AppCompatActivity {
    private ActivityManagerBinding binding;
    private static Connection connection_manager;
    private String username ="";
    private String password ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        connection_manager = new Connecting_MSSQL(connection_manager).Connecting();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = new Bundle();
        if (connection_manager != null)
        {
            try {
                Statement statement = connection_manager.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT Staff_ID , Name_of_staff ,Position , Gender , PhoneNumber , Email , ProvivedImage\n" +
                        "FROM STAFF\n" +
                        "WHERE Staff_ID ="+"'"+username+"'"+";");
                while (resultSet.next())
                {
                    bundle.putString("AccountName" ,resultSet.getString(1).toString().trim());
                    bundle.putString("Password" ,password);
                    bundle.putString("Name" , resultSet.getString(2).toString().trim());
                    bundle.putString("Position" , resultSet.getString(3).toString().trim());
                    bundle.putString("Gender" , resultSet.getString(4).toString().trim());
                    bundle.putString("PhoneNumber" , resultSet.getString(5).toString().trim());
                    bundle.putString("Email" , resultSet.getString(6).toString().trim());
                    bundle.putString("Image", resultSet.getString(7).toString().trim());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        binding.imageslidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawlayoutlayer.openDrawer(GravityCompat.START);

            }
        });
        binding.navigationview.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this ,binding.navigationHostFragment.getId());
        NavigationUI.setupWithNavController(binding.navigationview , navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                binding.textApp.setText(navDestination.getLabel());
            }
        });
        binding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().toString().trim().equals("Profile")) {
                    navController.navigate(R.id.profile, bundle);
                }else if (item.getTitle().toString().trim().equals("Home"))
                {
                    navController.navigate(R.id.home);
                }else if (item.getTitle().toString().trim().equals("About Us"))
                {
                    navController.navigate(R.id.aboutus);
                }else if (item.getTitle().toString().trim().equals("Statistic"))
                {
                    navController.navigate(R.id.statistic);
                }
                else if (item.getTitle().toString().trim().equals("Log Out"))
                {
                    Dialog dialog = new Dialog(Manager.this);
                    dialog.setContentView(R.layout.activity_log_out);
                    Button btnYes = (Button) dialog.findViewById(R.id.btnCustomDialogYes);
                    Button btnNo  = (Button) dialog.findViewById(R.id.btnCustomDialogNo);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                               dialog.dismiss();
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Statement statement = connection_manager.createStatement();
                                statement.execute("UPDATE ACCOUNT\n" +
                                        "SET Status ='inactive'\n" +
                                        "WHERE Account_name ="+"'"+username+"'");
                                dialog.dismiss();
                                finish();
                                startActivity(new Intent(getApplicationContext() , Login.class));
                            }catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                return true;
            }
        });





    }
}