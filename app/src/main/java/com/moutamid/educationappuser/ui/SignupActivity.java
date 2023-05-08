package com.moutamid.educationappuser.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.databinding.ActivitySignupBinding;
import com.moutamid.educationappuser.models.UserModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating your account");

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.signup.setOnClickListener(v -> {
            if (valid()) {
                progressDialog.show();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    String ID = Constants.auth().getCurrentUser().getUid();
                    UserModel userModel = new UserModel(ID, binding.name.getEditText().getText().toString(),
                            binding.email.getEditText().getText().toString(), binding.password.getEditText().getText().toString());

                    Constants.databaseReference().child(Constants.USER).child(ID).setValue(userModel)
                            .addOnSuccessListener(unused -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            });

                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private boolean valid() {
        if (binding.name.getEditText().getText().toString().isEmpty()){
            binding.name.getEditText().setError("required*");
            binding.name.getEditText().requestFocus();
            return false;
        }
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("required*");
            binding.email.getEditText().requestFocus();
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("required*");
            binding.password.getEditText().requestFocus();
            return false;
        }
        return true;
    }

}