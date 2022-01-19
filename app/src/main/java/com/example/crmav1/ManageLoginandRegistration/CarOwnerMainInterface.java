package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.Adapter.ChatListAdapter;
import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageAccount.CarOwnerProfileInterface;
import com.example.crmav1.ManageAccount.CarOwnerRejectInterface;
import com.example.crmav1.ManageAccount.CarOwnerViewInterface;
import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageCar.AddCarInterface;
import com.example.crmav1.ManageCar.CarListInterface;
import com.example.crmav1.Model.Chat;
import com.example.crmav1.Model.Chatlist;
import com.example.crmav1.Model.Student;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarOwnerMainInterface extends AppCompatActivity implements ChatListAdapter.ItemClickListener {

    private Button deleteAcc, addCar, viewCar;

    ProgressDialog progressDialogAdd, progressDialogDel, progressDialogView;

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private FirebaseUser CarOwner;
    private List<Chatlist> userlist;
    private List<Student> studentList;
    private DatabaseReference coDBRef, databaseReference;
    private FirebaseAuth auth;
    private TextView chat;
    private String coID;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owner_main_interface);

        progressDialogDel = new ProgressDialog(this);
        progressDialogDel.setMessage("Deleting...");
        context = this;

        progressDialogView = new ProgressDialog(this);
        progressDialogView.setMessage("Loading...");

        chat = findViewById(R.id.chat);

        recyclerView = findViewById(R.id.chatList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userlist = new ArrayList<>();

        deleteAcc = findViewById(R.id.coDelete);
        addCar = findViewById(R.id.addCar);
        viewCar = findViewById(R.id.viewCar);

        CarOwner = FirebaseAuth.getInstance().getCurrentUser();
        coDBRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();


        coID = CarOwner.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(coID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chatlist chatlist = dataSnapshot.getValue(Chatlist.class);
                    userlist.add(chatlist);
                }

                readChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                if (statusCo.equalsIgnoreCase("Rejected")){
                    viewCar.setVisibility(View.GONE);
                    addCar.setVisibility(View.GONE);
                    chat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                else if (statusCo.equalsIgnoreCase("Approved")){
                    deleteAcc.setVisibility(View.GONE);
                }
                else {
                    viewCar.setVisibility(View.GONE);
                    addCar.setVisibility(View.GONE);
                    deleteAcc.setVisibility(View.GONE);
                    chat.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }

                viewCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialogView.show();
                        Intent intent2view = new Intent(CarOwnerMainInterface.this, CarListInterface.class);
                        startActivity(intent2view);
                    }
                });

                addCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2add = new Intent(CarOwnerMainInterface.this, AddCarInterface.class);
                        startActivity(intent2add);
                    }
                });

                deleteAcc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog deleteAlert = confirmation();
                        deleteAlert.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //bottom Nav
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
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CarOwnerMainInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    private void readChat() {
        studentList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Student student = dataSnapshot.getValue(Student.class);
                    if (student.getUserType().equalsIgnoreCase("Student"))
                    {
                        for (Chatlist chatlist : userlist)
                        {
                            if (student.getsId() != null && student.getsId().equals(chatlist.getId()))
                            {
                                studentList.add(student);
                            }
                        }
                    }
                }
                adapter = new ChatListAdapter(context, studentList, CarOwnerMainInterface.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private AlertDialog confirmation(){
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Try register again.")
                .setMessage("Hope you will get approval as car owner next time! See you soon.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        FirebaseUser coID = FirebaseAuth.getInstance().getCurrentUser();
                        coID.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                String userID = coID.getUid();
                                DatabaseReference ID = FirebaseDatabase.getInstance().getReference("Users");
                                ID.child(userID).removeValue();
                                progressDialogDel.show();
                                Toast.makeText(CarOwnerMainInterface.this, "Your account is deleted. Please REGISTER again for login!!", Toast.LENGTH_SHORT).show();
                                Intent intent2delete = new Intent(CarOwnerMainInterface.this, LoginInterface.class);
                                startActivity(intent2delete);
                            }
                        });

                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    @Override
    public void onItemClick(int position) {

    }
}