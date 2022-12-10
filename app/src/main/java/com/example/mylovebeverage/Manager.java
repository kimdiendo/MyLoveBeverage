package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mylovebeverage.databinding.ActivityManagerBinding;

public class Manager extends AppCompatActivity {
    private ActivityManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        Manage_Staff();
        Manage_Supplier();
        Manage_Warehouse();
        Manage_Product();
        Manage_Invoice();
        Manage_Expense();

    }
    protected void Manage_Staff()
    {

    }
    protected void Manage_Warehouse()
    {

    }
    protected void Manage_Supplier()
    {

    }
    protected void Manage_Product()
    {
        binding.buttonProduct.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), ManageProduct.class)));
    }
    protected void Manage_Invoice()
    {

    }
    protected void Manage_Expense()
    {

    }

}