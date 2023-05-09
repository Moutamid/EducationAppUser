package com.moutamid.educationappuser.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.educationappuser.MainActivity;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.SplashScreenActivity;
import com.moutamid.educationappuser.databinding.ActivityProfileBinding;
import com.moutamid.educationappuser.models.UserModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        greetingMessage();
        binding.header.tittle.setText("Profile");

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModel model = snapshot.getValue(UserModel.class);
                            binding.username.setText(model.getUsername().toUpperCase());
                            binding.email.setText(model.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        binding.logout.setOnClickListener(v -> {
            Constants.auth().signOut();
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
        });

    }

    private void greetingMessage() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            binding.greeting.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            binding.greeting.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            binding.greeting.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            binding.greeting.setText("Good Night");
        }
    }

}