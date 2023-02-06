package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Account;
import com.example.mylovebeverage.SharedPreferences.MyPreferences;
import com.example.mylovebeverage.databinding.ActivityLoginBinding;

import java.sql.SQLException;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    Account account ;
    private static Connection connection;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myPreferences = new MyPreferences(getApplicationContext());
        if (myPreferences.getKeyCheck() && myPreferences.getPosition().equals("BOSS")) {
            Intent intent_boss = new Intent(getApplicationContext(), Manager.class);
            intent_boss.putExtra("username", myPreferences.getUsername());
            intent_boss.putExtra("password", myPreferences.getPassword());
            startActivity(intent_boss);
            finish();
        } else if (myPreferences.getKeyCheck() && myPreferences.getPosition().equals("ORDER")) {
            Intent intent_order = new Intent(getApplicationContext(), Order.class);
            intent_order.putExtra("Staff Id", myPreferences.getUsername());
            startActivity(intent_order);
            finish();
        }
        connection = new Connecting_MSSQL(connection).Connecting();

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
        login();
    }
    protected void login()
    {
        binding.buttonLogin.setOnClickListener(v->{
            if(checkLogin())
            {
                String username = binding.loginUsername.getText().toString().trim();
                String password = binding.loginPassword.getText().toString().trim();
                if (connection!=null)
                {
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT;");
                        int check_key = 1;
                        while(resultSet.next())
                        {
                            account = new Account(resultSet.getString(1).trim() ,resultSet.getString(2).trim(), resultSet.getString(3).trim());
                            check_key = account.AuthenticateLogin(username , password);
                            if(check_key ==2)
                            {
                                Toast.makeText(getApplicationContext() , "Wrong password" , Toast.LENGTH_SHORT).show();
                                break;
                            }else if(check_key == 3) {
                                Statement statement1 = connection.createStatement();
                                statement1.execute("UPDATE ACCOUNT\n" +
                                        "SET Status ='active'\n" +
                                        "WHERE Account_name =" + "'" + username + "'");
                                myPreferences.saveKeyCheck(true);
                                myPreferences.saveUsername(username);
                                myPreferences.savePassword(password);
                                myPreferences.savePosition("BOSS");
                                Intent intent = new Intent(getApplicationContext(), Manager.class);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                                break;
                            }else if(check_key ==4) {
                                Statement statement1 = connection.createStatement();
                                statement1.execute("UPDATE ACCOUNT\n" +
                                        "SET Status ='active'\n" +
                                        "WHERE Account_name =" + "'" + username + "'");
                                myPreferences.saveKeyCheck(true);
                                myPreferences.saveUsername(username);
                                myPreferences.savePassword(password);
                                myPreferences.savePosition("ORDER");
                                Intent intent = new Intent(getApplicationContext(), Order.class);
                                intent.putExtra("Staff Id", account.getUsername());
                                startActivity(intent);
                                finish();
                                break;
                            }
                        }
                        resultSet.close();
                        statement.close();
                        if(check_key ==1)
                        {
                            Toast.makeText(getApplicationContext() , "Wrong username" , Toast.LENGTH_SHORT).show();
                        }
                    }catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Connect makes error.Please hotline with the administrator!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected boolean checkLogin()
    {
        if(binding.loginUsername.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this , "You must enter your username!" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(binding.loginPassword.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "You must enter your password!" , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}