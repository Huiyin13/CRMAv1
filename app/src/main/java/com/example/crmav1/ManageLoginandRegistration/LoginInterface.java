package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginInterface extends AppCompatActivity {

    private EditText email, password;
    private RadioButton admin, student, carOwner;
    private Button login, register;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_interface);

        this.setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        admin = findViewById(R.id.rbAdmin);
        student = findViewById(R.id.rbStd);
        carOwner = findViewById(R.id.rbOwner);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Email is missing!");
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Password is missing!");
                    return;
                }

                //login user
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //read data from database
                        //read from Users table
                        dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String userType = snapshot.child("userType").getValue().toString();
                                if(userType.equalsIgnoreCase("Student") && student.isChecked()){
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                        startActivity(new Intent(getApplicationContext(), StudentMainInterface.class));
                                        finish();
                                        Toast.makeText(LoginInterface.this, "Successfully login as a student.", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(firebaseAuth != null)
                                    {
                                        startActivity(new Intent(LoginInterface.this,StudentMainInterface.class));
                                        finish();
                                    }
                                    else{
                                        startActivity(new Intent(getApplicationContext(), EmailVerificationInterface.class));
                                    }
                                }
                                else if(userType.equalsIgnoreCase("Car Owner") && carOwner.isChecked()){
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                        startActivity(new Intent(getApplicationContext(), CarOwnerMainInterface.class));
                                        finish();
                                        Toast.makeText(LoginInterface.this, "Successfully login as a car owner.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        startActivity(new Intent(getApplicationContext(), EmailVerificationInterface.class));
                                    }
                                }
                                else if(userType.equalsIgnoreCase("Admin") && admin.isChecked()){
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                        startActivity(new Intent(getApplicationContext(), AdminMainInterface.class));
                                        finish();
                                        Toast.makeText(LoginInterface.this, "Successfully login as a admin.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                         startActivity(new Intent(getApplicationContext(), EmailVerificationInterface.class));
                                    }
                                }
                                else{
                                    finish();
                                    startActivity(getIntent());
                                    Toast.makeText(LoginInterface.this, "Unsuccessfully login.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginInterface.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2register = new Intent(LoginInterface.this, RegistrationInterface.class);
                startActivity(intent2register);
            }
        });

    }

    //maintain the session
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//        if(firebaseUser != null)
//        {
//            startActivity(new Intent(LoginInterface.this,MainActivity.class));
//            finish();
//        }
//    }
}