package com.moutamid.educationappuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.educationappuser.databinding.ActivityMainBinding;
import com.moutamid.educationappuser.ui.MyClassActivity;
import com.moutamid.educationappuser.ui.ProfileActivity;
import com.moutamid.educationappuser.ui.ScoreActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.classMy.setOnClickListener(v -> {
            startActivity(new Intent(this, MyClassActivity.class));
            finish();
        });

        binding.profile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });

        binding.score.setOnClickListener(v -> {
            startActivity(new Intent(this, ScoreActivity.class));
            finish();
        });

        greetingMessage();

    }

    private void greetingMessage() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            binding.morning.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            binding.morning.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            binding.morning.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            binding.morning.setText("Good Night");
        }
    }

}