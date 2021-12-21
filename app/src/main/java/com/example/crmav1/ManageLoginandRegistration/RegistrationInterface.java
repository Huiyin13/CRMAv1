package com.example.crmav1.ManageLoginandRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.crmav1.R;

public class RegistrationInterface extends AppCompatActivity {

    private Button stdRegister, ownerRegister, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_interface);

        this.setTitle("Register");

        stdRegister = findViewById(R.id.btnStd);
        ownerRegister = findViewById(R.id.btnOwner);
        login = findViewById(R.id.btnLogin);

        stdRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2studentRegister = new Intent(RegistrationInterface.this, StudentRegistrationInterface.class);
                startActivity(intent2studentRegister);
            }
        });

        ownerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ownerRegister = new Intent(RegistrationInterface.this, CarOwnerRegistrationInterface.class);
                startActivity(intent2ownerRegister);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2Login = new Intent(RegistrationInterface.this, LoginInterface.class);
                startActivity(intent2Login);
            }
        });
    }
}