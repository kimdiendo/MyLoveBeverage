package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mylovebeverage.databinding.ActivityLayoutNavigationHeaderBinding;

public class layout_navigation_header extends AppCompatActivity {
    private ActivityLayoutNavigationHeaderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLayoutNavigationHeaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}