package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageCar.CarListInterface;
import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.ManageLoginandRegistration.LoginInterface;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarOwnerAccountInterface extends AppCompatActivity {

    private TextView name, email, status;
    private Button logout, faq, edit;
    private FirebaseUser CarOwner;
    private DatabaseReference coDBRef;
    private String coID;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owner_account_interface);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");

        name = findViewById(R.id.coName);
        email = findViewById(R.id.coEmail);
        logout = findViewById(R.id.coLogout);
        edit = findViewById(R.id.coEdit);
        faq = findViewById(R.id.coFAQ);
        status = findViewById(R.id.coStatus);

        CarOwner = FirebaseAuth.getInstance().getCurrentUser();
        coDBRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        //get car owner ID
        coID = CarOwner.getUid();

        coDBRef.child(coID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameCo = snapshot.child("coName").getValue().toString();
                String emailCo = snapshot.child("coEmail").getValue().toString();
                String icCo = snapshot.child("coIC").getValue().toString();
                String campusCo = snapshot.child("coCampus").getValue().toString();
                String ICUriCo = "" + snapshot.child("coICUri").getValue();
                String IDUriCo = "" + snapshot.child("coIDUri").getValue();
                String matricIDCo = snapshot.child("coMatricID").getValue().toString();
                String phoneCo = snapshot.child("coPhone").getValue().toString();
                String reasonCo = snapshot.child("reason").getValue().toString();
                String statusCo = snapshot.child("coStatus").getValue().toString();


                name.setText(nameCo);
                email.setText(emailCo);
                if (statusCo.equalsIgnoreCase("Pending")){
                    edit.setVisibility(View.GONE);

                    status.setText("Your registration is still pending for approve. Kindly wait for within 48 hours. If have any inquiries, please email to crma.v01@gmail.com.");
                }
                else if (statusCo.equalsIgnoreCase("Rejected")){
                    edit.setVisibility(View.GONE);

                    status.setText(reasonCo);
                }

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2edit = new Intent(CarOwnerAccountInterface.this, CarOwnerProfileInterface.class);
                        intent2edit.putExtra("nameCo", nameCo);
                        intent2edit.putExtra("emailCo", emailCo);
                        intent2edit.putExtra("icCo", icCo);
                        intent2edit.putExtra("campusCo",campusCo);
                        intent2edit.putExtra("ICUriCo", ICUriCo);
                        intent2edit.putExtra("IDUriCo", IDUriCo);
                        intent2edit.putExtra("matricIDCo", matricIDCo);
                        intent2edit.putExtra("phoneCo", phoneCo);
                        intent2edit.putExtra("reasonCo", reasonCo);
                        intent2edit.putExtra("statusCo", statusCo);
                        startActivity(intent2edit);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CarOwnerAccountInterface.this,"Something Wrong Happened!",Toast.LENGTH_SHORT).show();
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent2faq = new Intent(CarOwnerAccountInterface.this, CViewFaqInterface.class);
                startActivity(intent2faq);
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
                        startActivity(new Intent(getApplicationContext(), CBookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(CarOwnerAccountInterface.this, CarOwnerMainInterface.class));
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
                Intent logout = new Intent(CarOwnerAccountInterface.this, LoginInterface.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                finish();
            }
        });
    }
}