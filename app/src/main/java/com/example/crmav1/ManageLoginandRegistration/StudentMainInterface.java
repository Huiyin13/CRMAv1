package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.crmav1.Adapter.RegistrationListAdapter;
import com.example.crmav1.ManageBooking.BookingListInterface;
import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.R;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageBooking.SCarListInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentMainInterface extends AppCompatActivity implements RegistrationListAdapter.ItemClickListenser {

    private RecyclerView recyclerView1;
    private DatabaseReference coRef;
    private ArrayList<CarOwner> coList;
    private RegistrationListAdapter coAdapter;
    private String coId, coName;
    private ToggleButton aSwitch;
    private TextView tv;

    private FirebaseUser auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main_interface);

        aSwitch = findViewById(R.id.switchCampus1);

        recyclerView1 = findViewById(R.id.coListView);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        coList = new ArrayList<>();

        coAdapter = new RegistrationListAdapter(this, coList, this);
        recyclerView1.setAdapter(coAdapter);

        auth = FirebaseAuth.getInstance().getCurrentUser();

        coRef = FirebaseDatabase.getInstance().getReference();
        Query query = coRef.child("Users").orderByChild("coStatus").equalTo("Approved");
        Query query1 = coRef.child("Users").orderByChild("coCampus").equalTo("Pekan");
        Query query2 = coRef.child("Users").orderByChild("coCampus").equalTo("Gambang");
        if (!aSwitch.isChecked())
        {
            query1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    coList.clear();
                    if (snapshot.exists())
                    {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            CarOwner carOwner = dataSnapshot.getValue(CarOwner.class);
                            coId = carOwner.getCoId();
                            coName = carOwner.getCoName();
                            if (carOwner.getCoStatus().equalsIgnoreCase("Approved")){
                                coList.add(carOwner);
                            }

                        }
                        coAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(StudentMainInterface.this, "No data", Toast.LENGTH_LONG).show();
                        System.out.println("error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked())
                {
                    System.out.println("Gambang");
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            coList.clear();
                            if (snapshot.exists())
                            {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    CarOwner carOwner = dataSnapshot.getValue(CarOwner.class);
                                    coId = carOwner.getCoId();
                                    coName = carOwner.getCoName();
                                    if (carOwner.getCoStatus().equalsIgnoreCase("Approved")){
                                        coList.add(carOwner);
                                    }

                                }
                                coAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(StudentMainInterface.this, "No data", Toast.LENGTH_LONG).show();
                                System.out.println("error");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    System.out.println("Pekan");
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            coList.clear();
                            if (snapshot.exists())
                            {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    CarOwner carOwner = dataSnapshot.getValue(CarOwner.class);
                                    coId = carOwner.getCoId();
                                    coName = carOwner.getCoName();
                                    if (carOwner.getCoStatus().equalsIgnoreCase("Approved")){
                                        coList.add(carOwner);
                                    }

                                }
                                coAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(StudentMainInterface.this, "No data", Toast.LENGTH_LONG).show();
                                System.out.println("error");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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
                        return true;
                    case R.id.account:
                        startActivity(new Intent(StudentMainInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent2view = new Intent(this, SCarListInterface.class);
        intent2view.putExtra("coId", coId);
        intent2view.putExtra("coName", coName);
        startActivity(intent2view);

    }
}