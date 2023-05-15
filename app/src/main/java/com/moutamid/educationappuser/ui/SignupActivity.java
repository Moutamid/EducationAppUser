package com.moutamid.educationappuser.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.moutamid.educationappuser.MainActivity;
import com.moutamid.educationappuser.R;
import com.moutamid.educationappuser.databinding.ActivitySignupBinding;
import com.moutamid.educationappuser.models.UserModel;
import com.moutamid.educationappuser.utilis.Constants;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    ProgressDialog progressDialog;
    String username, email, password;
    ProgressDialog dialog6;

    String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending OTP");

        dialog6 = new ProgressDialog(this);
        dialog6.setCancelable(false);
        dialog6.setMessage("Creating Your Account");

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.signup.setOnClickListener(v -> {
            if (valid()) {
                progressDialog.show();
                username = binding.name.getEditText().getText().toString();
                email = binding.email.getEditText().getText().toString().trim();
                password = binding.password.getEditText().getText().toString();
                Stash.put("num", binding.email.getEditText().getText().toString().trim());
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(Constants.auth())
                                .setPhoneNumber(binding.email.getEditText().getText().toString().trim())       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                Constants.auth().useAppLanguage();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential c) {
            final String code = c.getSmsCode();
            if (code!=null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            progressDialog.dismiss();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }
            Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationId = s;
            verifyCode();
            progressDialog.dismiss();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void verifyCode() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_layout);
        dialog.setCancelable(false);

        TextInputLayout otp = dialog.findViewById(R.id.otp);
        otp.getEditText().requestFocus();

        Button verify = dialog.findViewById(R.id.verify);
        verify.setOnClickListener(v -> {
            if (otp.getEditText().getText().toString().isEmpty()){
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                dialog6.show();
                verifyCode(otp.getEditText().getText().toString());
                dialog.dismiss();
            }

        });

        Button retry = dialog.findViewById(R.id.retry);
        retry.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        Constants.auth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            String ID = user.getUid();
                            UserModel userModel = new UserModel(ID, username, email, password);
                            Constants.databaseReference().child(Constants.USER).child(ID).setValue(userModel)
                                    .addOnSuccessListener(unused -> {
                                        dialog6.dismiss();
                                        Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish();
                                    }).addOnFailureListener(e -> {
                                        dialog6.dismiss();
                                        Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignupActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                            dialog6.dismiss();
                        }
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