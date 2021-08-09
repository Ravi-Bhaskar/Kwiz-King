package com.raam.kwizking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.raam.kwizking.databinding.ActivityForgotPasswordBinding;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;

                email = binding.emailBox.getText().toString();

                if (email.isEmpty()) {
                    binding.emailBox.setError("Email is Required");
                    binding.emailBox.requestFocus();
                } else {
                    forgetPassword(email);
                }
            }
        });
        binding.backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });
    }

    private void forgetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Check Your Mail For Creating New Password", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}