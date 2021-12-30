package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageCar.ACarListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CarOwnerViewInterface extends AppCompatActivity {

    private TextView ic, email, id,  name, phone, campus;
    private Button reject, approve;
    private ImageView icPhoto, idPhoto;

    private FirebaseDatabase db;
    private DatabaseReference coDBRef;
    private FirebaseAuth auth;
    private Uri icFile, idFile, licenceFile;

    private String uid, nameCO, emailCO, phoneCO, matricCO, campusCO, idCO, icCO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owner_view_interface);
        this.setTitle("Registration");

        name = findViewById(R.id.coName);
        ic = findViewById(R.id.coIC);
        id = findViewById(R.id.coID);
        email = findViewById(R.id.coEmail);
        phone = findViewById(R.id.coPhone);
        icPhoto = findViewById(R.id.coICPhoto);
        idPhoto = findViewById(R.id.coIDPhoto);
        campus = findViewById(R.id.coCampus);
        reject = findViewById(R.id.aReject);
        approve = findViewById(R.id.aApprove);

        nameCO = getIntent().getStringExtra("coName");
        emailCO = getIntent().getStringExtra("emailCo");
        icCO = getIntent().getStringExtra("icCo");
        uid = getIntent().getStringExtra("coId");
        phoneCO = getIntent().getStringExtra("phoneCo");
        matricCO = getIntent().getStringExtra("matricIDCo");

        name.setText(nameCO);
        ic.setText(icCO);
        id.setText(matricCO);
        email.setText(emailCO);
        phone.setText(phoneCO);
        if (getIntent().getStringExtra("campusCo").equals("Gambang")){
            campus.setText("Gambang");
        }
        else {
            campus.setText("Pekan");
        }

        String ic = getIntent().getStringExtra("ICUriCo");
        icFile = Uri.parse(ic);
        String id = getIntent().getStringExtra("IDUriCo");
        idFile = Uri.parse(id);
        String uid = getIntent().getStringExtra("uid");

        try{
            Picasso.get().load(icFile).placeholder(R.drawable.add_image).into(icPhoto);
        }
        catch (Exception e) {

        }
        try{
            Picasso.get().load(idFile).placeholder(R.drawable.add_image).into(idPhoto);
        }
        catch (Exception e) {

        }

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                coDBRef = db.getReference("Users");
                Query query = coDBRef.child("Users").orderByChild("coId").equalTo(uid);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coDBRef.child(uid).child("coStatus").setValue("Approved");
                        Intent intent2reject = new Intent(CarOwnerViewInterface.this, CarOwnerListInterface.class);
                        startActivity(intent2reject);
                        Toast.makeText(CarOwnerViewInterface.this, "You have approved the registration.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2reject = new Intent(CarOwnerViewInterface.this, CarOwnerRejectInterface.class);
                intent2reject.putExtra("uid", uid);
                startActivity(intent2reject);
            }
        });
        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.car:
                        startActivity(new Intent(getApplicationContext(), ACarListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), CarOwnerListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), AdminMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

}