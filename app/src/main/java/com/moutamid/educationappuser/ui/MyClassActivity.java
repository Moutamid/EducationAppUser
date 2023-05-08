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
import com.moutamid.educationappuser.adapters.ClassAdapter;
import com.moutamid.educationappuser.databinding.ActivityMyClassBinding;
import com.moutamid.educationappuser.models.ClassModel;
import com.moutamid.educationappuser.models.ItemModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MyClassActivity extends AppCompatActivity {
    ActivityMyClassBinding binding;
    ArrayList<ItemModel> list;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        list = new ArrayList<>();

        binding.header.tittle.setText("Select Your Class");

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.Class).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ItemModel model = new ItemModel(R.drawable.round_class_24, dataSnapshot.getValue(ClassModel.class));
                        list.add(model);
                    }
                    Collections.reverse(list);
                }
                progressDialog.dismiss();
                ClassAdapter adapter = new ClassAdapter(MyClassActivity.this, list);
                binding.recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(MyClassActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}