package com.moutamid.educationappuser.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.educationappuser.MainActivity;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.adapters.ScoreAdapter;
import com.moutamid.educationappuser.databinding.ActivityScoreBinding;
import com.moutamid.educationappuser.models.SaveScoreModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    ActivityScoreBinding binding;
    ArrayList<SaveScoreModel> list;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.header.tittle.setText("Your Quiz Score");

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.SCORE).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                SaveScoreModel model = dataSnapshot.getValue(SaveScoreModel.class);
                                list.add(model);
                            }
                        } else {
                            Toast.makeText(ScoreActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        ScoreAdapter adapter = new ScoreAdapter(ScoreActivity.this, list);
                        binding.recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ScoreActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }
}