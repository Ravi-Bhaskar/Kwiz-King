package com.raam.kwizking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raam.kwizking.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);

        binding.RegisterBtn.setOnClickListener(v -> {
            String email, pass, name, referCode;

            email = binding.emailBox.getText().toString();
            pass = binding.passwordBox.getText().toString();
            name = binding.nameBox.getText().toString();
            referCode = binding.referBox.getText().toString();
            if (name.isEmpty()) {
                binding.nameBox.setError("Please Enter Your Name");
                binding.nameBox.requestFocus();
            } else if (email.isEmpty()) {
                binding.emailBox.setError("Please Enter Email");
                binding.emailBox.requestFocus();
            } else if (!email.matches(emailPattern)) {
                binding.emailBox.setError("Please Enter Correct Email");
                binding.emailBox.requestFocus();
            } else if (pass.length() == 0) {
                binding.passwordBox.setError("Please Enter Password");
                binding.passwordBox.requestFocus();
            } else if (email.isEmpty() && pass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
            } else if (!(email.isEmpty() && pass.isEmpty())) {


                User user = new User(name, email, pass, referCode);


                dialog.setMessage("Registration in Process Please Wait...");
                dialog.setTitle("Register");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();

                            database
                                    .collection("users")
                                    .document(uid)
                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.AlreadyHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}