package com.example.crmav1.ManageLoginandRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class EmailVerificationInterface extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference dbRef;

    private Button verify, login;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification_interface);

        auth = FirebaseAuth.getInstance();
        verify = findViewById(R.id.btnVerify);
        login = findViewById(R.id.btnLogin);
        message = findViewById(R.id.emailVerify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EmailVerificationInterface.this, "Email of verification is sent. Please kindly checck your email.", Toast.LENGTH_LONG).show();
                        verify.setVisibility(View.GONE);
                        message.setVisibility(View.GONE);
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginInterface.class));
                finish();
                Toast.makeText(EmailVerificationInterface.this, "Please login.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}