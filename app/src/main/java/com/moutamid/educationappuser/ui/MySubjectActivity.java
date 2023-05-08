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
import com.moutamid.educationappuser.databinding.ActivityMySubjectBinding;
import com.moutamid.educationappuser.models.ClassModel;
import com.moutamid.educationappuser.models.ItemModel;
import com.moutamid.educationappuser.models.SubjectModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class MySubjectActivity extends AppCompatActivity {
    ActivityMySubjectBinding binding;
    ArrayList<ItemModel> list;
    String ID;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ID = getIntent().getStringExtra(Constants.ID);

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        list = new ArrayList<>();

        binding.header.tittle.setText("Select Your Subject");

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.Subject).child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ItemModel model = new ItemModel(R.drawable.round_menu_book_24, dataSnapshot.getValue(SubjectModel.class));
                        list.add(model);
                    }
                    Collections.reverse(list);
                }
                progressDialog.dismiss();
                ClassAdapter adapter = new ClassAdapter(MySubjectActivity.this, list);
                binding.recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(MySubjectActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}