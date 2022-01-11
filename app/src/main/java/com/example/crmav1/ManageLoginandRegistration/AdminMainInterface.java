package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.crmav1.Adapter.CarListAdapter;
import com.example.crmav1.Adapter.FaqAdapter;
import com.example.crmav1.ManageAccount.AddFaqInterface;
import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageAccount.CarOwnerViewInterface;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageCar.ACarListInterface;
import com.example.crmav1.Model.FAQ;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMainInterface extends AppCompatActivity implements FaqAdapter.ItemClickListener {

    private Button logout, faq;
    private RecyclerView recyclerView;

    private FirebaseAuth auth;
    private DatabaseReference faqRef;
    private ArrayList<FAQ> faqArrayList;
    private FaqAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_interface);

        logout = findViewById(R.id.aLogout);
        faq = findViewById(R.id.addFaq);
        recyclerView = findViewById(R.id.faqListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqArrayList = new ArrayList<>();

        adapter = new FaqAdapter(this, faqArrayList, this);
        recyclerView.setAdapter(adapter);

        faqRef = FirebaseDatabase.getInstance().getReference("Faq");

        faqRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        FAQ faq1 = dataSnapshot.getValue(FAQ.class);

                            faqArrayList.add(faq1);

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

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2faq = new Intent(AdminMainInterface.this, AddFaqInterface.class);
                startActivity(intent2faq);
            }
        });


        auth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent logout = new Intent(AdminMainInterface.this, LoginInterface.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
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
                    case R.id.car:
                        startActivity(new Intent(getApplicationContext(), ACarListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), CarOwnerListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
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