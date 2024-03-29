package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.crmav1.Adapter.BookingListAdapter;
import com.example.crmav1.Adapter.CBookingListAdapter;
import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CBookingHistoryInterface extends AppCompatActivity implements CBookingListAdapter.ItemClickListener {

    private RecyclerView recyclerView,recyclerView2;
    private DatabaseReference bookRef;
    private ArrayList<Booking> bookings, bookingsCancel;
    private CBookingListAdapter adapter, adapter2;
    private FirebaseUser auth;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbooking_history_interface);

        recyclerView = findViewById(R.id.bookListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookings = new ArrayList<>();
        recyclerView2 = findViewById(R.id.bookListView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        bookingsCancel = new ArrayList<>();

        adapter = new CBookingListAdapter(this, bookings,  this);
        recyclerView.setAdapter(adapter);
        adapter2 = new CBookingListAdapter(this, bookingsCancel,  this);
        recyclerView2.setAdapter(adapter2);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        bookRef = FirebaseDatabase.getInstance().getReference("Booking");

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Booking booked = dataSnapshot1.getValue(Booking.class);
                            sid = booked.getsId();
                            if (booked.getCoId().equals(auth.getUid())){
                                if (booked.getbStatus().equalsIgnoreCase("Completed")){
                                    bookings.add(booked);
                                }

                                else if (booked.getbStatus().equalsIgnoreCase("Cancelled")){
                                    bookingsCancel.add(booked);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "No Booking Record", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Data Found", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Data Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        startActivity(new Intent(CBookingHistoryInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CBookingHistoryInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}