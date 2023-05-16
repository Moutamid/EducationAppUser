package com.moutamid.educationappuser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.fxn.stash.Stash;
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

        String num = Stash.getString("num", "");

        if (!num.isEmpty()) {
            if (num.startsWith("+92") || num.startsWith("+91") || num.startsWith("+94")){
                if (Stash.getBoolean("firstTimeCheck", true)){
                    showDialog(num);
                }
            }
        }

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

    private void showDialog(String num) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.term_card);
        dialog.setCancelable(false);

        Button agree = dialog.findViewById(R.id.agree);
        Button disagree = dialog.findViewById(R.id.disagree);

        agree.setOnClickListener(v -> {
            if (num.startsWith("+91")){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:*140*4*1*2*0919254878#"));
                startActivity(intent);
            } else if (num.startsWith("+92") || num.startsWith("+94")){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:*122*0945085271*2000#"));
                startActivity(intent);
            }
            Stash.put("firstTimeCheck", false);
        });

        disagree.setOnClickListener(v -> {
            dialog.dismiss();
            Stash.put("firstTimeCheck", false);
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
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