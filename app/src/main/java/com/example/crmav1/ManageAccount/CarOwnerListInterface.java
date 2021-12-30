package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.crmav1.Adapter.RegistrationListAdapter;
import com.example.crmav1.ManageCar.ACarListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarOwnerListInterface extends AppCompatActivity implements RegistrationListAdapter.ItemClickListenser {
    private RecyclerView recyclerView;
    private DatabaseReference coRef;
    private ArrayList<CarOwner> registerList;
    private RegistrationListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owner_list_interface);
        recyclerView = findViewById(R.id.registerListView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        registerList = new ArrayList<>();

        adapter = new RegistrationListAdapter(this, registerList, this);
        recyclerView.setAdapter(adapter);

        coRef = FirebaseDatabase.getInstance().getReference();
        Query query = coRef.child("Users").orderByChild("userType").equalTo("Car Owner");
        Query query2 = coRef.child("Users").orderByChild("coStatus").equalTo("Pending");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                CarOwner co = dataSnapshot.getValue(CarOwner.class);
                                registerList.add(co);
                            }

                            Toast.makeText(getApplicationContext(), "Data Found", Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getApplicationContext(),"No Data Found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getDetails(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemClick(int position) {
        Intent intent2view = new Intent(this, CarOwnerViewInterface.class);
        intent2view.putExtra("coName", registerList.get(position).getCoName());
        intent2view.putExtra("emailCo", registerList.get(position).getCoEmail());
        intent2view.putExtra("icCo", registerList.get(position).getCoIC());
        intent2view.putExtra("campusCo",registerList.get(position).getCoCampus());
        intent2view.putExtra("ICUriCo", registerList.get(position).getCoICUri());
        intent2view.putExtra("IDUriCo", registerList.get(position).getCoIDUri());
        intent2view.putExtra("matricIDCo", registerList.get(position).getCoMatricID());
        intent2view.putExtra("phoneCo", registerList.get(position).getCoPhone());
        intent2view.putExtra("reasonCo", registerList.get(position).getReason());
        intent2view.putExtra("statusCo", registerList.get(position).getCoStatus());
        intent2view.putExtra("uid", registerList.get(position).getCoId());
        startActivity(intent2view);
    }
}