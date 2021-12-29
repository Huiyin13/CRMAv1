package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageBooking.BookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.LoginInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentAccountInterface extends AppCompatActivity {

    private TextView name, email;
    private Button logout, faq, edit;
    private FirebaseUser Student;
    private DatabaseReference stdDBRef;
    private String stdID;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account_interface);

        name = findViewById(R.id.stdName);
        email = findViewById(R.id.stdEmail);
        logout = findViewById(R.id.stdLogout);
        edit = findViewById(R.id.stdEdit);
        faq = findViewById(R.id.stdFAQ);

        Student = FirebaseAuth.getInstance().getCurrentUser();
        stdDBRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        //get student ID
        stdID = Student.getUid();

        stdDBRef.child(stdID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameS = snapshot.child("sName").getValue().toString();
                String emailS = snapshot.child("sEmail").getValue().toString();
                String icS = snapshot.child("sIC").getValue().toString();
                String campusS = snapshot.child("sCampus").getValue().toString();
                String ICUriS = "" + snapshot.child("sICUri").getValue();
                String IDUriS = "" + snapshot.child("sIDUri").getValue();
                String LicenceUriS = "" + snapshot.child("sLicenceUri").getValue();
                String matricIDS = snapshot.child("sMatricID").getValue().toString();
                String phoneS = snapshot.child("sPhone").getValue().toString();

                name.setText(nameS);
                email.setText(emailS);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2edit = new Intent(StudentAccountInterface.this, StudentProfileInterface.class);
                        intent2edit.putExtra("nameS", nameS);
                        intent2edit.putExtra("emailS", emailS);
                        intent2edit.putExtra("icS", icS);
                        intent2edit.putExtra("campusS", campusS);
                        intent2edit.putExtra("ICUriS", ICUriS);
                        intent2edit.putExtra("IDUriS", IDUriS);
                        intent2edit.putExtra("LicenceUriS", LicenceUriS);
                        intent2edit.putExtra("matricIDS", matricIDS);
                        intent2edit.putExtra("phoneS", phoneS);
                        startActivity(intent2edit);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentAccountInterface.this,"Something Wrong Happened!",Toast.LENGTH_SHORT).show();
            }
        });

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.book:
                        startActivity(new Intent(getApplicationContext(), BookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(StudentAccountInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        return true;
                }
                return false;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent logout = new Intent(StudentAccountInterface.this, LoginInterface.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(logout);

            }
        });

    }
}