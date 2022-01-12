package com.example.crmav1.ManageBooking;

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

import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Student;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CStudentProfileInterface extends AppCompatActivity {

    private TextView name, id, ic, phone, email, campus;
    private ImageView icPhoto, idPhoto, licencePhoto;
    private Button back;

    private String cid, bid, sId;
    private DatabaseReference student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cstudent_profile_interface);

        name = findViewById(R.id.stdName);
        id = findViewById(R.id.stdID);
        ic = findViewById(R.id.stdIC);
        phone = findViewById(R.id.stdPhone);
        email = findViewById(R.id.stdEmail);
        icPhoto = findViewById(R.id.stdICPhoto);
        idPhoto = findViewById(R.id.stdIDPhoto);
        licencePhoto = findViewById(R.id.stdLicencePhoto);
        back = findViewById(R.id.back);
        campus = findViewById(R.id.stdCampus);

        cid = getIntent().getStringExtra("cid");
        bid = getIntent().getStringExtra("bid");
        sId = getIntent().getStringExtra("sId");

        student = FirebaseDatabase.getInstance().getReference("Users").child(sId);

        student.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Student std = snapshot.getValue(Student.class);
                    name.setText(std.getsName());
                    id.setText(std.getsMatricID());
                    ic.setText(std.getsIC());
                    phone.setText(std.getsPhone());
                    email.setText(std.getsEmail());
                    campus.setText(std.getsCampus());

                    Picasso.get().load(std.getsICUri()).into(icPhoto);
                    Picasso.get().load(std.getsIDUri()).into(idPhoto);
                    Picasso.get().load(std.getsLicenceUri()).into(licencePhoto);
                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + std.getsPhone()));//change the number
                            startActivity(callIntent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2back = new Intent(CStudentProfileInterface.this, CBookingDetailsInterface.class);
                intent2back.putExtra("sId", sId);
                intent2back.putExtra("bid", bid);
                intent2back.putExtra("cid", cid);
                startActivity(intent2back);
                finish();
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
                        startActivity(new Intent(CStudentProfileInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CStudentProfileInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}