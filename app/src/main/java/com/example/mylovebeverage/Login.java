package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.example.mylovebeverage.databinding.ActivityLoginBinding;
import java.sql.SQLException;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    Connecting_MSSQL connecting_mssql;
    Account account ;
    private static Connection connection_login = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        connecting_mssql = new Connecting_MSSQL(connection_login);
        connection_login = connecting_mssql.Connecting();
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
                if (connection_login!=null)
                {
                    try {
                        Statement statement = connection_login.createStatement();
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
                            }else if(check_key == 3)
                            {
                                Toast.makeText(getApplicationContext() , "welcome to my boss" ,Toast.LENGTH_SHORT).show();
                                break;
                            }else if(check_key ==4)
                            {
                                //se chuyen tiep qua man hinh order tu login.
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