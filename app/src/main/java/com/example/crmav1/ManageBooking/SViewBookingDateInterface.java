package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.crmav1.Adapter.Holder;
import com.example.crmav1.Adapter.ViewBookedDateAdapter;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SViewBookingDateInterface extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference bookRef;
    private ArrayList<Booking> bookedDate;
    private ViewBookedDateAdapter adapter;
    private String coId,cid, fee;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sview_booking_date_interface);

        recyclerView = findViewById(R.id.viewBook);
        back = findViewById(R.id.back);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookedDate = new ArrayList<>();
        adapter = new ViewBookedDateAdapter(this, bookedDate);
        recyclerView.setAdapter(adapter);

        fee = getIntent().getStringExtra("fee");
        coId = getIntent().getStringExtra("coId");
        cid = getIntent().getStringExtra("cid");
        bookRef = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking");
//        bookRef = FirebaseDatabase.getInstance().getReference("Booking");

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot dataSnapshot : DataSnapshot.getChildren()){

                                Booking booking = dataSnapshot.getValue(Booking.class);
                                System.out.println(booking.getbStatus() + "4567890");
                                if (booking.getCid().equalsIgnoreCase(cid)){
                                    if (booking.getbStatus().equalsIgnoreCase("Applying") || booking.getbStatus().equalsIgnoreCase("Pick Up")
                                            || booking.getbStatus().equalsIgnoreCase("Accepted")){
                                        bookedDate.add(booking);
                                    }

                                }


                        }
                    }
                    Toast.makeText(getApplicationContext(), "Data Found", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Data Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getDetails(), Toast.LENGTH_LONG).show();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2back = new Intent(SViewBookingDateInterface.this, BookingFormInterface.class);
                intent2back.putExtra("fee", fee);
                intent2back.putExtra("cid", cid);
                intent2back.putExtra("coId", coId);
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
                        startActivity(new Intent(getApplicationContext(), BookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(SViewBookingDateInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SViewBookingDateInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}