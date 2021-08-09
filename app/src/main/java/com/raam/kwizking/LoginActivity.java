package com.raam.kwizking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.raam.kwizking.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    FirebaseAuth auth;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");

        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.passwordBox.setTransformationMethod(null);
                } else {
                    binding.passwordBox.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        binding.LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                if (email.isEmpty()){
                    binding.emailBox.setError("Please Enter Email");
                    binding.emailBox.requestFocus();
                } else if (!email.matches(emailPattern)) {
                    binding.emailBox.setError("Please Enter Correct Email");
                    binding.emailBox.requestFocus();
                } else if (pass.length()==0){
                    binding.passwordBox.setError("Please Enter Password");
                    binding.passwordBox.requestFocus();
                }else if (email.isEmpty() && pass.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }else if (!(email.isEmpty() && pass.isEmpty())) {


                    dialog.setMessage("Please Wait While Login...");
                    dialog.setTitle("Login");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.CreateAnAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        binding.forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

    }
}