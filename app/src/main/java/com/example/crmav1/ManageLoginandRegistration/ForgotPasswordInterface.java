package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crmav1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordInterface extends AppCompatActivity {

    private EditText email;
    private Button reset;

    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_interface);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending email...");

        email = findViewById(R.id.loginEmail);
        reset = findViewById(R.id.btnReset);
        
        auth = FirebaseAuth.getInstance();
        
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String emailR = email.getText().toString().trim();

        if (emailR.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailR).matches()){
            email.setError("Please provide valid email");
            email.requestFocus();
        }

        auth.sendPasswordResetEmail(emailR).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.show();
                    Toast.makeText(ForgotPasswordInterface.this, "Check your email to reset password.", Toast.LENGTH_SHORT).show();
                    Intent intent2login = new Intent(ForgotPasswordInterface.this, LoginInterface.class);
                    startActivity(intent2login);
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordInterface.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
}