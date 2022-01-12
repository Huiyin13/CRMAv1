package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.crmav1.Adapter.BookingListAdapter;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
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

public class BookingListInterface extends AppCompatActivity implements BookingListAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private DatabaseReference bookRef;
    private ArrayList<Booking> bookings;
    private BookingListAdapter adapter;
    private FirebaseUser auth;
    private Button history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list_interface);

        recyclerView = findViewById(R.id.bookListView);
        history = findViewById(R.id.history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookings = new ArrayList<>();

        adapter = new BookingListAdapter(this, bookings, this);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        bookRef = FirebaseDatabase.getInstance().getReference("Booking").child(auth.getUid());

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       Booking book = dataSnapshot.getValue(Booking.class);
                       if (book.getbStatus().equalsIgnoreCase("Completed") || book.getbStatus().equalsIgnoreCase("Cancelled")){
                           if (bookings.size()<0){
                               Toast.makeText(getApplicationContext(), "No Current Booking", Toast.LENGTH_LONG).show();
                           }

                       }
                       else {
                           bookings.add(book);
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

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2history = new Intent(BookingListInterface.this, SBookingHistoryInterface.class);
                startActivity(intent2history);
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
                        startActivity(new Intent(BookingListInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(BookingListInterface.this, StudentAccountInterface.class));
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