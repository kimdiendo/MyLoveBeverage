package com.example.mylovebeverage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Notify.BartenderSubscriber;
import com.example.mylovebeverage.Notify.Notifier;
import com.example.mylovebeverage.Notify.OrderSubscriber;
import com.example.mylovebeverage.Notify.SecuritySubscriber;
import com.example.mylovebeverage.Notify.WaitressSubscriber;
import com.example.mylovebeverage.SharedPreferences.MyPreferences;
import com.example.mylovebeverage.Singleton.MySingleton;
import com.example.mylovebeverage.databinding.ActivityManagerBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Manager extends AppCompatActivity {
    private ActivityManagerBinding binding;
    private static Connection connection_manager;
    private String username ="";
    private String password = "";
    Bundle bundle;
    MyPreferences myPreferences;
    private static final int PERMISSION_REQUEST_CODE_CALL = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myPreferences = new MyPreferences(getApplicationContext());
        connection_manager = new Connecting_MSSQL(connection_manager).Connecting();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        MySingleton.getInstance().setVariable(username);
        if (connection_manager != null) {
            bundle = new Bundle();
            try {
                Statement statement = connection_manager.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT Staff_ID , Name_of_staff ,Position , Gender , PhoneNumber , Email , ProvivedImage\n" +
                        "FROM STAFF\n" +
                        "WHERE Staff_ID =" + "'" + username + "'" + ";");
                while (resultSet.next()) {
                    bundle.putString("AccountName", resultSet.getString(1).trim());
                    bundle.putString("Password", password);
                    bundle.putString("Name", resultSet.getString(2).trim());
                    bundle.putString("Position", resultSet.getString(3).trim());
                    bundle.putString("Gender", resultSet.getString(4).trim());
                    bundle.putString("PhoneNumber", resultSet.getString(5).trim());
                    bundle.putString("Email", resultSet.getString(6).trim());
                    bundle.putString("Image", resultSet.getString(7).trim());
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void requestCallPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
                //provide explannation to user that s' why they must provide permission
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE_CALL);
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCalling();
            } else {
                Toast.makeText(this, "Calling Permission has denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCalling() {
        if (ContextCompat.checkSelfPermission(Manager.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent_call = new Intent();
            intent_call.setAction(Intent.ACTION_CALL);
            intent_call.setData(Uri.parse("tel: " + "0964376592"));
            startActivity(intent_call);
        } else {
            // Permission has not been granted, request it
            requestCallPermission();
        }
    }

    private void Handling_text_spinner(Spinner spinner, EditText edt) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt.setText(parent.getAdapter().getItem(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                }else if (item.getTitle().toString().trim().equals("Home")) {
                    navController.navigate(R.id.home);
                } else if (item.getTitle().toString().trim().equals("Hotline")) {
                    Dialog dialog = new Dialog(Manager.this);
                    dialog.setContentView(R.layout.activity_hotline);
                    Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                    Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
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
                            openCalling();
                            dialog.dismiss();
                        }
                    });
                }else if (item.getTitle().toString().trim().equals("Statistic")) {
                    navController.navigate(R.id.statistic);
                } else if (item.getTitle().toString().trim().equals("Log Out")) {
                    Dialog dialog = new Dialog(Manager.this);
                    dialog.setContentView(R.layout.activity_log_out);
                    Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                    Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
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
                                        "WHERE Account_name =" + "'" + username + "'");
                                dialog.dismiss();
                                finish();
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                mAuth.signOut();
                                myPreferences.saveKeyCheck(false);
                                myPreferences.saveUsername("");
                                myPreferences.savePassword("");
                                myPreferences.savePosition("");
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (item.getTitle().toString().trim().equals("About Us")) {
                    String url = "https://kms-technology.com/danang";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if (item.getTitle().toString().trim().equals("Attendance DashBoard")) {
                    navController.navigate(R.id.attendancedashboard);
                } else if (item.getTitle().toString().equals("Notification")) {
                    //set up subscriber for publisher
                    BartenderSubscriber bartenderSubscriber = new BartenderSubscriber();
                    OrderSubscriber orderSubscriber = new OrderSubscriber();
                    SecuritySubscriber securitySubscriber = new SecuritySubscriber();
                    WaitressSubscriber waitressSubscriber = new WaitressSubscriber();
                    Notifier notifier = new Notifier();
                    // subscribe
                    notifier.subscribe(bartenderSubscriber); //0
                    notifier.subscribe(waitressSubscriber);  //1
                    notifier.subscribe(securitySubscriber);  //2
                    notifier.subscribe(orderSubscriber);  //3
                    //done
                    Dialog dialog = new Dialog(Manager.this);
                    dialog.setContentView(R.layout.activity_create_notification);
                    ImageView btn_close = dialog.findViewById(R.id.close);
                    Button btn_send = dialog.findViewById(R.id.button);
                    EditText edt_message = dialog.findViewById(R.id.editText);
                    EditText edt_type_position = dialog.findViewById(R.id.position);
                    ArrayAdapter<CharSequence> array_type_notification = ArrayAdapter.createFromResource(Manager.this, R.array.type_notification_position, android.R.layout.simple_spinner_item);
                    array_type_notification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner spinner_type_notification_position = dialog.findViewById(R.id.img_position);
                    spinner_type_notification_position.setAdapter(array_type_notification);
                    Handling_text_spinner(spinner_type_notification_position, edt_type_position);
                    dialog.show();
                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btn_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifier.setMessage(edt_message.getText().toString().trim());
                            notifier.setType_position(edt_type_position.getText().toString().trim());
                            notifier.Notify();
                            dialog.dismiss();
                        }
                    });
                }
                return true;
            }
        });
    }
}
