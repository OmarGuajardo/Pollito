package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.github.florent37.materialviewpager.MaterialViewPager;

public class ProfileActivity extends AppCompatActivity {
MaterialViewPager materialViewPager;
ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}